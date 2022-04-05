package com.example.flo

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.flo.databinding.ActivitySongBinding

class SongActivity : AppCompatActivity() {

    //소괄호 : 클래스를 다른 클래스로 상속을 진행할 때는 소괄호를 넣어줘야 한다.

    //전역 변수
    lateinit var binding : ActivitySongBinding
    lateinit var song: Song
    lateinit var timer :Timer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySongBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //song 데이터 받아오기
        initSong()
        setPlayer(song)




        //if(intent.hasExtra("title") && intent.hasExtra("singer")){
           //binding.songMusicTitleTv.text = intent.getStringExtra("title")
           //binding.songSingerNameTv.text = intent.getStringExtra("singer")
       //}

        binding.songDownIb.setOnClickListener {
            finish()
        }

        binding.songMiniplayerIv.setOnClickListener {
            setPlayerStatus(true)
        }

        binding.songPauseIv.setOnClickListener {
            setPlayerStatus(false)
        }


    }

    //강제 종료시에 쓰레드 꺼지도록 //쓰레드 종료시 interrupt
    override fun onDestroy(){
        super.onDestroy()
        timer.interrupt()
    }

    //song에 대한 데이터 렌더링 ?
    private fun setPlayer(song: Song){
        binding.songMusicTitleTv.text = intent.getStringExtra("title")
        binding.songSingerNameTv.text = intent.getStringExtra("singer")
        binding.songStartTimeTv.text = String.format("%02d:%02d",song.second /60, song.second % 60)
        binding.songEndTimeTv.text = String.format("%02d:%02d", song.playTime / 60, song.playTime % 60)
        binding.songProgressSb.progress = (song.second * 1000 / song.playTime)

        setPlayerStatus(song.isPlaying)
    }


    private fun initSong(){
        if(intent.hasExtra("title") && intent.hasExtra("singer")){
            song = Song(
                intent.getStringExtra("title")!!,
                intent.getStringExtra("singer")!!,
                intent.getIntExtra("second",0),
                intent.getIntExtra("playTime",60),
                intent.getBooleanExtra("isPlaying",false)

            )
        }
        startTimer()
    }



    fun setPlayerStatus (isPlaying : Boolean){
        song.isPlaying=isPlaying
        timer.isPlaying=isPlaying

        if(isPlaying){
            binding.songMiniplayerIv.visibility = View.GONE
            binding.songPauseIv.visibility = View.VISIBLE
        } else {
            binding.songMiniplayerIv.visibility = View.VISIBLE
            binding.songPauseIv.visibility = View.GONE
        }
    }


    private fun startTimer(){
        timer = Timer(song.playTime,song.isPlaying)
        timer.start()
    }

    //스레드 생성
    inner class Timer(private val playTime: Int, var isPlaying: Boolean = true):Thread(){
        //외부에서 노래의 진행시간을 second단위와 mills단위로 받아올 것

        private var second : Int=0
        private var mills : Float =0f

        override fun run(){
            //타이머의 경우 계속 진행되어야하기 때문에 계속 반복시켜줘야한다.
            super.run()
            try{
                while(true){
                    if(second >= playTime){
                        break
                    }

                    if (isPlaying){
                        sleep(50)
                        mills +=50

                        //seekbar구현

                        runOnUiThread{
                            binding.songProgressSb.progress = ((mills/playTime)*100).toInt()
                        }

                        //타이머구현
                        if(mills % 1000 == 0f){
                            //뷰렌더링 작업
                            runOnUiThread{
                                binding.songStartTimeTv.text = String.format("%02d:%02d",song.second /60,second % 60)

                            }
                            second++
                        }

                    }
                }

            }catch(e: InterruptedException){
                Log.d("Song","쓰레드가 죽었습니다. ${e.message}")
            }

            }
        }

    }
