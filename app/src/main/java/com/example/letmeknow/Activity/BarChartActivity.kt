package com.example.letmeknow.Activity

import android.content.DialogInterface
import android.content.Intent
import android.graphics.Color
import android.media.Image
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.core.view.get
import com.example.letmeknow.R
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.formatter.ValueFormatter
import com.github.mikephil.charting.utils.ColorTemplate
import com.squareup.picasso.Picasso

class BarChartActivity : AppCompatActivity() {

    private lateinit var barChart : BarChart

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        supportActionBar?.setTitle("Poll Details")
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        setContentView(R.layout.activity_bar_chart)

        val PollQues = intent.getStringExtra("PollQues")
        val quesImg = intent.getStringExtra("QuesImg")
        val PollOptions = intent.getStringArrayListExtra("PollOptions")
        val Votes = intent.getIntegerArrayListExtra("Votes")
        val endTimeMilli = intent.getLongExtra("endTime", 0L)
        val currTimeMilli : Long = System.currentTimeMillis()

        val perVote1 : Float
        val perVote2 : Float
        val perVote3 : Float
        val perVote4 : Float

        val sumVotes : Int = Votes!![0]+ Votes[1]+ Votes[2]+ Votes[3]
        if(sumVotes!=0){
            perVote1 = "%.1f".format(Votes[0]*100f/sumVotes).toFloat()
            perVote2 = "%.1f".format(Votes[1]*100f/sumVotes).toFloat()
            perVote3 = "%.1f".format(Votes[2]*100f/sumVotes).toFloat()
            perVote4 = "%.1f".format(Votes[3]*100f/sumVotes).toFloat()
        }else{
            perVote1 = 0f
            perVote2 = 0f
            perVote3 = 0f
            perVote4 = 0f
        }

        val tvQues : TextView = findViewById(R.id.Question)
        val ivQuesImg : ImageView = findViewById(R.id.quesImg)
        val tvOp1 : TextView = findViewById(R.id.Op1)
        val tvOp2 : TextView = findViewById(R.id.Op2)
        val tvOp3 : TextView = findViewById(R.id.Op3)
        val tvOp4 : TextView = findViewById(R.id.Op4)
        val tvPerVote1 : TextView = findViewById(R.id.PerVoteOn1)
        val tvPerVote2 : TextView = findViewById(R.id.PerVoteOn2)
        val tvPerVote3 : TextView = findViewById(R.id.PerVoteOn3)
        val tvPerVote4 : TextView = findViewById(R.id.PerVoteOn4)
        val tvTotalVotes : TextView = findViewById(R.id.TotalVotes)
        val tvTimer : TextView = findViewById(R.id.timer)
        val ivLive : ImageView = findViewById(R.id.live)

        tvQues.text = PollQues

        if(quesImg!=null){
            Picasso.get().load(quesImg).into(ivQuesImg)
            ivQuesImg.layoutParams.height = 500
        }

        tvOp1.text = PollOptions!![0]
        tvOp2.text = PollOptions[1]
        tvOp3.text = PollOptions[2]
        tvOp4.text = PollOptions[3]
        tvPerVote1.text = "$perVote1%"
        tvPerVote2.text = "$perVote2%"
        tvPerVote3.text = "$perVote3%"
        tvPerVote4.text = "$perVote4%"
        tvTotalVotes.text = "Total votes = $sumVotes"


        if(endTimeMilli<=currTimeMilli){
            ivLive.setImageResource(R.drawable.completed)
            tvTimer.text = "POLL RESULTS   "
        }else{
            tvTimer.text = convert_to_timer(endTimeMilli - currTimeMilli)
            object : CountDownTimer(endTimeMilli - currTimeMilli , 1000) {
                override fun onTick(p0: Long) {
                    tvTimer.text = convert_to_timer(p0)
//                    tvTimer.text = remaining.toString()
                }

                override fun onFinish() {
                    ivLive.setImageResource(R.drawable.completed)
                    tvTimer.text = "POLL RESULTS    "
                }

            }.start()
        }




        barChart = findViewById(R.id.BarChart)

        val list: ArrayList<BarEntry> = ArrayList()

        list.add(BarEntry(0f, perVote1))
        list.add(BarEntry(1f, perVote2))
        list.add(BarEntry(2f, perVote3))
        list.add(BarEntry(3f, perVote4))
        

        val barDataSet = BarDataSet(list,"Options")
        barDataSet.setColors(ColorTemplate.MATERIAL_COLORS,255)
        barDataSet.valueTextColor = Color.BLACK
        barDataSet.valueTextSize = 20f

        val labels = ArrayList<String>()
        labels.add("A")
        labels.add("B")
        labels.add("C")
        labels.add("D")

        val barData = BarData(barDataSet)

        barChart.data = barData

        barChart.xAxis.valueFormatter = IndexAxisValueFormatter(labels)
        barChart.xAxis.granularity = 1f
        barChart.xAxis.isGranularityEnabled = true

        barChart.axisLeft.axisMinimum = 0f
        barChart.axisLeft.axisMaximum = 100f
        barChart.axisLeft.granularity = 10f

        barChart.axisRight.axisMinimum = 0f
        barChart.axisRight.axisMaximum = 100f
        barChart.axisRight.granularity = 10f

        barChart.setFitBars(true)

        barChart.description.text = ""

        barChart.animateY(1500)





    }

    private fun convert_to_timer(p0: Long): String {
        val sec = (p0/1000%60).toInt()
        val min = (p0/(1000*60)%60).toInt()
        val hrs = (p0/(1000*60*60)%24).toInt()
        val days = (p0/(1000*60*60*24)).toInt()
        return (days.toString() + String.format(":%02d:%02d:%02d",hrs,min,sec))

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == android.R.id.home)
            onBackPressed()
        return super.onOptionsItemSelected(item)
    }

}