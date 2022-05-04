package com.example.flo

import androidx.room.*

@Dao
interface SongDao {
    @Insert
    fun insert(song: Song)

    @Update
    fun update(song: Song)

    @Delete
    fun delete(dong: Song)

    @Query("SELECT * FROM SongTable")
    fun getSongs(): List<Song>//리스트에 저장되어 있는 송을 전부 받아오게 될 것


    @Query("SELECT * FROM SongTable WHERE id=:id ")
    fun getSongs(id:Int): Song
}