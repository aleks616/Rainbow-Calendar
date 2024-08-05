package com.example.rainbowcalendar

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.RadioButton
import android.widget.Spinner
import android.widget.TextView

class IntroductionActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_introduction)

        //reg: themes
        val spinner=findViewById<Spinner>(R.id.themeSpinner)
        ArrayAdapter.createFromResource(this,R.array.themes_array,android.R.layout.simple_spinner_item)
            .also {adapter->
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                spinner.adapter=adapter
            }

        spinner.onItemSelectedListener=object:
            AdapterView.OnItemSelectedListener{
            override fun onItemSelected(parent:AdapterView<*>?,view:View?,position:Int,id:Long){
                val value=parent?.getItemAtPosition(position).toString()
            }
            override fun onNothingSelected(parent:AdapterView<*>?){}
        }
        //TODO: do themes


        //val sharedpref: SharedPreferences =applicationContext.getSharedPreferences("com.example.rainbowcalendar", MODE_PRIVATE)
        //val token: String?=sharedpref.getString("token", null)

        val sharedPrefGender = applicationContext.getSharedPreferences("com.example.rainbowcalendar_gender", Context.MODE_PRIVATE) ?: return
        var gender: String? =sharedPrefGender.getString("com.example.rainbowcalendar_gender","")
        val sharedPrefName = applicationContext.getSharedPreferences("com.example.rainbowcalendar_name", Context.MODE_PRIVATE)
        val sharedPrefTM=applicationContext.getSharedPreferences("com.example.rainbowcalendar_tm", Context.MODE_PRIVATE)
        var tM=false
        //var name: String?=sharedPrefName.getString("com.example.rainbowcalendar_name", "")
        val genderM=findViewById<RadioButton>(R.id.genderMale)
        val genderF=findViewById<RadioButton>(R.id.genderFemale)
        val genderN=findViewById<RadioButton>(R.id.genderNeutral)

        val nameText=findViewById<TextView>(R.id.nameET)
        nameText.setOnEditorActionListener{ v, actionId, _ ->
            if(actionId==EditorInfo.IME_ACTION_DONE){
                if(v.text.contains("tRvEt",ignoreCase = false)){
                    tM=true
                    genderM.text=getString(R.string.gender_mode_m)
                }
                else{
                    genderM.text=getString(R.string.gender_mode_tm)
                }
                true
            }
            else
                false
        }

        val button=findViewById<Button>(R.id.buttonNext)
        button.setOnClickListener{
            val errorText=findViewById<TextView>(R.id.errorText)
            //val nameText=findViewById<TextView>(R.id.nameET)
            var name=nameText.text.toString()
            if(name.isEmpty()){
                errorText.text=getString(R.string.fill_name)
            }
            else{
                if(name.contains("tRvEt",ignoreCase = false)){
                    name=name.replace("tRvEt","")
                }
                with(sharedPrefTM.edit()){
                    putBoolean("com.example.rainbowcalendar_tm", tM)
                    apply()
                } //doc: if transmed, it saves it
                with (sharedPrefName.edit()) {
                    putString("com.example.rainbowcalendar_name", name)
                    apply()
                }
                errorText.text=""
            }

            if(genderM.isChecked) gender="m"
            else if (genderF.isChecked) gender="f"
            else if (genderN.isChecked) gender="n"
            else errorText.text=getString(R.string.select_one_option)
            if(gender!=null){
                //val sharedPrefGender: SharedPreferences =applicationContext.getSharedPreferences("com.example.rainbowcalendar_gender", MODE_PRIVATE)
                with (sharedPrefGender.edit()) {
                    putString("com.example.rainbowcalendar_gender", gender)
                    apply()
                }
                //val genderRead: String? =sharedPrefGender.getString("com.example.rainbowcalendar_gender","")
                //Toast.makeText(this@IntroductionActivity, "g1: $genderRead", Toast.LENGTH_SHORT).show()

            }
            if(name.isNotEmpty()&&(!gender.isNullOrEmpty())){
                errorText.text=""
                startActivity(Intent(this, IntroductionActivity2::class.java))
                //Toast.makeText(this@IntroductionActivity, name, Toast.LENGTH_SHORT).show()
            }

        }


    }
}

