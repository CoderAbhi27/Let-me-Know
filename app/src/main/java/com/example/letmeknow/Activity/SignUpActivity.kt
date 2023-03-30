package com.example.letmeknow.Activity

import android.app.ProgressDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.content.Intent
import android.widget.Toast
import com.example.letmeknow.databinding.ActivitySignUpBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class SignUpActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignUpBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var dbref : DatabaseReference
    private lateinit var pd : ProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()


        binding.textView.setOnClickListener {
            val intent = Intent(this, SignInActivity::class.java)
            startActivity(intent)
        }
        binding.appCompatButton.setOnClickListener {
            pd = ProgressDialog(this)
            pd.setMessage("Creating your account...")
            pd.show()
            val email = binding.emailEt.text.toString()
            val pass = binding.passwordEt.text.toString()
            val cpass = binding.ConfirmPasswordEt.text.toString()

            if (email.isNotEmpty() && pass.isNotEmpty() && cpass.isNotEmpty()) {
                if (cpass == pass) {
                    firebaseAuth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener {
                        if (it.isSuccessful) {
                            val uid = firebaseAuth.currentUser!!.uid
                            dbref = FirebaseDatabase.getInstance().getReference("uid")
                            val key = dbref.push().key!!
                            dbref.child(key).setValue(uid)
                            pd.dismiss()
                            val intent = Intent(this, SignInActivity::class.java)
                            startActivity(intent)
                        } else {
                            pd.dismiss()
                            Toast.makeText(this, it.exception.toString(), Toast.LENGTH_LONG).show()
                        }
                    }
                } else {
                    pd.dismiss()
                    Toast.makeText(this, "Passwords does not match", Toast.LENGTH_SHORT).show()
                }
            } else {
                pd.dismiss()
                Toast.makeText(this, "please fill all the fields", Toast.LENGTH_SHORT).show()
            }
        }
    }
}