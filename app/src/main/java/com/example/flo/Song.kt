package com.example.flo

import androidx.room.Entity
import androidx.room.PrimaryKey


//제목, 가수, 사진,재생시간,현재 재생시간, isplaying(재생 되고 있는지)


@Entity(tableName = "SongTable")
data class Song(
        val title : String = "",
        val singer : String = "",
        var second : Int=0,// 노래가 얼만큼 재생되었는지
        val playTime : Int=0, //총 재생시간
        var isPlaying : Boolean = false,// 지금 노래가 재생중인지
        var music:String="",//어떤 음악이 재생되고 있었는지를 알려주 변수
        var coverImg: Int? = null,
        var isLike: Boolean = false

){

        @PrimaryKey(autoGenerate = true) var id:Int =0
}
