package com.example.FIO_MIRIM

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment //프래그먼트의 기능을 사용할 수 있는 클래스

import com.example.FIO_MIRIM.databinding.FragmentAlbumBinding

class Albumfragment :Fragment() {
    //바인딩 선언
     lateinit var  binding : FragmentAlbumBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //바인딩 초기화
        binding = FragmentAlbumBinding.inflate(inflater,container,false)

        //다시 홈프래그먼트로 되돌아가기
        binding.albumAlbumIv.setOnClickListener{
            (context as MainActivity).supportFragmentManager.beginTransaction().replace(R.id.main_frm,HomeFragment()).commitAllowingStateLoss()
        }

        //binding.songAlbumLayout.setOnClickListener{
           //oast.makeText(activity,"LILAC",Toast.LENGTH_SHORT).show()
        //}
        return binding.root
    }
}