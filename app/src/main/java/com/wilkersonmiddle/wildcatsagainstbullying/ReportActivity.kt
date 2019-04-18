package com.wilkersonmiddle.wildcatsagainstbullying

import android.app.Activity
import android.content.Intent
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.wilkersonmiddle.wildcatsagainstbullying.classes.Report
import kotlinx.android.synthetic.main.activity_report.*
import java.util.*

class ReportActivity : AppCompatActivity() {
    companion object {

    }

    var reportKey: String? = ""
    var isInDatabase: Boolean = false
    var imageDownloadUrl: String? = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_report)

        supportActionBar?.title = "Report"

        var anonIsChecked = false


        //takes away "name" field if anonymous is checked
        switch_report_anon.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked){
                txt_report_name.visibility = View.GONE
                anonIsChecked = true
            }
            else{
                txt_report_name.visibility = View.VISIBLE
                anonIsChecked = false
            }
        }

        btn_report_submit.setOnClickListener {
            performSubmit(anonIsChecked)


        }

        btn_report_uploadfile.setOnClickListener {
            Log.d("ReportActivity", "Try to show photo selector")

            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            //calls "onActivityResult"
            startActivityForResult(intent, 0)
        }
    }


    //This method allows user to select a photo from their downloads and then shows a preview of the image
    //below the submit button
    var selectedPhotoUri: Uri? = null
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 0 && resultCode == Activity.RESULT_OK && data != null){
            Log.d("ReportActivity", "Photo was selected")

            selectedPhotoUri = data.data

            val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, selectedPhotoUri)

            val bitmapDrawable = BitmapDrawable(bitmap)
            imgview_report_uploadedimage.setBackgroundDrawable(bitmapDrawable)

            //upload selected image to database, if one's been selected
            uploadImageToFirebaseStorage()
        }
    }

    private fun performSubmit(anonIsChecked: Boolean){


        val valid = isSubmissionValid(anonIsChecked)
        if (valid == false)
            return

        else{
            //actually puts report in database
            putReportInDatabase()
        }

        val intent = Intent(this, LinksActivity::class.java)
        //Tell links page to show thank you message
        intent.putExtra("showThankYou", true)
        startActivity(intent)


    }


    //Puts image in firebase storage database

    fun uploadImageToFirebaseStorage() {
        //If theres nothing to put in database, exit out of function
        if (selectedPhotoUri == null) return

        val filename = FirebaseAuth.getInstance().currentUser?.uid + "----------" + UUID.randomUUID().toString()
        val ref = FirebaseStorage.getInstance().getReference("$filename")
        ref.putFile(selectedPhotoUri!!)
            .addOnSuccessListener {

                Log.d("ReportActivity", "Successfully uploaded image: ${it.metadata?.path}")

                ref.downloadUrl.addOnSuccessListener {
                    Log.d("ReportActivity", "File location: $it")
                    imageDownloadUrl = it.toString()
                }
            }

            .addOnFailureListener{
                Toast.makeText(
                    this, "Failed to upload image: ${it.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
    }

    fun putReportInDatabase(){
        val enteredName = txt_report_name.text.toString()
        val enteredDate = txt_report_date.text.toString()
        val enteredDescription = txt_report_description.text.toString()

        /*inverseTimestamp subtracts the current time from a much longer number in
        order to sort reports by most recent first in the firebase console*/
        val inverseTimestamp = ( 3000000000000 - System.currentTimeMillis())
        val uid = FirebaseAuth.getInstance().uid ?: ""
        val reportid = UUID.randomUUID().toString()
        val email = FirebaseAuth.getInstance().currentUser?.email
        val imageurl = imageDownloadUrl
        val handledYet = "no"
        val ref = FirebaseDatabase.getInstance().getReference("/reports").push() ///$inverseTimestamp
        reportKey = ref.key
        val report = Report(inverseTimestamp, uid, reportid, email, enteredName, enteredDate, enteredDescription, imageurl, handledYet)

        //what actually uploads the report to the database
        ref.setValue(report)
            .addOnSuccessListener {
                Log.d("ReportActivity", "Saved report to firebase database")
                Toast.makeText(
                    this, "Successfully sumbmitted report. Thank you.",
                    Toast.LENGTH_SHORT
                ).show()

                //open email prompt with prefilled fields
                val intent = Intent(Intent.ACTION_SEND)
                intent.type = "plain/text"
                intent.putExtra(Intent.EXTRA_EMAIL, arrayOf("wildcatsagainstbullying@gmail.com"))
                intent.putExtra(Intent.EXTRA_SUBJECT, "REPORT $enteredDate")
                intent.putExtra(Intent.EXTRA_TEXT, "$enteredName:\n$enteredDescription\n$imageurl")
                startActivity(Intent.createChooser(intent, ""))
            }
            .addOnFailureListener() {
                Log.d("ReportActivity", "Failed to save report: ${it.message}")
                Toast.makeText(
                    this, "Failed to sumbmit report: ${it.message}",
                    Toast.LENGTH_SHORT
                ).show()
                return@addOnFailureListener
            }
    }

    private fun isSubmissionValid(anonIsChecked: Boolean): Boolean{
        val enteredName = txt_report_name.text.toString()
        val enteredDate = txt_report_date.text.toString()
        val enteredDescription = txt_report_description.text.toString()
        if (anonIsChecked == false && enteredName.isEmpty()){
            Toast.makeText(this, "Please enter your name or submit an anonymous report.", Toast.LENGTH_SHORT).show()
            return false
        }
        else if (enteredDate.isEmpty() || enteredDescription.isEmpty()){
            Toast.makeText(this, "Please enter a date and description of the incident.", Toast.LENGTH_SHORT).show()
            return false

        }
        else
            return true

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
