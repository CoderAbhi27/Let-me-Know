package com.example.letmeknow

import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AlertDialog
import com.example.letmeknow.databinding.ActivitySignInBinding
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        firebaseAuth = FirebaseAuth.getInstance()
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

    }
}