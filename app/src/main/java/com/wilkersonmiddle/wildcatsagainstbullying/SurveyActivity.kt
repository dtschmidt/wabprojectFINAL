package com.wilkersonmiddle.wildcatsagainstbullying

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import com.google.firebase.database.DataSnapshot
import com.wilkersonmiddle.wildcatsagainstbullying.classes.Question
import com.wilkersonmiddle.wildcatsagainstbullying.classes.SurveyResults
import kotlinx.android.synthetic.main.activity_survey.*


class SurveyActivity : AppCompatActivity() {

    companion object {

        var questionArray = emptyArray<Question>()

        var questionNumber = 0

        var totalQuestions = 0

        var submittedYet = false

        var doneCollecting = false

        var isSomethingChecked = false

        var surveyResultsItem: SurveyResults? = SurveyResults()

        //set to 5, so just in case something goes wrong, the default value won't mess up the average too much
        var q1response = 5
        var q2response = 5
        var q3response = 5
        var q4response = 5
        var q5response = 5
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_survey)

        supportActionBar?.title = "Survey"

        resetVariables()
        listenForQuestions()
        listenForResults()

        btn_survey_1.setOnClickListener {
            btn_survey_submitnext.setBackgroundResource(R.drawable.roundedbuttonblue)
            btn_survey_1.setBackgroundResource(R.drawable.roundedbuttonblue)
            btn_survey_submitnext.text = "NEXT"
            isSomethingChecked = true

            //disable the rest of the buttons
            btn_survey_1.isEnabled = false
            btn_survey_2.isEnabled = false
            btn_survey_3.isEnabled = false
            btn_survey_4.isEnabled = false

            when (questionArray[questionNumber - 1].actualQuestion) {
                "Did you treat others the way you wanted to be treated this month?" -> q1response = 10
                "Did you witness any bullying this month?" -> q2response = 10
                "Were you able to speak up against bullying this month?" -> q3response = 10
                "How comfortable are you with your peers?" -> q4response = 10
                "Which one of these statements reflects how you most feel about attending school?" -> q5response = 10
            }
        }

        btn_survey_2.setOnClickListener {
            btn_survey_submitnext.setBackgroundResource(R.drawable.roundedbuttonblue)
            btn_survey_2.setBackgroundResource(R.drawable.roundedbuttonblue)
            btn_survey_submitnext.text = "NEXT"
            isSomethingChecked = true

            //disable the rest of the buttons
            btn_survey_1.isEnabled = false
            btn_survey_2.isEnabled = false
            btn_survey_3.isEnabled = false
            btn_survey_4.isEnabled = false

            when (questionArray[questionNumber - 1].actualQuestion) {
                "Did you treat others the way you wanted to be treated this month?" -> q1response = 0
                "Did you witness any bullying this month?" -> q2response = 0
                "Were you able to speak up against bullying this month?" -> q3response = 0
                "How comfortable are you with your peers?" -> q4response = 6
                "Which one of these statements reflects how you most feel about attending school?" -> q5response = 6
            }
        }

        btn_survey_3.setOnClickListener {
            btn_survey_submitnext.setBackgroundResource(R.drawable.roundedbuttonblue)
            btn_survey_3.setBackgroundResource(R.drawable.roundedbuttonblue)
            btn_survey_submitnext.text = "NEXT"
            isSomethingChecked = true

            //disable the rest of the buttons
            btn_survey_1.isEnabled = false
            btn_survey_2.isEnabled = false
            btn_survey_3.isEnabled = false
            btn_survey_4.isEnabled = false

            when (questionArray[questionNumber - 1].actualQuestion) {
                "How comfortable are you with your peers?" -> q4response = 3
                "Which one of these statements reflects how you most feel about attending school?" -> q5response = 3
            }
        }

        btn_survey_4.setOnClickListener {
            btn_survey_submitnext.setBackgroundResource(R.drawable.roundedbuttonblue)
            btn_survey_4.setBackgroundResource(R.drawable.roundedbuttonblue)
            btn_survey_submitnext.text = "NEXT"
            isSomethingChecked = true

            //disable the rest of the buttons
            btn_survey_1.isEnabled = false
            btn_survey_2.isEnabled = false
            btn_survey_3.isEnabled = false
            btn_survey_4.isEnabled = false

            when (questionArray[questionNumber - 1].actualQuestion) {
                "How comfortable are you with your peers?" -> q4response = 0
                "Which one of these statements reflects how you most feel about attending school?" -> q5response = 0
            }
        }

        btn_survey_submitnext.setOnClickListener {
            //if the user has already selected their answer for current page & there are still questions remaining
            if (isSomethingChecked && questionNumber < questionArray.size) {
                questionNumber++

                //reenable the buttons
                btn_survey_1.isEnabled = true
                btn_survey_2.isEnabled = true
                btn_survey_3.isEnabled = true
                btn_survey_4.isEnabled = true
                btn_survey_1.visibility = View.VISIBLE
                btn_survey_2.visibility = View.VISIBLE
                btn_survey_3.visibility = View.VISIBLE
                btn_survey_4.visibility = View.VISIBLE

                setTotalQsAndSetUpPage()
            }

            //if all questions have been answered, finish the survey
            else if (isSomethingChecked && questionNumber >= questionArray.size){
                uploadResults()

                val intent = Intent(this, FinishSurveyActivity::class.java)
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
        totalQuestions = 0
        submittedYet = false
        doneCollecting = false
        isSomethingChecked = false
    }

    private fun setTotalQsAndSetUpPage() {
        totalQuestions = questionArray.size
        if ((questionNumber - 1) < questionArray.size){
            setUpPage()
            //Log.d("SurveyActivity", "Page set up")
        }
    }

    private fun setUpPage() {
        resetButtonColors()
        isSomethingChecked = false

        //set buttons to default colors and configurations - this is required when every new question shows up

        txtview_survey_questionnum.text = "Question $questionNumber)"
        txtview_survey_questionnum3.text = "$questionNumber / ${questionArray.size}"
        txtview_survey_qtext.text = questionArray[questionNumber - 1].actualQuestion

        //setting the text of the buttons to be the answer choices
        btn_survey_1.text = questionArray[questionNumber - 1].option1
        btn_survey_2.text = questionArray[questionNumber - 1].option2

        //since some survey questions only have two answer choices, only show the choices available
        if(questionArray[questionNumber - 1].option3 == "")
            btn_survey_3.visibility = View.GONE
        else
            btn_survey_3.text = questionArray[questionNumber - 1].option3

        if(questionArray[questionNumber - 1].option4 == "")
            btn_survey_4.visibility = View.GONE
        else
            btn_survey_4.text = questionArray[questionNumber - 1].option4
        ////////////////////////////////////////////////////////////////////////////////////////////
    }

    //this method is only called once
    private fun listenForQuestions(){
        var questionArrayList = emptyList<Question>()
        var questionArrayListShuffled = emptyList<Question>()
        var questionArrayListFinal = emptyList<Question>()

        val current = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("MM")
        val formatted = current.format(formatter)
        var count = 0
        var numberOfQuestions = 0
        val ref = FirebaseDatabase.getInstance().getReference("/survey")

        ref.addChildEventListener(object: ChildEventListener {
            override fun onChildAdded(p0: DataSnapshot, p1: String?) {
                val questionItem = p0.getValue(Question::class.java)

                //adding the individual questions to the array of questions
                if (questionItem != null) {
                    questionArray += questionItem
                    count++

                    //after all of the questions for this node are gathered, do this
                    if (count >= 5) {
                        doneCollecting = true
                        numberOfQuestions = questionArray.size
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

    private fun listenForResults() {

        val ref = FirebaseDatabase.getInstance().getReference("/surveyresults")

        ref.addChildEventListener(object: ChildEventListener {
            override fun onChildAdded(p0: DataSnapshot, p1: String?) {
                surveyResultsItem  = p0.getValue(SurveyResults::class.java)
            }

            override fun onCancelled(p0: DatabaseError) {}
            override fun onChildChanged(p0: DataSnapshot, p1: String?) {}
            override fun onChildMoved(p0: DataSnapshot, p1: String?) {}
            override fun onChildRemoved(p0: DataSnapshot) {}
        })

    }

    private fun uploadResults(){
        var q1Numresponses = surveyResultsItem?.q1responses?.toDouble()
        var q2Numresponses = surveyResultsItem?.q2responses?.toDouble()
        var q3Numresponses = surveyResultsItem?.q3responses?.toDouble()
        var q4Numresponses = surveyResultsItem?.q4responses?.toDouble()
        var q5Numresponses = surveyResultsItem?.q4responses?.toDouble()
        var q1runningSum = surveyResultsItem?.q1runningsum?.toDouble()
        var q2runningSum = surveyResultsItem?.q2runningsum?.toDouble()
        var q3runningSum = surveyResultsItem?.q3runningsum?.toDouble()
        var q4runningSum = surveyResultsItem?.q4runningsum?.toDouble()
        var q5runningSum = surveyResultsItem?.q4runningsum?.toDouble()
        var q1avg = surveyResultsItem?.q1avg?.toDouble()
        var q2avg = surveyResultsItem?.q2avg?.toDouble()
        var q3avg = surveyResultsItem?.q3avg?.toDouble()
        var q4avg = surveyResultsItem?.q4avg?.toDouble()
        var q5avg = surveyResultsItem?.q4avg?.toDouble()


        if (q1Numresponses != null) {
            q1Numresponses++
            surveyResultsItem?.q1responses = q1Numresponses.toString()
        }
        if (q2Numresponses != null) {
            q2Numresponses++
            surveyResultsItem?.q2responses = q2Numresponses.toString()
        }
        if (q3Numresponses != null) {
            q3Numresponses++
            surveyResultsItem?.q3responses = q3Numresponses.toString()
        }
        if (q4Numresponses != null) {
            q4Numresponses++
            surveyResultsItem?.q4responses = q4Numresponses.toString()
        }
        if (q5Numresponses != null) {
            q5Numresponses++
            surveyResultsItem?.q5responses = q5Numresponses.toString()
        }

        if (q1runningSum != null) {
            q1runningSum += q1response
            surveyResultsItem?.q1runningsum = q1runningSum.toString()
        }
        if (q2runningSum != null) {
            q2runningSum += q2response
            surveyResultsItem?.q2runningsum = q2runningSum.toString()
        }
        if (q3runningSum != null) {
            q3runningSum += q3response
            surveyResultsItem?.q3runningsum = q3runningSum.toString()
        }
        if (q4runningSum != null) {
            q4runningSum += q4response
            surveyResultsItem?.q4runningsum = q4runningSum.toString()
        }
        if (q5runningSum != null) {
            q5runningSum += q5response
            surveyResultsItem?.q5runningsum = q5runningSum.toString()
        }

        if (q1avg != null && q1Numresponses != null && q1runningSum != null) {
            q1avg = q1runningSum / q1Numresponses
            surveyResultsItem?.q1avg = q1avg.toString()
        }
        if (q2avg != null && q2Numresponses != null && q2runningSum != null) {
            q2avg = q2runningSum / q2Numresponses
            surveyResultsItem?.q2avg = q2avg.toString()
        }
        if (q3avg != null && q3Numresponses != null && q3runningSum != null) {
            q3avg = q3runningSum / q3Numresponses
            surveyResultsItem?.q3avg = q3avg.toString()
        }
        if (q4avg != null && q4Numresponses != null && q4runningSum != null) {
            q4avg = q4runningSum / q4Numresponses
            surveyResultsItem?.q4avg = q4avg.toString()
        }
        if (q5avg != null && q5Numresponses != null && q5runningSum != null) {
            q5avg = q5runningSum / q5Numresponses
            surveyResultsItem?.q5avg = q5avg.toString()
        }

        val reference = FirebaseDatabase.getInstance().getReference()
        val results = surveyResultsItem
        reference.child("surveyresults/surveyresults").setValue(results)
            .addOnSuccessListener {
                //Log.d("SurveyActivity", "Survey results updated")
            }


    }

    private fun resetButtonColors() {
        btn_survey_1.setBackgroundResource(R.drawable.roundedbuttonwhite)
        btn_survey_2.setBackgroundResource(R.drawable.roundedbuttonwhite)
        btn_survey_3.setBackgroundResource(R.drawable.roundedbuttonwhite)
        btn_survey_4.setBackgroundResource(R.drawable.roundedbuttonwhite)
        btn_survey_submitnext.setBackgroundResource(R.drawable.roundedbuttonlightgrey)
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
