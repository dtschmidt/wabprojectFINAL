package com.wilkersonmiddle.wildcatsagainstbullying

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_register.*
import com.google.firebase.auth.UserProfileChangeRequest


class RegisterActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        supportActionBar?.title = "Register"

        btn_register_register.setOnClickListener {
            performRegister()
        }


    }

    private fun performRegister(){
        val enteredEmail = txt_register_emailEntry.text.toString()
        var enteredPassword:String

        //if/else statement makes sure password and confirmPassword are the same
        if (txt_register_passwordEntry.text.toString() == txt_register_passwordConfirm.text.toString())
            enteredPassword = txt_register_passwordConfirm.text.toString()
        else {
            Toast.makeText(this, "Make sure your password entries match.", Toast.LENGTH_SHORT).show()
            return
        }

        val enteredName = txt_register_usernameEntry.text.toString()

        if (enteredEmail.isEmpty() || enteredPassword.isEmpty() || enteredName.isEmpty()){
            Toast.makeText(this, "Please enter an en email, password, and display name.", Toast.LENGTH_SHORT).show()
            return
        }
        // TODO else if (!enteredEmail.contains("bhm.k12.al.us")){
        //Toast.makeText(this, "Email must be for the birmingham school district.", Toast.LENGTH_SHORT).show()
        //return
        //}

        //Log.d("LoginActivity", "Email is: $enteredEmail")
        //Log.d("LoginActivity", "Password is: $enteredPassword")
        //Log.d("LoginActivity", "Name is: $enteredName")

        FirebaseAuth.getInstance().createUserWithEmailAndPassword(enteredEmail, enteredPassword)
            .addOnCompleteListener{
                if (!it.isSuccessful) return@addOnCompleteListener

                //saveUserToFirebaseDatabase()
                //else if successful
                //Log.d("Register", "Successfully created user with uid: ${it.result?.user?.uid}")
                //send email to verify user
                FirebaseAuth.getInstance().currentUser?.sendEmailVerification()
                    ?.addOnSuccessListener {

                        //send user back to login page if they successfully sent registration email
                        val intent = Intent(this, LoginActivity::class.java)
                        startActivity(intent)
                        ///////////////////////////////////////////////////////////////////////////

                        //Log.d("Register", "Sent email verificaiton email.")
                        Toast.makeText(this, "Verification email has been sent to your inbox. Click the" +
                                " link in the email to be able to log in.", Toast.LENGTH_SHORT).show()

                        //this block of code sets the user's display name
                        //TODO: one could probably change their display name by doing registration again, need to check and fix
                        val user = FirebaseAuth.getInstance().currentUser

                        val profileUpdates = UserProfileChangeRequest.Builder()
                            .setDisplayName(enteredName).build()

                        user?.updateProfile(profileUpdates)
                        //////////////////////////////////////////////////

                    }
                    ?.addOnFailureListener{
                        //Log.d("Register", "Verification email sending failed.")
                        Toast.makeText(this, "Error sending verification email. Please try creating " +
                                "your account again.", Toast.LENGTH_SHORT).show()
                    }

            }
            .addOnFailureListener{
                //Log.d("Register", "Failed to create user: ${it.message}")
                Toast.makeText(this, "Failed to create user: ${it.message}", Toast.LENGTH_SHORT).show()
            }

    }

    //below method was used to save the user again, but to the database. It shouldn't be needed any longer.

    /*private fun saveUserToFirebaseDatabase(){
        val uid = FirebaseAuth.getInstance().uid ?: ""
        val ref = FirebaseDatabase.getInstance().getReference("/users/$uid")

        val user = User(uid, txt_register_usernameEntry.text.toString(), txt_register_emailEntry.text.toString())

        ref.setValue(user)
            .addOnSuccessListener {
                Log.d("Register", "Saved user to firebase database")
                val intent = Intent(this, LoginActivity::class.java)
                //take user to login screen and clear out unused activities from the stack
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
            }
            .addOnFailureListener{
                Log.d("Register", "Failed to save user to database: ${it.message}")
            }
    }*/
}


