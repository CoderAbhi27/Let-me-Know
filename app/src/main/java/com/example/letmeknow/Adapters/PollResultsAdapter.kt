package com.example.letmeknow.Adapters


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.letmeknow.DataClass.PollDataClass
import com.example.letmeknow.R
import com.squareup.picasso.Picasso

class PollResultsAdapter(private var DataList: ArrayList<PollDataClass>) :
    RecyclerView.Adapter<PollResultsAdapter.ViewHolder>(){

//    var onItemclick : ((PollDataClass) -> Unit)?= null

    private lateinit var mListener: onItemClickListener

    interface onItemClickListener{
        fun onItemClick(position: Int)
    }

    fun setOnItemClickListener(listener: onItemClickListener){
        mListener = listener
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val ItemView = LayoutInflater.from(parent.context).inflate(R.layout.each_poll_result, parent, false)
        return ViewHolder(ItemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.setIsRecyclable(false)
        val currentData = DataList[position]
        if(currentData.QuesImg!=null){
            Picasso.get().load(currentData.QuesImg).into(holder.ivQuesImg)
            holder.ivQuesImg.layoutParams.height = 200
        }

        var totVotes : Int = 0
        for(i in 0..3){
            totVotes+=currentData.Votes[i]
        }

        val perVotes : ArrayList<Float> = ArrayList()
        for(i in 0..3){
            val x: Float = if(totVotes==0) 0f else (currentData.Votes[i]*1000/totVotes)/10f
            perVotes.add(x)
        }

        holder.tvQues.text = currentData.PollQues.toString()
        holder.tvOp1.text = currentData.Options[0]
        holder.tvOp2.text = currentData.Options[1]
        holder.tvOp3.text = currentData.Options[2]
        holder.tvOp4.text = currentData.Options[3]
        holder.tvVote1.text = "${perVotes[0]}%"
        holder.tvVote2.text = "${perVotes[1]}%"
        holder.tvVote3.text = "${perVotes[2]}%"
        holder.tvVote4.text = "${perVotes[3]}%"
        holder.pb1.progress = perVotes[0].toInt()
        holder.pb2.progress = perVotes[1].toInt()
        holder.pb3.progress = perVotes[2].toInt()
        holder.pb4.progress = perVotes[3].toInt()


        holder.detailsButton.setOnClickListener{
            mListener.onItemClick(position)
        }
    }


    override fun getItemCount(): Int {
        return DataList.size
    }


    class ViewHolder(view: View) : RecyclerView.ViewHolder(view){
        val tvQues : TextView = view.findViewById(R.id.Question)
        val ivQuesImg : ImageView = view.findViewById(R.id.quesImg)
        val tvOp1 : TextView = view.findViewById(R.id.Op1)
        val tvOp2 : TextView = view.findViewById(R.id.Op2)
        val tvOp3 : TextView = view.findViewById(R.id.Op3)
        val tvOp4 : TextView = view.findViewById(R.id.Op4)
        val tvVote1 : TextView = view.findViewById(R.id.VoteOn1)
        val tvVote2 : TextView = view.findViewById(R.id.VoteOn2)
        val tvVote3 : TextView = view.findViewById(R.id.VoteOn3)
        val tvVote4 : TextView = view.findViewById(R.id.VoteOn4)
        val pb1 : ProgressBar = view.findViewById(R.id.pb1)
        val pb2 : ProgressBar = view.findViewById(R.id.pb2)
        val pb3 : ProgressBar = view.findViewById(R.id.pb3)
        val pb4 : ProgressBar = view.findViewById(R.id.pb4)
        val detailsButton : Button = view.findViewById(R.id.ViewBarChart)


    }

}


