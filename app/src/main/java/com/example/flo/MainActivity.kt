package com.example.flo

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import com.example.flo.databinding.ActivityMainBinding
import com.google.gson.Gson

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding
    private  var song:Song = Song()
    //lateinit var song: Song
    lateinit var timer :Timer
    private  var gson: Gson = Gson()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.Theme_FLO)//스플래시테마 실행된 후 다시 테마로 돌아옴 (메인화면으로 돌아갈때)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        inputDummySongs()




        //song 데이터 받아오기
        initSong()
        //setMiniPlayer(song)


        initBottomNavigation()

        //val song = Song(binding.mainMiniplayerTitleTv.text.toString(), binding.mainMiniplayerSingerTv.text.toString(),0,60,false,"old_pop")



        binding.mainPlayerCl.setOnClickListener {
            val intent = Intent(this,SongActivity::class.java)
            intent.putExtra("title", song.title)
            intent.putExtra("singer",song.singer)
            intent.putExtra("song",song.second)
            intent.putExtra("playTime",song.playTime)
            intent.putExtra("isPlaying",song.isPlaying)
            intent.putExtra("music",song.music)
            startActivity(intent)//메인에서 넘겨주고, SongActivitiy에서도 받아옴


            //버튼 클릭시 재생버튼으로 바뀔 수 있도록
            binding.mainMiniplayerBtn.setOnClickListener{
                setPlayerStatus(true)
            }

            binding.mainPauseBtn.setOnClickListener{
                setPlayerStatus(false)
            }
        }


    }


    //강제 종료시에 쓰레드 꺼지도록 //쓰레드 종료시 interrupt
    override fun onDestroy(){
        super.onDestroy()
        timer.interrupt()
    }

    //private fun setMiniPlayer(song : Song){
        //binding.mainMiniplayerTitleTv.text = song.title
       // binding.mainMiniplayerSingerTv.text = song.singer
       // binding.mainMiniplayerProgressSb.progress = (song.second*100000)/song.playTime
   // }

    //노래가 재생 중인지 아닌지 확인 함수
    fun setPlayerStatus (isPlaying : Boolean){
        song.isPlaying=isPlaying
        timer.isPlaying=isPlaying
        if(isPlaying){
            binding.mainMiniplayerBtn.visibility =View.GONE
            binding.mainPauseBtn.visibility =View.VISIBLE
        }else{
            binding.mainMiniplayerBtn.visibility =View.VISIBLE
            binding.mainPauseBtn.visibility =View.GONE
        }
    }

    //노래와 제목 데이터가 존재할 때는 데이터를 불러와 사용, 없다면 아직 DB가 없어 임의로 넣어
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
        else{
            song = Song(
                "Lilac",
                "아이유 (IU)",
                0,
                200,
                false
            )
        }
        startTimer()//현재 듣고 있던 노래가 얼만큼 진행되고 있는지를 보여줘야하기에 , 지속 실행해야하기
    }


    private fun startTimer(){
        timer = Timer(song.playTime,song.isPlaying)//재생을 하기 위해서는 재생이 되고 있는지와 총 재생시간을 알아야한다.
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
                    if(second >= playTime){//총 재생시간보다 초시간이 같거나 커질때
                        break
                    }

                    if (isPlaying){//재생이 되고 있다면 50천천히 하고 50을 쉬면서 바를 움직여라
                        sleep(50)
                        mills +=50

                        //seekbar구현

                        runOnUiThread{// Seekbar에 구현된 모습이 나타나도록
                            binding.mainMiniplayerProgressSb.progress = ((mills/playTime)*100).toInt()
                        }



                    }
                }

            }catch(e: InterruptedException){
                Log.d("Song","쓰레드가 죽었습니다. ${e.message}")
            }

        }
    }



    private fun initBottomNavigation(){

        supportFragmentManager.beginTransaction()
            .replace(R.id.main_frm, HomeFragment())
            .commitAllowingStateLoss()

        binding.mainBnv.setOnItemSelectedListener{ item ->
            when (item.itemId) {

                R.id.homeFragment -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.main_frm, HomeFragment())
                        .commitAllowingStateLoss()
                    return@setOnItemSelectedListener true
                }

                R.id.lookFragment -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.main_frm, LookFragment())
                        .commitAllowingStateLoss()
                    return@setOnItemSelectedListener true
                }
                R.id.searchFragment -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.main_frm, SearchFragment())
                        .commitAllowingStateLoss()
                    return@setOnItemSelectedListener true
                }
                R.id.lockerFragment -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.main_frm, LockerFragment())
                        .commitAllowingStateLoss()
                    return@setOnItemSelectedListener true
                }
            }
            false
        }
    }

    private fun setMiniPlayer(song: Song) {
        binding.mainMiniplayerTitleTv.text = song.title
        binding.mainMiniplayerSingerTv.text = song.singer
        binding.mainMiniplayerProgressSb.progress = (song.second * 100000)/song.playTime
    }



    private fun inputDummySongs(){
        val songDB = SongDatabase.getInstance(this)!!
        val songs = songDB.songDao().getSongs()//데이터가 이미 있다면 종료, 데이터가 없다면 넣어줌

        if (songs.isNotEmpty()) return

        //데이터 삽임은 DAO에 insert로 내부에 송데이터 클래스 넣어줌
        songDB.songDao().insert(
            Song(
                "old",
                "아이유(iu)",
                0,
                230,
                false,
                "old_pop",
                 R.drawable.img_album_exp,
                false,
            )
        )

        songDB.songDao().insert(
            Song(
                "old",
                "아이유(iu)",
                0,
                230,
                false,
                "old_pop",
                R.drawable.img_album_exp,
                false,
            )
        )

        songDB.songDao().insert(
            Song(
                "old",
                "아이유(iu)",
                0,
                230,
                false,
                "old_pop",
                R.drawable.img_album_exp,
                false,
            )
        )

        val _songs = songDB.songDao().getSongs()
        Log.d("DB data", _songs.toString())//로그를 사용해 들어간 데이터 확인
    }


    override fun onStart(){
        super.onStart()
        val sharedPreferences = getSharedPreferences("song", MODE_PRIVATE)
        val songJson = sharedPreferences.getString("songData",null )

        song = if(songJson == null){
            Song("라일락", "아이유(IU)",0,60,false,"music_lilac")
        } else {
            gson.fromJson(songJson, song::class.java) // sonJson을 자바 객체로 변환해줘라
        }

        setMiniPlayer(song)
    }
}