package com.example.flo


//제목, 가수, 사진,재생시간,현재 재생시간, isplaying(재생 되고 있는지)

data class Song(
        val title : String = "",
        val singer : String = "",
        val second : Int=0,// 노래가 얼만큼 재생되었는지
        val playTime : Int=0, //총 재생시간

        var isPlaying : Boolean = false// 지금 노래가 재생중인지
)
