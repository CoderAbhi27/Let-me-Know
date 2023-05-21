package com.example.letmeknow.Activity

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
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

class MyPollsFragment : Fragment(R.layout.fragment_my_polls) {


    private lateinit var PollsRecyclerView : RecyclerView
    private var dataList : ArrayList<PollDataClass> = ArrayList<PollDataClass>()
    private lateinit var dbref : DatabaseReference
    private lateinit var firebaseAuth : FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_my_polls, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        PollsRecyclerView = view.findViewById(R.id.ViewPolls)
        PollsRecyclerView.layoutManager = LinearLayoutManager(context)
        PollsRecyclerView.setHasFixedSize(true)


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
                            val intent = Intent(activity,BarChartActivity::class.java)
                            intent.putExtra("PollQues", dataList[position].PollQues)
                            intent.putExtra("QuesImg", dataList[position].QuesImg)
                            intent.putExtra("PollOptions", dataList[position].Options)
                            intent.putExtra("Votes", dataList[position].Votes)
                            intent.putExtra("endTime", dataList[position].endTimeMilli)
                            startActivity(intent)

                        }

                    })

                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(activity, error.toString(), Toast.LENGTH_LONG).show()

            }

        })

    }
}