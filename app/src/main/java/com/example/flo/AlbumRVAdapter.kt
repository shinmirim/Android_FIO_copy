package com.example.flo

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ExpandableListView
import androidx.recyclerview.widget.RecyclerView
import com.example.flo.databinding.ItemAlbumBinding

class AlbumRVAdapter(private val albumList: ArrayList<Album>): RecyclerView.Adapter<AlbumRVAdapter.ViewHolder>(){

    interface MyItemClickListenter{
        //아이템이 클릭되었을
        fun onItemClick(album: Album)
        //fun onRemoveAlbum(position: Int)
    }

    private  lateinit var mItemClickListener: MyItemClickListenter
    fun setMyItemClickListener(itemClickListener:MyItemClickListenter){
        mItemClickListener = itemClickListener
    }

    fun addItem(album: Album){
        albumList.add(album)
        notifyDataSetChanged()//데이터가 바뀌었다는 것을 알려줘야한다.

    }

    fun removeItem(position: Int){
        albumList.removeAt(position)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        //TODO("Not yet implemented")
        //아이템 객체 생성해 저장
        val binding: ItemAlbumBinding = ItemAlbumBinding.inflate(LayoutInflater.from(viewGroup.context),viewGroup, false)


        return  ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        //TODO("Not yet implemented")
        //데이터 바인딩 해줌
        holder.bind(albumList[position])
        // 아이템 뷰가 클릭되었을 때
        holder.itemView.setOnClickListener{ mItemClickListener.onItemClick(albumList[position])}
       // 타이틀 뷰가 클릭되었을 때 삭제되도록
        //holder.binding.itemAlbumTitleTv.setOnClickListener{mItemClickListener.onRemoveAlbum(position)}
    }

    //리사이클러뷰가 마지막이 언제 인지
    override fun getItemCount(): Int = albumList.size




    inner class  ViewHolder(val binding: ItemAlbumBinding): RecyclerView.ViewHolder(binding.root){

        fun bind(album: Album){
            binding.itemAlbumTitleTv.text = album.title
            binding.itemAlcumSingerTv.text = album.singer
            binding.itemAlbumCoverImgIv.setImageResource(album.coverImg!!)
        }
    }

}