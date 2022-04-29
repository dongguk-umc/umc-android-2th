package com.example.flo_clone

import android.media.MediaPlayer
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.flo_clone.databinding.ActivitySongBinding
import com.google.gson.Gson


class SongActivity : AppCompatActivity() {

    //소괄호 : 클래스를 다른 클래스로 상속을 진행할 때는 소괄호를 넣어줘야 한다.

    lateinit var binding : ActivitySongBinding
    lateinit var song : Song
    lateinit var timer: Timer
    private var mediaPlayer: MediaPlayer? = null //null값이 들어올 수 있음
    private var gson: Gson = Gson()



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySongBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initSong()
        setPlayer(song)


        binding.songDownIb.setOnClickListener {
            finish()
        }

        binding.songMiniplayerIv.setOnClickListener {
            setPlayerStatus(true)
        }

        binding.songPauseIv.setOnClickListener {
            setPlayerStatus(false)
        }


    }

    override fun onPause() {
        super.onPause()
        setPlayerStatus((false))
        song.second = ((binding.songProgressSb.progress * song.playTime)/100)/1000 //밀리 세컨드로 계산
        val sharedPreferences = getSharedPreferences("song", MODE_PRIVATE)
        //내부 저장소에 데이터를 저장하는 것, 앱이 종료되어도 저장소에서 꺼내서 데이터 씀
        //간단한 데이터에 용이 자기 앱에서만 사용할수 있도록
        val editor = sharedPreferences.edit() // 에디터
        val songJson = gson.toJson(song)
        editor.putString("songData",songJson)

        editor.apply()

    }

        override fun onDestroy() {
            super.onDestroy()
            timer.interrupt()
            mediaPlayer?.release() //미디어플레이어가 갖고 있던 리소스 해제
            mediaPlayer = null //미디어 플레이어 해제
        }

    private fun initSong(){
        if(intent.hasExtra("title") && intent.hasExtra("singer")){
            song = Song(
                intent.getStringExtra("title")!!,
                intent.getStringExtra("singer")!!,
                intent.getIntExtra("second",0),
                intent.getIntExtra("playTime",0),
                intent.getBooleanExtra("isPlaying",false),
                intent.getStringExtra("music")!!

            )
        }
        startTimer()
    }

    private fun setPlayer(song: Song){
        binding.songMusicTitleTv.text = intent.getStringExtra("title")!!
        binding.songSingerNameTv.text = intent.getStringExtra("singer")!!
        binding.songStartTimeTv.text = String.format("%02d:%02d",song.second / 60, song.second % 60)
        binding.songEndTimeTv.text = String.format("%02d:%02d",song.playTime / 60, song.playTime % 60)
        binding.songProgressSb.progress = (song.second * 1000 / song.playTime)

        val music = resources.getIdentifier(song.music,"raw",this.packageName)
        mediaPlayer = MediaPlayer.create(this,music)

        setPlayerStatus(song.isPlaying)

    }


    private fun setPlayerStatus (isPlaying : Boolean){
        song.isPlaying = isPlaying
        timer.isPlaying = isPlaying

        if(isPlaying){
            binding.songMiniplayerIv.visibility = View.GONE
            binding.songPauseIv.visibility = View.VISIBLE
            mediaPlayer?.start()

        } else {
            binding.songMiniplayerIv.visibility = View.VISIBLE
            binding.songPauseIv.visibility = View.GONE
            if(mediaPlayer?.isPlaying ==true)
            {
                mediaPlayer?.pause()
            }

        }

    }

    private fun startTimer(){
        timer = Timer(song.playTime,song.isPlaying)
        timer.start()
    }

    inner class Timer(private val playTime: Int,var isPlaying: Boolean = true):Thread(){

        private var second : Int = 0
        private var mills: Float = 0f

        override fun run() {
            super.run()
            try {
                while (true){

                    if (second >= playTime){
                        break
                    }

                    if (isPlaying){
                        sleep(50)
                        mills += 50

                        runOnUiThread {
                            binding.songProgressSb.progress = ((mills / playTime)*100).toInt()
                        }

                        if (mills % 1000 == 0f){
                            runOnUiThread {
                                binding.songStartTimeTv.text = String.format("%02d:%02d",second / 60, second % 60)
                            }
                            second++
                        }

                    }

                }

            }catch (e: InterruptedException){
                Log.d("Song","쓰레드가 죽었습니다. ${e.message}")
            }
        }
    }


}