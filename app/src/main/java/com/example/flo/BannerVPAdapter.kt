package com.example.flo

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

//인자값이 fragment , fragment타입
//Adaptar의 경우 하나의 클래스를 상속 받아야한다.
class BannerVPAdapter(fragment:Fragment) :FragmentStateAdapter(fragment) {

    //여러개의 fragment들 담아두기
    private val fragmentlist : ArrayList<Fragment> = ArrayList()

    //해당 클래스에 연결된 뷰페이저에게 데이터를 전달할 데이터를 몇개를 전달할 것이냐를 써주는 함 : fragmentlist담긴 데이터의 갯
    override fun getItemCount(): Int = fragmentlist.size//안에 갯수를 가져오는 것이기에 Size를 사

    override fun createFragment(position: Int): Fragment = fragmentlist[position]

    fun addFragment(fragment: Fragment){
        fragmentlist.add(fragment)
        notifyItemInserted(fragmentlist.size-1)
    }
}