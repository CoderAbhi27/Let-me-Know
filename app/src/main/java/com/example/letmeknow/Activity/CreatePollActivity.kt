package com.example.letmeknow.Activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*
import androidx.appcompat.widget.SwitchCompat
import com.example.letmeknow.DataClass.PollDataClass
import com.example.letmeknow.R
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class CreatePollActivity : AppCompatActivity() {

    //private lateinit var OptionsRecyclerView: RecyclerView
    private lateinit var OptionsArray : ArrayList<String>
    private lateinit var dbref : DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_poll)

//        OptionsRecyclerView = findViewById(R.id.CreateOptions)
//        OptionsRecyclerView.layoutManager = LinearLayoutManager(this)
//        OptionsRecyclerView.setHasFixedSize(true)

        dbref = FirebaseDatabase.getInstance().getReference("Poll")
        OptionsArray = ArrayList<String>()

//        OptionsArray!!.add("")
//        OptionsArray!!.add("")

//        val itemAdapter = OptionsAdapter(this, OptionsArray!!)
//        OptionsRecyclerView.adapter = itemAdapter

//        val AddOption : LinearLayout = findViewById(R.id.AddOption)
//        AddOption.setOnClickListener {
//            OptionsArray!!.add("")
//            val itemAdapter = OptionsAdapter(this, OptionsArray!!)
//            OptionsRecyclerView.adapter = itemAdapter
//        }

        val CreatePoll : Button = findViewById(R.id.CreatePoll)
        CreatePoll.setOnClickListener{
            val PollQues : EditText = findViewById(R.id.PollQuestion)
            val ques = PollQues.text.toString()
            val MultiSwitch : SwitchCompat = findViewById(R.id.MultiAnsSwitch)
            val QuizSwitch : SwitchCompat = findViewById(R.id.QuizModeSwitch)

            var isMulti: Boolean = false
            if(MultiSwitch.isChecked){
                isMulti = true
            }
            var isQuiz: Boolean = false
            if(QuizSwitch.isChecked){
                isQuiz = true
            }

            getOptionsArray()

            if(OptionsArray.isEmpty()){
                Toast.makeText(this, "Poll Options cannot be empty!", Toast.LENGTH_LONG).show()
            }else if(ques.isEmpty()){
                Toast.makeText(this, "Poll Question is empty!", Toast.LENGTH_LONG).show()

            }else{
                val PollID = dbref.push().key!!
                val PollDataClass = PollDataClass(PollID, ques, isMulti, isQuiz, OptionsArray)
                dbref.child(PollID).setValue(PollDataClass).addOnCompleteListener {
                    if(it.isSuccessful){
                        Toast.makeText(this, "Poll created successfully", Toast.LENGTH_LONG).show()
                        val intent = Intent(this, MainActivity::class.java)
                        startActivity(intent)
                    }else{
                        Toast.makeText(this, it.exception.toString(), Toast.LENGTH_LONG).show()
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