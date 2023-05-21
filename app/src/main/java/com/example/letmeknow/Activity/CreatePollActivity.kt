package com.example.letmeknow.Activity

import android.app.ProgressDialog
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.widget.*
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.SwitchCompat
import com.example.letmeknow.DataClass.PollDataClass
import com.example.letmeknow.R
import com.google.android.gms.tasks.OnSuccessListener
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.time.ZoneOffset
import java.util.Date

class CreatePollActivity : AppCompatActivity() {

    //private lateinit var OptionsRecyclerView: RecyclerView
    private var quesImg : Uri?= null
    private lateinit var OptionsArray : ArrayList<String>
    private lateinit var Votes : ArrayList<Int>
    private lateinit var dbref : DatabaseReference
    private lateinit var storageRef : StorageReference
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var pd : ProgressDialog

    private val selectImage = registerForActivityResult(ActivityResultContracts.GetContent()){

        quesImg = it
        val ImgView : ImageView = findViewById(R.id.ImageQues)
        ImgView.setImageURI(it)
        ImgView.layoutParams.height = 200
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_poll)

//        supportActionBar?.setDisplayShowHomeEnabled(true)
//        supportActionBar?.setDisplayHomeAsUpEnabled(true)
//        supportActionBar?.setTitle("Create Poll")
//        supportActionBar?.show()

        val InsertImage : ImageButton = findViewById(R.id.InsertImageQues)
        InsertImage.setOnClickListener{
            selectImage.launch("image/*")
        }


        val CreatePoll : Button = findViewById(R.id.CreatePoll)
        CreatePoll.setOnClickListener{
            val builder = AlertDialog.Builder(this)
            builder.setTitle("Create this poll?")
            builder.setPositiveButton("OK", DialogInterface.OnClickListener{ dialog, which->
                createPoll()
            })
            builder.setNegativeButton("Cancel", DialogInterface.OnClickListener{ dialog, which-> })
            val alertDialog: AlertDialog = builder.create()
            alertDialog.show()


        }

    }

    private fun createPoll(){
        pd = ProgressDialog(this)
        pd.setMessage("Creating Poll...")
        pd.show()

        firebaseAuth = FirebaseAuth.getInstance()
        val uid = firebaseAuth.currentUser!!.uid
        dbref = FirebaseDatabase.getInstance().getReference("poll")
        storageRef = FirebaseStorage.getInstance().getReference()


        OptionsArray = ArrayList<String>()

        Votes = ArrayList<Int>()
        for(i in 1..4){
            Votes.add(0)
        }


        val etPollQues : EditText = findViewById(R.id.PollQuestion)
        val MultiSwitch : SwitchCompat = findViewById(R.id.MultiAnsSwitch)
        val etDays : EditText = findViewById(R.id.days)
        val etHrs : EditText = findViewById(R.id.hrs)
        val etMins : EditText = findViewById(R.id.mins)

        val ques = etPollQues.text.toString()

        var days: Int = 0
        var hrs: Int = 0
        var mins: Int = 0
        if(etDays.text.isNotEmpty()){
            days = etDays.text.toString().toInt()
        }
        if(etHrs.text.isNotEmpty()){
            hrs = etHrs.text.toString().toInt()
        }
        if(etMins.text.isNotEmpty()){
            mins = etMins.text.toString().toInt()
        }

        var isMulti: Boolean = false

        if(MultiSwitch.isChecked){
            isMulti = true
        }


        getOptionsArray()



        if(OptionsArray.isEmpty()){
            pd.dismiss()
            Toast.makeText(this, "Poll Options cannot be empty!", Toast.LENGTH_LONG).show()
        }else if(ques.isEmpty() && quesImg==null){
            pd.dismiss()
            Toast.makeText(this, "Poll Question is empty!", Toast.LENGTH_LONG).show()
        }else if(hrs>=24 || mins>=60){
            pd.dismiss()
            Toast.makeText(this, "Please enter correct Duration format", Toast.LENGTH_LONG).show()
        }else{
            val endTimeMilli = System.currentTimeMillis() + (days*24*60 + hrs*60 + mins)*60*1000L

            val pollID = dbref.push().key!!

            if (quesImg==null){
                val pollDataClass = PollDataClass(pollID, uid, ques, null, isMulti, OptionsArray, Votes, endTimeMilli, ArrayList<String>())
                dbref.child(pollID).setValue(pollDataClass).addOnCompleteListener {
                    if(it.isSuccessful){
                        Toast.makeText(this, "Poll created successfully", Toast.LENGTH_LONG).show()
                        pd.dismiss()
                        val intent = Intent(this, MainActivity::class.java)
                        startActivity(intent)
                    }else{
                        Toast.makeText(this, it.exception.toString(), Toast.LENGTH_LONG).show()
                        pd.dismiss()
                    }
                }
            }else{
                storageRef.child(pollID).putFile(quesImg!!).addOnCompleteListener {
                    if(it.isSuccessful){
                        storageRef.child(pollID).downloadUrl.addOnSuccessListener {
                            val pollDataClass = PollDataClass(pollID, uid, ques, it.toString(), isMulti, OptionsArray, Votes, endTimeMilli, ArrayList<String>())
                            dbref.child(pollID).setValue(pollDataClass).addOnCompleteListener {
                                if(it.isSuccessful){
                                    Toast.makeText(this, "Poll created successfully", Toast.LENGTH_LONG).show()
                                    pd.dismiss()
                                    val intent = Intent(this, MainActivity::class.java)
                                    startActivity(intent)
                                }else{
                                    Toast.makeText(this, it.exception.toString(), Toast.LENGTH_LONG).show()
                                    pd.dismiss()
                                }
                            }
                        }

                    }
                }
            }

        }

    }


    private fun getOptionsArray(){
        val etOpt1 : EditText = findViewById(R.id.Opt1)
        val etOpt2 : EditText = findViewById(R.id.Opt2)
        val etOpt3 : EditText = findViewById(R.id.Opt3)
        val etOpt4 : EditText = findViewById(R.id.Opt4)

        val Opt1 = etOpt1.text.toString()
        val Opt2 = etOpt2.text.toString()
        val Opt3 = etOpt3.text.toString()
        val Opt4 = etOpt4.text.toString()

        if(Opt1.isNotEmpty() && Opt2.isNotEmpty() && Opt3.isNotEmpty() && Opt4.isNotEmpty()){
            OptionsArray.add(Opt1)
            OptionsArray.add(Opt2)
            OptionsArray.add(Opt3)
            OptionsArray.add(Opt4)
        }


    }
}