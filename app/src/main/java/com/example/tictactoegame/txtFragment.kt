//package com.example.tictactoegame
//
//import androidx.fragment.app.Fragment
//
//class ThreexthreeFragment: Fragment(R.layout.threexthreefragment) {
//
//
//}


package com.example.tictactoegame

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.example.tictactoegame.databinding.FragmentTxtBinding
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.text.DateFormat
import java.util.Calendar

class txtFragment : Fragment() {

    enum class Turn {
        NOUGHT,
        CROSS
    }

    private var firstTurn = Turn.CROSS
    private var currentTurn = Turn.CROSS

//    private var crossScore = 0
//    private var noughtScore = 0

    private var boardList = mutableListOf<Button>()
    private lateinit var binding: FragmentTxtBinding

//    Store Replay to Firebase

    private lateinit var database: DatabaseReference

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // ผูกกับเลย์เอาท์ `FragmentGameBinding`
        binding = FragmentTxtBinding.inflate(inflater, container, false)
        initBoard()



        // ผูก boardTrapped กับคลิกปุ่ม
        binding.a1.setOnClickListener {
            boardTrapped(it)
        }
        binding.a2.setOnClickListener {
            boardTrapped(it)
        }
        binding.a3.setOnClickListener {
            boardTrapped(it)
        }
        binding.b1.setOnClickListener {
            boardTrapped(it)
        }
        binding.b2.setOnClickListener {
            boardTrapped(it)
        }
        binding.b3.setOnClickListener {
            boardTrapped(it)
        }
        binding.c1.setOnClickListener {
            boardTrapped(it)
        }
        binding.c2.setOnClickListener {
            boardTrapped(it)
        }
        binding.c3.setOnClickListener {
            boardTrapped(it)
        }

        return binding.root
    }

    private fun initBoard() {
        boardList.add(binding.a1)
        boardList.add(binding.a2)
        boardList.add(binding.a3)
        boardList.add(binding.b1)
        boardList.add(binding.b2)
        boardList.add(binding.b3)
        boardList.add(binding.c1)
        boardList.add(binding.c2)
        boardList.add(binding.c3)
    }

    fun boardTrapped(view: View) {

        val calendar = Calendar.getInstance().time
        val dateFormat = DateFormat.getDateInstance(DateFormat.FULL).format(calendar)
        val timeFormat = DateFormat.getTimeInstance().format(calendar)

        if (view !is Button) {
            return
        }
        addToBoard(view)

        if (checkForVictory(NOUGHT)) {
//            noughtScore++

            val name = "Nought"
            val noughtResult = "Win"
            val crossResult = "Lose"
            val date = dateFormat.toString()
            val time = timeFormat.toString()
            val layout = "3X3"

            database = FirebaseDatabase.getInstance().getReference("ReplayResult")
            val replayResult = Replay(name,noughtResult,crossResult,date,time,layout)

            database.child("$date $time").setValue(replayResult).addOnSuccessListener {
                Toast.makeText(this.context,"Nought Win!!",Toast.LENGTH_SHORT).show()
                result("Noughts Win!")
            }.addOnFailureListener {
                Toast.makeText(this.context,"Fail to insert Data to Database",Toast.LENGTH_SHORT).show()
            }


        }
        if (checkForVictory(CROSS)) {
//            crossScore++

            val name = "CROSS"
            val noughtResult = "Lose"
            val crossResult = "Win"
            val date = dateFormat.toString()
            val time = timeFormat.toString()
            val layout = "3X3"

            database = FirebaseDatabase.getInstance().getReference("ReplayResult")
            val replayResult = Replay(name,noughtResult,crossResult,date,time,layout)

            database.child("$date $time").setValue(replayResult).addOnSuccessListener {
                Toast.makeText(this.context,"Crosses Win!!",Toast.LENGTH_SHORT).show()
                result("Crosses Win!")
            }.addOnFailureListener {
                Toast.makeText(this.context,"Fail to insert Data to Database",Toast.LENGTH_SHORT).show()
            }



        }

        if (fullBoard()) {

            val name = "Draw"
            val noughtResult = "Draw"
            val crossResult = "Draw"
            val date = dateFormat.toString()
            val time = timeFormat.toString()
            val layout = "3X3"

            database = FirebaseDatabase.getInstance().getReference("ReplayResult")
            val replayResult = Replay(name,noughtResult,crossResult,date,time,layout)

            database.child("$date $time").setValue(replayResult).addOnSuccessListener {
                Toast.makeText(this.context,"Draw !!",Toast.LENGTH_SHORT).show()
                result("Draw")
            }.addOnFailureListener {
                Toast.makeText(this.context,"Fail to insert Data to Database",Toast.LENGTH_SHORT).show()
            }


        }
    }

    private fun checkForVictory(s: String): Boolean {
        // ชนะ แบบแนวนอน
        if (match(binding.a1, s) && match(binding.a2, s) && match(binding.a3, s)) {
            return true
        }
        if (match(binding.b1, s) && match(binding.b2, s) && match(binding.b3, s)) {
            return true
        }
        if (match(binding.c1, s) && match(binding.c2, s) && match(binding.c3, s)) {
            return true

        }
        // ชนะ แบบแนวตั้ง
        if (match(binding.a1, s) && match(binding.b1, s) && match(binding.c1, s)) {
            return true
        }
        if (match(binding.a2, s) && match(binding.b2, s) && match(binding.c2, s)) {
            return true
        }
        if (match(binding.a3, s) && match(binding.b3, s) && match(binding.c3, s)) {
            return true

        }
        // ชนะแบบเฉียง
        if (match(binding.a1, s) && match(binding.b2, s) && match(binding.c3, s)) {
            return true
        }
        if (match(binding.a3, s) && match(binding.b2, s) && match(binding.c1, s)) {
            return true
        }

        return false
    }

    private fun match(button: Button, symbol: String): Boolean = button.text == symbol

    private fun result(title: String) {
        val message = "Congratulation!!"
        AlertDialog.Builder(requireContext())
            .setTitle(title)
            .setMessage(message)
            .setPositiveButton("Reset") { _, _ ->
                resetBoard()
            }
            .setCancelable(false)
            .show()
    }

    private fun resetBoard() {
        for (button in boardList) {
            button.text = ""
        }

        if (firstTurn == Turn.NOUGHT) {
            firstTurn = Turn.CROSS
        } else if (firstTurn == Turn.CROSS) {
            firstTurn = Turn.NOUGHT
        }

        currentTurn = firstTurn
        setTurnLabel()
    }

    private fun fullBoard(): Boolean {
        for (button in boardList) {
            if (button.text == "") {
                return false
            }
        }
        return true
    }

    private fun addToBoard(button: Button) {
        if (button.text != "") {
            return
        }
        if (currentTurn == Turn.NOUGHT) {
            button.text = NOUGHT
//            Set Text Color
            button.setTextColor(Color.RED)
            currentTurn = Turn.CROSS
        } else if (currentTurn == Turn.CROSS) {
            //            Set Text Color
            button.setTextColor(Color.BLUE)
            button.text = CROSS
            currentTurn = Turn.NOUGHT
        }
        setTurnLabel()
    }

    private fun setTurnLabel() {
        var turnText = ""
        if (currentTurn == Turn.CROSS) {
            turnText = "Turn $CROSS"

        } else if (currentTurn == Turn.NOUGHT) {
            turnText = "Turn $NOUGHT"
        }

        binding.turnTextTV.text = turnText
    }

    companion object {
        const val NOUGHT = "O"
        const val CROSS = "X"
    }




}
