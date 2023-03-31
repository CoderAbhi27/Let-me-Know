package com.example.letmeknow.Activity

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.letmeknow.Adapters.VotingAdapter
import com.example.letmeknow.DataClass.PollDataClass
import com.example.letmeknow.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class VotingFragment : Fragment(R.layout.fragment_voting) {

    private lateinit var PollsRecyclerView : RecyclerView
    private lateinit var dbref : DatabaseReference
    private lateinit var firebaseAuth : FirebaseAuth
    private var uidList : ArrayList<String> = ArrayList<String>()
    private var dataList : ArrayList<PollDataClass> = ArrayList<PollDataClass>()
    private lateinit var contexts : Context

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_voting)



    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        contexts = container?.context!!
        return inflater.inflate(R.layout.fragment_voting, container, false)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        PollsRecyclerView = view.findViewById(R.id.Voting)
        PollsRecyclerView.layoutManager = LinearLayoutManager(context)
        PollsRecyclerView.setHasFixedSize(true)

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

                    val itemAdapter = VotingAdapter(dataList, contexts)
                    PollsRecyclerView.adapter = itemAdapter

                    itemAdapter.setOnItemClickListener(object: VotingAdapter.onItemClickListener{
                        override fun onItemClick(position: Int, voteOn : ArrayList<Int>, key : String) {
                            val data = snapshot.child(key).getValue(PollDataClass::class.java)
                            for(i in voteOn){
                                data!!.Votes[i]++
                            }
                            data!!.votedUid.add(uid)
                            dbref.child(key).setValue(data)

                            Toast.makeText(activity, "Voted Successfully!", Toast.LENGTH_SHORT).show()

                        }

                    })





                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(activity, error.toString(), Toast.LENGTH_LONG).show()
            }

        })

    }


    /*private fun getUidList() {
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
                Toast.makeText(activity, error.toString(), Toast.LENGTH_LONG).show()
            }

        })
    }*/
}