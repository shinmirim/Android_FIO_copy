package com.example.flo

import android.media.MediaPlayer
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.flo.databinding.ActivitySongBinding
import com.google.gson.Gson
class SongActivity : AppCompatActivity() {

    //소괄호 : 클래스를 다른 클래스로 상속을 진행할 때는 소괄호를 넣어줘야 한다.

    //전역 변수
    lateinit var binding : ActivitySongBinding
    lateinit var song: Song
    lateinit var timer :Timer
    private var mediaPlayer: MediaPlayer ?= null
    private var gson : Gson = Gson()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySongBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //song 데이터 받아오기
        initSong()
       // setPlayer(song)




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
        mediaPlayer?.release()//미디어 플레이어가 가지고 있던 리소스 해제 - 불필요한 리소스 사용 하지 않기 위해
        mediaPlayer = null // 미디어 플레이어 해제
    }
    //사용자가 포커스를 잃었을 때 음악중지
    override fun onPause(){
        super.onPause()
        setPlayerStatus(false)
        song.second = ((binding.songProgressSb.progress * song.playTime)/100)/1000
        val sharedPreferences = getSharedPreferences("song", MODE_PRIVATE)
        val editor = sharedPreferences.edit() // 에디터
        //editor.putString("title",song.title)
        val songJson = gson.toJson(song)
        editor.putString("songData",songJson)

        editor.apply()//여기까지 해야 실제 저장공간에 저장이 된다.

    }

    //song에 대한 데이터 렌더링 ?
    private fun setPlayer(song: Song){
        binding.songMusicTitleTv.text = intent.getStringExtra("title")
        binding.songSingerNameTv.text = intent.getStringExtra("singer")
        binding.songStartTimeTv.text = String.format("%02d:%02d",song.second /60, song.second % 60)
        binding.songEndTimeTv.text = String.format("%02d:%02d", song.playTime / 60, song.playTime % 60)
        binding.songProgressSb.progress = (song.second * 1000 / song.playTime)
        val music = resources.getIdentifier(song.music,"raw",this.packageName) //ㄹㅣ소스를 반환 받아줌
        mediaPlayer = MediaPlayer.create(this,music)// 미디어 플레이어에게 이 음악을 재생할 거야라는 것을 알려줌

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
            mediaPlayer?.start() //음악재생
        } else {
            binding.songMiniplayerIv.visibility = View.VISIBLE
            binding.songPauseIv.visibility = View.GONE
            if(mediaPlayer?.isPlaying == true ){
                mediaPlayer?.pause()// 재생중이 아닐 시에 오류에 생길 수 있어 if문을 넣어줌
            }
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


