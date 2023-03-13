package com.example.letmeknow.Adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.letmeknow.DataClass.PollDataClass
import com.example.letmeknow.R

class ViewPollsAdapter(private var DataList: ArrayList<PollDataClass>) :
    RecyclerView.Adapter<ViewPollsAdapter.ViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val ItemView = LayoutInflater.from(parent.context).inflate(R.layout.each_poll, parent, false)
        return ViewHolder(ItemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentData = DataList[position]
        holder.tvQues.text = currentData.PollQues.toString()
        holder.tvOp1.text = currentData.Options[0].toString()
        holder.tvOp2.text = currentData.Options[1].toString()
        holder.tvOp3.text = currentData.Options[2].toString()
        holder.tvOp4.text = currentData.Options[3].toString()
    }

    override fun getItemCount(): Int {
        return DataList.size
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view){
        val tvQues : TextView = view.findViewById(R.id.Question)
        val tvOp1 : TextView = view.findViewById(R.id.Op1)
        val tvOp2 : TextView = view.findViewById(R.id.Op2)
        val tvOp3 : TextView = view.findViewById(R.id.Op3)
        val tvOp4 : TextView = view.findViewById(R.id.Op4)
    }

}


