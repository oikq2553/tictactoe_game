# Tic Tac Toe 
* [Link How to Setup](#how-to-setup)
* [How to Start Program](#how-to-start-program)
* [Algorithm and Design](#algorithm-and-design)

## How to setup

1. Go to https://github.com/oikq2553/tictactoe_game.git


2. Click on Code button and Clone Project

![image](https://github.com/oikq2553/tictactoe_game/assets/150600969/b82bba29-8c30-4b7f-afaf-9b2dbbd663a0)

3. Go to Android Studio Menubar

    File -> New -> Project from Version Control

![image-1](https://github.com/oikq2553/tictactoe_game/assets/150600969/b7cfa24b-bc82-486c-8edb-c77451fc4892)

4. Press the URL form Github and Select Directory

![image-3](https://github.com/oikq2553/tictactoe_game/assets/150600969/501aab90-e3c3-4665-a568-4c48851d2517)

5. Clone project successful 

![image-4](https://github.com/oikq2553/tictactoe_game/assets/150600969/689f2396-b35c-4c19-9ff5-3f5b2b21b39e)

## How to Start Program

1. Once you have imported the project from GitHub you can run the program or (2)

2. Install APK file from 
    https://drive.google.com/drive/folders/176G8bUI6JtuTaRA26j0IPhSw_tGgYgyV?usp=sharing

## Algorithm and Design

1. Design Layout

    เริ่มจากการ Design Layout ของกระดานเกมและตำแหน่งของปุ่มก่อนโดย
        
        - แถว(row) เราจะกำหนดให้เป็น a b c ...
        - คอลัม(column) เราจะกำหนดให้เป็น 1 2 3 ...
    ดังภาพประกอบข้างล่าง

    ![image-6](https://github.com/oikq2553/tictactoe_game/assets/150600969/7c1fdaa8-d413-49fc-ad28-9a5e4526af70)
    
    จากภาพจะเป็นตัวอย่างของ Layout ที่เป็น 3x3


2. การจัดวางองค์ประกอบของ MainActivity

    ผมจะใช้ Fragment เข้ามาช่วยในการแสดงกระดานเกมและใช้ Dropdown หรือ Spinner ในการเลือก Layout ของกระดานเกม ส่วนตัวใช้วิธีนี้เพราะว่าจะได้ไม่ต้องไปสร้าง Activity หลายตัว จึงเลือกใช้ Fragment ช่วยแสดงกระดานเกม

    ![image-8](https://github.com/oikq2553/tictactoe_game/assets/150600969/4e26ee53-fc91-4874-8d9a-de702e8d7092)


3. ส่วนของ Replay

    ผมใช้ MVC Pattern โดยการใช้ RecyclerView เข้ามาช่วยในการแสดงประวัติการแข่งขันโดยการดึงข้อมูลจาก RealtimeDatabase ของ Firebase

    ![image-9](https://github.com/oikq2553/tictactoe_game/assets/150600969/b9e7d622-4840-4d47-aae1-ee169eee25ef)


4. ส่วนของ Database

    ในการเก็บประวัติการแข่งขันผมเลือกที่จะใช้ Date + Time เป็น Child โดย

        name -> ผู้ชนะ (Cross, Nought, Draw)
        crossResult -> ผลแพ้ชนะของ X
        noughtResult -> ผลแพ้ชนะของ O
        layout -> ชนิดของ Layout (3X3, 3X4, 4X4)
        date -> วันที่
        time -> เวลา



    ![image-10](https://github.com/oikq2553/tictactoe_game/assets/150600969/c6030658-1c5b-4114-81a9-aef998dbbfa5)


5. Algorithm

    ในส่วนของ Algorithm ของเกม

    - เริ่มจากการกำหนด Turn ของผู้เล่นโดย Turn แรกจะเป็น Turn ของ Cross หรือ X 

        ~~~ Kotlin
        enum class Turn {
            NOUGHT,
            CROSS
            }

        private var firstTurn = Turn.CROSS
        private var currentTurn = Turn.CROSS
        ~~~

    - เมื่อกดปุ่มก็จะเรียกใช้ Fuction boardTrapped() โดยเช็คก่อนว่า View เป็นปุ่มมั้ย ถ้าไม่เป็นจะหยุดการทำงาน ถ้าเป็นจะเรียกใช้ Function addToBoard() 

        ~~~ Kotlin
        fun boardTrapped(view: View) {

        if (view !is Button) {
            return
        }
        addToBoard(view)
        ~~~
    
    - Function addToBoard() เป็นฟังก์ชันที่ใช้ในการวาดตัวอีกษร X หรือ O โดยเช็คจาก Turn ของผู้เล่นว่าผู้เล่นอยู่ Turn ไหน ส่วน Fuction setTurnLabel() เป็นการตั้งค่า TextLabel ว่าเป็น Turn ไหน

        ~~~ Kotlin
        private fun addToBoard(button: Button) {
            if (button.text != "") {
                return
            }
            if (currentTurn == Turn.NOUGHT) {
                button.text = NOUGHT
                button.setTextColor(Color.RED)
                currentTurn = Turn.CROSS
            } else if (currentTurn == Turn.CROSS) {
                button.setTextColor(Color.BLUE)
                button.text = CROSS
                currentTurn = Turn.NOUGHT
            }
            setTurnLabel()
        }
        ~~~

    - กลับมาที่ Fuction boardTrapped()  เมื่อผู้ใช้กดปุ่มบนกระดานก็จะมีการเชคโดยการเรียกใช้ Fuction checkForVictory() (จากโค้ดด้านล่างคือในกรณี O ชนะก็จะ Insert ข้อมูลไปที่ Database) และเรียกใช้ Fuction result() เพื่อ Popup แสดงว่าใครชนะ

        ~~~Kotlin
            if (checkForVictory(NOUGHT)) {

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
        ~~~

    - ส่วนของ Fuction checkForVictory() จะเป็นการเช็คว่า s (ตัวอักษรในปุ่ม) เป้นตัวเดียวกันเรียงกันครบ 3 ตัวหรือไม่ถ้าตรงตามเงื่อนไขก็ Return ค่ากลับว่าชนะ (จากตัวอย่างโค้ดด้านล่างเป็นเงื่อนไขการชนะแบบแนวนอนของ Layout 3X3) 

        ~~~Kotlin
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
        ~~~
