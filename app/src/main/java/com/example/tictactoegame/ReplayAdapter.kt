package com.example.tictactoegame

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ReplayAdapter(private val replayList : ArrayList<Replay>) : RecyclerView.Adapter<ReplayAdapter.MyViewHolder>(){


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.replay_item,
            parent,false)
        return MyViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return replayList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentitem = replayList[position]

        when (currentitem.name) {
            "Nought" -> {
                holder.roleIcon.text = "O"
                holder.roleIcon.setTextColor(Color.RED)
            }
            "CROSS" -> {
                holder.roleIcon.text = "X"
                holder.roleIcon.setTextColor(Color.BLUE)
            }
            else -> {
                holder.roleIcon.text = "D"
                holder.roleIcon.setTextColor(Color.YELLOW)
            }
        }

        holder.winner.text = currentitem.name
        holder.layouts.text = currentitem.layout
        holder.date.text = currentitem.date
        holder.time.text = currentitem.time
    }

    class MyViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){

        val roleIcon : TextView = itemView.findViewById(R.id.role_tv)
        val winner : TextView = itemView.findViewById(R.id.winner_name_tv)
        val layouts : TextView = itemView.findViewById(R.id.layout_tv)
        val date : TextView = itemView.findViewById(R.id.date_tv)
        val time : TextView = itemView.findViewById(R.id.time_tv)



    }
}