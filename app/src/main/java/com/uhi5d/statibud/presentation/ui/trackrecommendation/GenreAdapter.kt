package com.uhi5d.statibud.presentation.ui.trackrecommendation

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.RecyclerView
import com.uhi5d.statibud.R
import com.uhi5d.statibud.databinding.TfGenreSingleBinding
import com.uhi5d.statibud.util.BaseViewHolder

class GenreAdapter (
    var context: Context,
    var clickListener : OnItemClickListener
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    interface OnItemClickListener{
        fun onItemClicked(genre: String)
    }

    private var list = listOf<String>()
    fun setList(list: List<String>){
        this.list = list
        notifyDataSetChanged()
    }
    private var selectedList = mutableListOf<String>()
    fun setSelectedList(item: String){
        if (!selectedList.contains(item)){
            selectedList.add(item)
        }else{
            selectedList.remove(item)
        }
        notifyDataSetChanged()
    }
    fun getSelectedList(): List<String>{
        return selectedList
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val itemBinding = TfGenreSingleBinding.inflate(LayoutInflater.from(context),parent,false)
        return GenreViewHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(holder){
            is GenreViewHolder -> {holder.bind(list[position])}
        }
    }

    override fun getItemCount(): Int = list.size


    inner class GenreViewHolder(
        private val itemBinding: TfGenreSingleBinding
    ): BaseViewHolder<String>(itemBinding.root){
        override fun bind(item: String) {
            itemBinding.tvGenreText.text = item
            itemBinding.root.isSelected = selectedList.contains(item)
            if (selectedList.contains(item)){
                itemBinding.tvGenreText.background = ResourcesCompat.getDrawable(context.resources,
                    R.drawable.genre_item_background_selector,context.theme)
            }

            itemBinding.root.setOnClickListener {
                clickListener.onItemClicked(item)
            }
        }

    }


}