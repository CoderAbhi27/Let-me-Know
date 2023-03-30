package com.example.letmeknow.Activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.letmeknow.DataClass.PollDataClass
import com.example.letmeknow.R
import com.example.letmeknow.Adapters.ViewPollsAdapter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class ViewPollsActivity : AppCompatActivity() {


    private lateinit var PollsRecyclerView : RecyclerView
    private var dataList : ArrayList<PollDataClass> = ArrayList<PollDataClass>()
    private lateinit var dbref : DatabaseReference
    private lateinit var firebaseAuth : FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_polls)

        PollsRecyclerView = findViewById(R.id.ViewPolls)
        PollsRecyclerView.layoutManager = LinearLayoutManager(this)

        firebaseAuth = FirebaseAuth.getInstance()
        val uid = firebaseAuth.currentUser!!.uid

        dbref = FirebaseDatabase.getInstance().getReference("poll")
        dbref.addValueEventListener( object: ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                dataList.clear()
                if(snapshot.exists()){
                    for(dataSnap in snapshot.children){
                        val data = dataSnap.getValue(PollDataClass::class.java)
                        if(uid==data!!.uid)
                            dataList.add(data)
                    }

                    val itemAdapter = ViewPollsAdapter(dataList)
                    PollsRecyclerView.adapter = itemAdapter

                    itemAdapter.setOnItemClickListener(object: ViewPollsAdapter.onItemClickListener{
                        override fun onItemClick(position: Int) {
                            val intent = Intent(this@ViewPollsActivity,BarChartActivity::class.java)
                            intent.putExtra("PollQues", dataList[position].PollQues)
//                            intent.putExtra("QuesImg", dataList[position].QuesImg)
                            intent.putExtra("PollOptions", dataList[position].Options)
                            intent.putExtra("Votes", dataList[position].Votes)
                            intent.putExtra("endTime", dataList[position].endTimeMilli)
                            startActivity(intent)

                        }

                    })

                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@ViewPollsActivity, error.toString(), Toast.LENGTH_LONG)

            }

        })

    }
}