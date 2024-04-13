package com.example.tictactoegame

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tictactoegame.databinding.ActivityMainBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding

    private lateinit var databasereference : DatabaseReference
    private lateinit var replayRecyclerView: RecyclerView
    private lateinit var replayArrayList : ArrayList<Replay>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
//            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
//            insets
//        }

        replayRecyclerView = findViewById(R.id.replayRecyclerview)
        replayRecyclerView.layoutManager = LinearLayoutManager(this)
        replayRecyclerView.setHasFixedSize(true)

        replayArrayList = arrayListOf<Replay>()
        getReplayData()





        // ตั้งค่า Spinner
        val spinner: Spinner = binding.layoutSpinner
        val items = listOf("3X3", "3X4", "4X4")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, items)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter

        // คุณสามารถตั้งค่า listener ให้กับ Spinner เพื่อรับค่าที่เลือกได้
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val selectedItem = parent?.getItemAtPosition(position)
//                Toast.makeText(this@MainActivity,selectedItem.toString(), Toast.LENGTH_SHORT).show()


                if (selectedItem.toString().equals("3X3")){
                    replaceFragment(txtFragment())
                }
                else if (selectedItem.toString().equals("3X4")){
                    replaceFragment(TreexfourFragment())
                }
                else if (selectedItem.toString().equals("4X4")){
                    replaceFragment(fxfFragment())
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // ทำงานเมื่อไม่มีค่าที่เลือก
            }
        }








    }

    private fun getReplayData() {
        databasereference = FirebaseDatabase.getInstance().getReference("ReplayResult")
        databasereference.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()){
                    for (replaySnapshot in snapshot.children){
                        val replay = replaySnapshot.getValue(Replay::class.java)
                        replayArrayList.add(replay!!)
                    }

                    replayRecyclerView.adapter = ReplayAdapter(replayArrayList)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }

    private fun replaceFragment(fragment: Fragment) {
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.fragmentContainer,fragment)
        fragmentTransaction.commit()
    }




}