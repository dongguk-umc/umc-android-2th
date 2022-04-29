package com.example.flo_clone

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.example.flo_clone.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    lateinit var binding: FragmentHomeBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)

        binding.homePannelTodaymusicAlbumcover1Iv.setOnClickListener {
            (context as MainActivity).supportFragmentManager.beginTransaction()
                .replace(R.id.main_frm , AlbumFragment())
                .commitAllowingStateLoss()
        }

        val bannerAdapter = BannerVPAdapter(this)
        bannerAdapter.addFragment(BannerFragmemt(R.drawable.img_home_viewpager_exp))
        bannerAdapter.addFragment(BannerFragmemt(R.drawable.img_home_viewpager_exp2))

        binding.homePannelViewpagerIv.adapter = bannerAdapter
        binding.homePannelViewpagerIv.orientation = ViewPager2.ORIENTATION_HORIZONTAL
        return binding.root
    }


}