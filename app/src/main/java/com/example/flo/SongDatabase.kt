package com.example.flo

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase


@Database(entities = [Song::class], version =1)
abstract class SongDatabase: RoomDatabase(){
    abstract fun songDao(): SongDao

    companion object{
        private var instance : SongDatabase? = null

        @Synchronized
        fun getInstance(context: Context):SongDatabase?{
            if (instance == null){
                synchronized(SongDatabase::class){
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        SongDatabase::class.java,
                        "song-database"//다른 데이터 베이스랑 이름 겹치면 꼬

                    ).allowMainThreadQueries().build()//메인스레드에 해당하는 정보를 넘겨줌
                }
            }

            return  instance
        }
    }
}
