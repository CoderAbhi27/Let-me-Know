package com.example.letmeknow.Activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.letmeknow.Adapters.ViewPollsAdapter
import com.example.letmeknow.Adapters.VotingAdapter
import com.example.letmeknow.DataClass.PollDataClass
import com.example.letmeknow.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class VotingActivity : AppCompatActivity() {

    private lateinit var PollsRecyclerView : RecyclerView
    private lateinit var dbref : DatabaseReference
    private lateinit var firebaseAuth : FirebaseAuth
    private var uidList : ArrayList<String> = ArrayList<String>()
    private var dataList : ArrayList<PollDataClass> = ArrayList<PollDataClass>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_voting)

        PollsRecyclerView = findViewById(R.id.Voting)
        PollsRecyclerView.layoutManager = LinearLayoutManager(this)

//        getUidList()

        firebaseAuth = FirebaseAuth.getInstance()
        val uid = firebaseAuth.currentUser!!.uid
        dbref = FirebaseDatabase.getInstance().getReference("poll")
        dbref.addValueEventListener( object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                dataList.clear()
                if(snapshot.exists()){
                    for (dataSnap in snapshot.children){
                        val data = dataSnap.getValue(PollDataClass::class.java)
                        if(!(uid in data!!.votedUid) && data.endTimeMilli > System.currentTimeMillis())
                            dataList.add(data)
                    }

                    val itemAdapter = VotingAdapter(dataList, this@VotingActivity)
                    PollsRecyclerView.adapter = itemAdapter

                    itemAdapter.setOnItemClickListener(object: VotingAdapter.onItemClickListener{
                        override fun onItemClick(position: Int, voteOn : ArrayList<Int>) {
                            var data = snapshot.children.elementAt(position).getValue(PollDataClass::class.java)
                            val key = snapshot.children.elementAt(position).key
                            for(i in voteOn){
                                data!!.Votes[i]++
                            }
                            data!!.votedUid.add(uid)
                            dbref.child(key!!).setValue(data)

                            Toast.makeText(this@VotingActivity, "Voted successfully!", Toast.LENGTH_SHORT).show()

                        }

                    })





                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@VotingActivity, error.toString(), Toast.LENGTH_LONG).show()
            }

        })







    }

    private fun getUidList() {
        dbref = FirebaseDatabase.getInstance().getReference("uid")
        dbref.addValueEventListener( object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                uidList.clear()
                if(snapshot.exists()){
                    for(dataSnap in snapshot.children){
                        val data = dataSnap.getValue(String::class.java)
                        uidList.add(data!!)
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@VotingActivity, error.toString(), Toast.LENGTH_LONG).show()
            }

        })
    }
}