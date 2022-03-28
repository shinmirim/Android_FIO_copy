package com.example.FIO_MIRIM

import android.os.Bundle
import android.text.Layout
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.FIO_MIRIM.databinding.ActivitySongBinding

class SongActivity :AppCompatActivity(){
    lateinit var binding : ActivitySongBinding // 바인딩선언

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //바인딩 초기화
        binding = ActivitySongBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.songDownIb.setOnClickListener{
            finish()
        }//다시 메인액티비티로 전
        binding.songMiniplayerIv.setOnClickListener{
             setPlayerStatus(false)
        }// 재생버튼 누르면 멈춤 버튼 나오도록 // 아직 재생안됨
        binding.songPauseIv.setOnClickListener{
            setPlayerStatus(true)
        }//재생시작

    }//처음으 실행되는 함수 , 자유롭게 채우기 위해 override 를 써줌

    //함수 생성
    fun setPlayerStatus(isPlaying : Boolean ){
        if(isPlaying){ // 노래가 재생 중이지 않을
            binding.songMiniplayerIv.visibility = View.VISIBLE
            binding.songPauseIv.visibility = View.GONE

        }else{ // 노래가 재생 중 일때
            binding.songMiniplayerIv.visibility = View.GONE
            binding.songPauseIv.visibility = View.VISIBLE
        }
    }//플레이어 상태가 어떤지 확인 할 수 있다.
}


