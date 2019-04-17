package com.wilkersonmiddle.wildcatsagainstbullying

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.wilkersonmiddle.wildcatsagainstbullying.classes.Question
import kotlinx.android.synthetic.main.activity_quiz.*
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class QuizActivity : AppCompatActivity() {

    companion object {

        var questionArray = emptyArray<Question>()

        /*used to get current month in listenForQuestions; first place is null so that
        1 corresponds with jan, 2 with feb, so on*/
        val months = arrayOf("null", "january", "february", "march", "april", "may", "june", "july", "august", "september", "october", "november", "december")

        var questionNumber = 1

        var questionsCorrect = 0

        var totalQuestions = 0

        var submittedYet = false

        var doneCollecting = false

        var isSomethingChecked = false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quiz)

        supportActionBar?.title = "Quiz"

        resetVariables()
        listenForQuestions()

        btn_quiz_1.setOnClickListener {
            btn_quiz_submitnext.setBackgroundResource(R.drawable.roundedbuttonblue)
            isSomethingChecked = true

            //disable the buttons
            btn_quiz_1.isEnabled = false
            btn_quiz_2.isEnabled = false
            btn_quiz_3.isEnabled = false
            btn_quiz_4.isEnabled = false

            //if option 1 is selected, but is wrong
            if (btn_quiz_1.text != questionArray[questionNumber - 1].correctAnswer){
                highlightCorrectAnswer()
                btn_quiz_1.setBackgroundResource(R.drawable.roundedbuttonred)
                btn_quiz_submitnext.text = "NEXT"
            }
            //if option 1 is selected, and is correct
            else if (btn_quiz_1.text == questionArray[questionNumber - 1].correctAnswer){
                questionsCorrect++
                btn_quiz_1.setBackgroundResource(R.drawable.roundedbuttongreen)
                btn_quiz_submitnext.text = "NEXT"
            }
        }

        btn_quiz_2.setOnClickListener {
            btn_quiz_submitnext.setBackgroundResource(R.drawable.roundedbuttonblue)
            isSomethingChecked = true

            //disable the buttons
            btn_quiz_1.isEnabled = false
            btn_quiz_2.isEnabled = false
            btn_quiz_3.isEnabled = false
            btn_quiz_4.isEnabled = false

            //if option 2 is selected, but is wrong
            if (btn_quiz_2.text != questionArray[questionNumber - 1].correctAnswer){
                highlightCorrectAnswer()
                btn_quiz_2.setBackgroundResource(R.drawable.roundedbuttonred)
                btn_quiz_submitnext.text = "NEXT"
            }

            //if option 2 is selected, and is correct
            else if (btn_quiz_2.text == questionArray[questionNumber - 1].correctAnswer){
                questionsCorrect++
                btn_quiz_2.setBackgroundResource(R.drawable.roundedbuttongreen)
                btn_quiz_submitnext.text = "NEXT"
            }
        }

        btn_quiz_3.setOnClickListener {
            btn_quiz_submitnext.setBackgroundResource(R.drawable.roundedbuttonblue)
            isSomethingChecked = true

            //disable the buttons
            btn_quiz_1.isEnabled = false
            btn_quiz_2.isEnabled = false
            btn_quiz_3.isEnabled = false
            btn_quiz_4.isEnabled = false

            //if option 3 is selected, but is wrong
            if (btn_quiz_3.text != questionArray[questionNumber - 1].correctAnswer){
                highlightCorrectAnswer()
                btn_quiz_3.setBackgroundResource(R.drawable.roundedbuttonred)
                btn_quiz_submitnext.text = "NEXT"
            }

            //if option 3 is selected, and is correct
            else if (btn_quiz_3.text == questionArray[questionNumber - 1].correctAnswer){
                questionsCorrect++
                btn_quiz_3.setBackgroundResource(R.drawable.roundedbuttongreen)
                btn_quiz_submitnext.text = "NEXT"
            }
        }

        btn_quiz_4.setOnClickListener {
            btn_quiz_submitnext.setBackgroundResource(R.drawable.roundedbuttonblue)
            isSomethingChecked = true

            //disable the buttons
            btn_quiz_1.isEnabled = false
            btn_quiz_2.isEnabled = false
            btn_quiz_3.isEnabled = false
            btn_quiz_4.isEnabled = false

            //if option 4 is selected, but is wrong
            if (btn_quiz_4.text != questionArray[questionNumber - 1].correctAnswer){
                highlightCorrectAnswer()
                btn_quiz_4.setBackgroundResource(R.drawable.roundedbuttonred)
                btn_quiz_submitnext.text = "NEXT"
            }

            //if option 4 is selected, and is correct
            else if (btn_quiz_4.text == questionArray[questionNumber - 1].correctAnswer){
                questionsCorrect++
                btn_quiz_4.setBackgroundResource(R.drawable.roundedbuttongreen)
                btn_quiz_submitnext.text = "NEXT"
            }
        }
        btn_quiz_submitnext.setOnClickListener {

            //if the user has already selected their answer for current page & there are still questions remaining
            if (isSomethingChecked && questionNumber < questionArray.size) {
                questionNumber++
                setTotalQsAndSetUpPage()

                //reenable the buttons
                btn_quiz_1.isEnabled = true
                btn_quiz_2.isEnabled = true
                btn_quiz_3.isEnabled = true
                btn_quiz_4.isEnabled = true
            }

            //if all questions have been answered, finish the quiz
            else if (isSomethingChecked && questionNumber >= questionArray.size){
                val intent = Intent(this, FinishQuizActivity::class.java)
                startActivity(intent)
            }

            else if (!isSomethingChecked){
                Toast.makeText(this, "Please make a selection.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun resetVariables() {
        val emptyQuestionArray = emptyArray<Question>()
        questionArray = emptyQuestionArray

        questionNumber = 1
        questionsCorrect = 0
        totalQuestions = 0
        submittedYet = false
        doneCollecting = false
        isSomethingChecked = false
    }

     private fun setTotalQsAndSetUpPage() {
        totalQuestions = questionArray.size
        if ((questionNumber - 1) < questionArray.size){
            setUpPage()
            //Log.d("QuizActivity", "Page set up")
        }
    }

    private fun setUpPage() {
        resetButtonColors()
        isSomethingChecked = false

        //set buttons to default colors and configurations - this is required when every new question shows up

        txtview_quiz_questionnum.text = "Question $questionNumber)"
        txtview_quiz_questionnum3.text = "$questionNumber / ${questionArray.size}"
        txtview_quiz_qtext.text = questionArray[questionNumber - 1].actualQuestion

        //setting the text of the buttons to be the answer choices
        btn_quiz_1.text = questionArray[questionNumber - 1].option1
        btn_quiz_2.text = questionArray[questionNumber - 1].option2
        btn_quiz_3.text = questionArray[questionNumber - 1].option3
        btn_quiz_4.text = questionArray[questionNumber - 1].option4
    }

    private fun highlightCorrectAnswer() {
        if (btn_quiz_1.text == questionArray[questionNumber - 1].correctAnswer){
            btn_quiz_1.setBackgroundResource(R.drawable.roundedbuttongreen)
        }
        else if (btn_quiz_2.text == questionArray[questionNumber - 1].correctAnswer){
            btn_quiz_2.setBackgroundResource(R.drawable.roundedbuttongreen)
        }
        else if (btn_quiz_3.text == questionArray[questionNumber - 1].correctAnswer){
            btn_quiz_3.setBackgroundResource(R.drawable.roundedbuttongreen)
        }
        else if (btn_quiz_4.text == questionArray[questionNumber - 1].correctAnswer){
            btn_quiz_4.setBackgroundResource(R.drawable.roundedbuttongreen)
        }
    }

    //this method is only called once
    private fun listenForQuestions(){
        var questionArrayList = emptyList<Question>()
        var questionArrayListShuffled = emptyList<Question>()
        var questionArrayListFinal = emptyList<Question>()

        val current = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("MM")
        val formatted = current.format(formatter)
        val currentMonth = QuizActivity.months[formatted.toInt()]
        var count = 0
        var numberOfQuestions = 0
        val ref = FirebaseDatabase.getInstance().getReference("/quizzes/$currentMonth")

        ref.addChildEventListener(object: ChildEventListener {
            override fun onChildAdded(p0: DataSnapshot, p1: String?) {
                val questionItem = p0.getValue(Question::class.java)

                //adding the individual questions to the array of questions
                if (questionItem != null) {
                    questionArray += questionItem
                    count++

                    //after all of the questions for this node are gathered, do this
                    if (count >= 10) {
                        doneCollecting = true

                        //convert the array of questions to a list, randomize them, get the first 5 and turn back into an array
                        questionArrayList = questionArray.toMutableList()
                        questionArrayListShuffled = questionArrayList.shuffled()
                        questionArrayListFinal = questionArrayListShuffled.take(5)
                        questionArray = questionArrayListFinal.toTypedArray()

                        setTotalQsAndSetUpPage()
                    }
                }
            }
            override fun onCancelled(p0: DatabaseError) {}
            override fun onChildChanged(p0: DataSnapshot, p1: String?) {}
            override fun onChildMoved(p0: DataSnapshot, p1: String?) {}
            override fun onChildRemoved(p0: DataSnapshot) {}
        })
        return
    }

    private fun resetButtonColors() {
        btn_quiz_1.setBackgroundResource(R.drawable.roundedbuttonwhite)
        btn_quiz_2.setBackgroundResource(R.drawable.roundedbuttonwhite)
        btn_quiz_3.setBackgroundResource(R.drawable.roundedbuttonwhite)
        btn_quiz_4.setBackgroundResource(R.drawable.roundedbuttonwhite)
        btn_quiz_submitnext.setBackgroundResource(R.drawable.roundedbuttonlightgrey)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.mymenu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when(item?.itemId) {
            R.id.menu_sign_out -> {
                FirebaseAuth.getInstance().signOut()
                val intent = Intent(this, LoginActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
            }
            R.id.menu_report -> {
                val intent = Intent(this, ReportActivity::class.java)
                startActivity(intent)
            }
        }
        return super.onOptionsItemSelected(item)
    }
}
