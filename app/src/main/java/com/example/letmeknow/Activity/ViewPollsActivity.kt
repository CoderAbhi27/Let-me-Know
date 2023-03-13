package com.example.letmeknow.Activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.letmeknow.DataClass.PollDataClass
import com.example.letmeknow.R
import com.example.letmeknow.Adapters.ViewPollsAdapter
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class ViewPollsActivity : AppCompatActivity() {


    private lateinit var PollsRecyclerView : RecyclerView
    private lateinit var DataList : ArrayList<PollDataClass>
    private lateinit var dbref : DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_polls)

        PollsRecyclerView = findViewById(R.id.ViewPolls)
        PollsRecyclerView.layoutManager = LinearLayoutManager(this)

        DataList = ArrayList<PollDataClass>()
        getPollData()


    }

    private fun getPollData() {
        dbref = FirebaseDatabase.getInstance().getReference("Poll")
        dbref.addValueEventListener( object: ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                DataList.clear()
                if(snapshot.exists()){
                    for(dataSnap in snapshot.children){
                        val data = dataSnap.getValue(PollDataClass::class.java)
                        DataList.add(data!!)
                    }
                    val itemAdapter = ViewPollsAdapter(DataList)
                    PollsRecyclerView.adapter = itemAdapter
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })

    }
}