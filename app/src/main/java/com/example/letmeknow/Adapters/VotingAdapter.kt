package com.example.letmeknow.Adapters

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.example.letmeknow.Activity.SignInActivity
import com.example.letmeknow.DataClass.PollDataClass
import com.example.letmeknow.R
import com.squareup.picasso.Picasso

class VotingAdapter(private var DataList: ArrayList<PollDataClass>, val context: Context) :
    RecyclerView.Adapter<VotingAdapter.ViewHolder>(){

    private lateinit var mListener: onItemClickListener
    private var voteOn = ArrayList<Int>()

    interface onItemClickListener{
        fun onItemClick(position: Int, voteOn : ArrayList<Int>)
    }

    fun setOnItemClickListener(listener: onItemClickListener){
        mListener = listener
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val ItemView = if(DataList[viewType].isMulti){
            LayoutInflater.from(parent.context).inflate(R.layout.multi_voting_poll, parent, false)
        }else{
            LayoutInflater.from(parent.context).inflate(R.layout.single_voting_poll, parent, false)
        }

        return ViewHolder(ItemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentData = DataList[position]
        if(currentData.QuesImg!=null){
            Picasso.get().load(currentData.QuesImg).into(holder.ivQuesImg)
            holder.ivQuesImg.layoutParams.height = 200
        }

        holder.tvQues.text = currentData.PollQues.toString()



        val currTimeMilli : Long = System.currentTimeMillis()

        if (holder.timer!=null) {
            holder.timer!!.cancel()
        }
        holder.timer = object : CountDownTimer(currentData.endTimeMilli - currTimeMilli , 1000) {
            override fun onTick(p0: Long) {
                holder.tvTimer.text = convert_to_timer(p0)
            }

            override fun onFinish() {
                DataList.remove(currentData)
            }

        }.start()

        if(currentData.isMulti){
            holder.c1!!.text = currentData.Options[0]
            holder.c2!!.text = currentData.Options[1]
            holder.c3!!.text = currentData.Options[2]
            holder.c4!!.text = currentData.Options[3]

            holder.voteButton.setOnClickListener{
                if(holder.c1.isChecked || holder.c2.isChecked || holder.c3.isChecked || holder.c4.isChecked){
                    val builder = AlertDialog.Builder(context)
                    builder.setTitle("CONFIRM VOTE?")
//            builder.setMessage("Are you sure?")
                    builder.setPositiveButton("CONFIRM", DialogInterface.OnClickListener{ dialog, which->
                        voteOn.clear()
                        if(holder.c1.isChecked){
                            voteOn.add(0)
                        }
                        if(holder.c2.isChecked) {
                            voteOn.add(1)
                        }
                        if(holder.c3.isChecked) {
                            voteOn.add(2)
                        }
                        if(holder.c4.isChecked) {
                            voteOn.add(3)
                        }

                        mListener.onItemClick(position, voteOn)
                    })
                    builder.setNegativeButton("Cancel", DialogInterface.OnClickListener{ dialog, which-> })
                    val alertDialog: AlertDialog = builder.create()
                    alertDialog.show()
                }else{
                    Toast.makeText(context, "Please select an option first", Toast.LENGTH_SHORT).show()
                }
            }

        }else{
            holder.r1!!.text = currentData.Options[0]
            holder.r2!!.text = currentData.Options[1]
            holder.r3!!.text = currentData.Options[2]
            holder.r4!!.text = currentData.Options[3]

            holder.voteButton.setOnClickListener{
                if(holder.r1.isChecked || holder.r2.isChecked || holder.r3.isChecked || holder.r4.isChecked){
                    val builder = AlertDialog.Builder(context)
                    builder.setTitle("CONFIRM VOTE?")
//            builder.setMessage("Are you sure?")
                    builder.setPositiveButton("CONFIRM", DialogInterface.OnClickListener{ dialog, which->
                        voteOn.clear()
                        if(holder.r1.isChecked) voteOn.add(0)
                        if(holder.r2.isChecked) voteOn.add(1)
                        if(holder.r3.isChecked) voteOn.add(2)
                        if(holder.r4.isChecked) voteOn.add(3)

                        mListener.onItemClick(position, voteOn)
                    })
                    builder.setNegativeButton("Cancel", DialogInterface.OnClickListener{ dialog, which-> })
                    val alertDialog: AlertDialog = builder.create()
                    alertDialog.show()
                }else{
                    Toast.makeText(context, "Please select an option first", Toast.LENGTH_SHORT).show()
                }
            }
        }

    }


    override fun getItemCount(): Int {
        return DataList.size
    }


    private fun convert_to_timer(p0: Long): String {
        val sec = (p0/1000%60).toInt()
        val min = (p0/(1000*60)%60).toInt()
        val hrs = (p0/(1000*60*60)%24).toInt()
        val days = (p0/(1000*60*60*24)).toInt()
        return (days.toString() + String.format(":%02d:%02d:%02d",hrs,min,sec))

    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view){
        val tvQues : TextView = view.findViewById(R.id.Question)
        val ivQuesImg : ImageView = view.findViewById(R.id.quesImg)


        val r1 : RadioButton? = view.findViewById(R.id.R1)
        val r2 : RadioButton? = view.findViewById(R.id.R2)
        val r3 : RadioButton? = view.findViewById(R.id.R3)
        val r4 : RadioButton? = view.findViewById(R.id.R4)

        val c1 : CheckBox? = view.findViewById(R.id.C1)
        val c2 : CheckBox? = view.findViewById(R.id.C2)
        val c3 : CheckBox? = view.findViewById(R.id.C3)
        val c4 : CheckBox? = view.findViewById(R.id.C4)

        val voteButton : Button = view.findViewById(R.id.VoteButton)
        val tvTimer : TextView = view.findViewById(R.id.Timer)
//        var ivLive : ImageView = view.findViewById(R.id.Live)
        var timer : CountDownTimer?=null

    }

}


