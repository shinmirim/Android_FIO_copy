package com.example.flo

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class AlbumVPAdapter(fragment:Fragment) : FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int = 3 //3개의 프래그먼트 생성(수록곡, 상세정보, 영상)

    //3개의 프래그먼트가 전달하는 내용이 다르기 때문에 각각 만들어준다. When문법사용 (조건에 따라 다른 작업 가능하게 함)
    override fun createFragment(position: Int): Fragment {
        return when(position){//포지션에 따라 바뀔 수 있도록

            0 -> SongFragment()//수록곡

            1 -> DetailFragment()//상세정보

            else -> VideoFragment()//영상

        }
    }
}