package com.example.flo_clone

import android.os.*
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.flo_clone.databinding.FragmentLockerSavedsongBinding

class SavedSongFragment() : Fragment(), Parcelable {
    lateinit var binding: FragmentLockerSavedsongBinding

    constructor(parcel: Parcel) : this() {

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLockerSavedsongBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {

    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<SavedSongFragment> {
        override fun createFromParcel(parcel: Parcel): SavedSongFragment {
            return SavedSongFragment(parcel)
        }

        override fun newArray(size: Int): Array<SavedSongFragment?> {
            return arrayOfNulls(size)
        }
    }

}