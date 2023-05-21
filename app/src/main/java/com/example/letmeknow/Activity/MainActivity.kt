package com.example.letmeknow.Activity

import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.fragment.app.replace
import com.example.letmeknow.R
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class MainActivity : AppCompatActivity() {

    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


//        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        firebaseAuth = FirebaseAuth.getInstance()

        setContentView(R.layout.activity_main)

        replaceFragment(VotingFragment())

        val bottomNavigationView : BottomNavigationView = findViewById(R.id.BottomNavigationView)
        bottomNavigationView.setOnItemSelectedListener {

            if(it.itemId == R.id.LivePolls){
                replaceFragment(VotingFragment())
            }
            if (it.itemId == R.id.PollResults){
                replaceFragment(PollResultsFragment())
            }
            if (it.itemId == R.id.MyPolls){
                replaceFragment(MyPollsFragment())
            }
            return@setOnItemSelectedListener true
        }

        val poll : FloatingActionButton = findViewById(R.id.CreatePoll)
        poll.setOnClickListener {
            val intent = Intent(this, CreatePollActivity::class.java)
            startActivity(intent)
        }

    }

    private fun replaceFragment(fragment: Fragment) {
        val fragmentTransaction : FragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.frame, fragment)
        fragmentTransaction.commit()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menu!!.add("Log Out")
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.title?.equals("Log Out") == true){
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
        if(item.itemId == android.R.id.home)
            onBackPressed()
        return super.onOptionsItemSelected(item)
    }
}