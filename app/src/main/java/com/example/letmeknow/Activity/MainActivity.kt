package com.example.letmeknow.Activity

import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AlertDialog
import com.example.letmeknow.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class MainActivity : AppCompatActivity() {
    private lateinit var firebaseAuth: FirebaseAuth



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        firebaseAuth = FirebaseAuth.getInstance()
        val firebase: DatabaseReference = FirebaseDatabase.getInstance().getReference()

        setContentView(R.layout.activity_main)
        val logout: Button = findViewById(R.id.logOut)
        logout.setOnClickListener{
            val builder = AlertDialog.Builder(this)
            builder.setTitle("LOG OUT")
            builder.setMessage("Are you sure?")
            builder.setPositiveButton("LOG OUT", DialogInterface.OnClickListener{dialog, which->
                firebaseAuth.signOut()
                val intent = Intent(this, SignInActivity::class.java)
                startActivity(intent)
            })
            builder.setNegativeButton("Cancel", DialogInterface.OnClickListener{dialog, which-> })
            val alertDialog: AlertDialog = builder.create()
            alertDialog.show()
        }

        val poll : Button = findViewById(R.id.CreatePoll)
        poll.setOnClickListener {
            val intent = Intent(this, CreatePollActivity::class.java)
            startActivity(intent)
        }

        val viewPoll : Button = findViewById(R.id.ViewPolls)
        viewPoll.setOnClickListener {
            val intent = Intent(this, ViewPollsActivity::class.java)
            startActivity(intent)
        }

        val voteOnPolls : Button = findViewById(R.id.VoteOnPolls)
        voteOnPolls.setOnClickListener{
            val intent = Intent(this, VotingActivity::class.java)
            startActivity(intent)
        }

    }
}