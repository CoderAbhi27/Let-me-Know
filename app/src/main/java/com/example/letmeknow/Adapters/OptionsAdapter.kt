package com.example.letmeknow.Adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.recyclerview.widget.RecyclerView
import com.example.letmeknow.R

class OptionsAdapter(val context: Context, private var OptionsList: ArrayList<String>) :
    RecyclerView.Adapter<OptionsAdapter.ViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val ItemView = LayoutInflater.from(parent.context).inflate(R.layout.create_option, parent, false)
        return ViewHolder(ItemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        OptionsList[position] = holder.etOption.text.toString()
        val currentOption = OptionsList[position]
        holder.etOption.setText(currentOption)
        holder.etOption.hint = "option ${(position + 1)}"
//        holder.etOption.addTextChangedListener(TextWatcher (){
//
//        })
    }

    override fun getItemCount(): Int {
        return OptionsList.size
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view){
        val etOption : EditText = view.findViewById(R.id.Option)
    }

}


