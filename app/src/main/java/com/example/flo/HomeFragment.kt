package com.example.flo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager2.widget.ViewPager2
import com.example.flo.databinding.FragmentHomeBinding
import com.google.gson.Gson

class HomeFragment : Fragment() {

    lateinit var binding: FragmentHomeBinding
    private  var albumDatas = ArrayList<Album>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)

       /* binding.homeAlbumImgIv1.setOnClickListener {
            (context as MainActivity).supportFragmentManager.beginTransaction()
                .replace(R.id.main_frm , AlbumFragment())
                .commitAllowingStateLoss()
        }*/

        //데이터 리스트 생성 더미 데이터
        albumDatas.apply {
            add(Album("Butter","방탄소년단(BTS)",R.drawable.img_album_exp))
            add(Album("Lilac","아이유(IU)",R.drawable.img_album_exp2))
            add(Album("Butter","방탄소년단(BTS)",R.drawable.img_album_exp))
            add(Album("Butter","방탄소년단(BTS)",R.drawable.img_album_exp))
            add(Album("Butter","방탄소년단(BTS)",R.drawable.img_album_exp))
            add(Album("Butter","방탄소년단(BTS)",R.drawable.img_album_exp))

        }//원래는 데이터를 서버에서 받아오는 것인데 서버가 없으므로 데이터를 직접 입력 리사이클러뷰에 들어갈 데이터 준비 완료


        val albumVPAdapter = AlbumRVAdapter(albumDatas)
        binding.homeTodayMusicAlbumRv.adapter =albumVPAdapter
        binding.homeTodayMusicAlbumRv.layoutManager =LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL,false)


        //리사이클러뷰에 아이템이 클릭되었을 때 앨범프레그먼트로 전환되도록
        albumVPAdapter.setMyItemClickListener(object: AlbumRVAdapter.MyItemClickListenter{
            override fun onItemClick(album: Album) {
                changeAlbumFragment(album)

            }

            //override fun onRemoveAlbum(position: Int) {
                //albumVPAdapter.removeItem(position)
            //}

            private fun changeAlbumFragment(album: Album) {
                (context as MainActivity).supportFragmentManager.beginTransaction()
                    .replace(R.id.main_frm, AlbumFragment().apply {
                        arguments = Bundle().apply {
                            val gson = Gson()
                            val albumJson = gson.toJson(album)
                            putString("album", albumJson)
                        }
                    })
                    .commitAllowingStateLoss()
            }

        })


        val bannerAdapter = BannerVPAdapter(this)//초기
        bannerAdapter.addFragment(BannerFragment(R.drawable.img_home_viewpager_exp))//추가할 프래그먼트를 가로 안에 써준다.
        bannerAdapter.addFragment(BannerFragment(R.drawable.img_home_viewpager_exp2))
        binding.homeBannerVp.adapter = bannerAdapter
        binding.homeBannerVp.orientation = ViewPager2.ORIENTATION_HORIZONTAL

        return binding.root
    }


}