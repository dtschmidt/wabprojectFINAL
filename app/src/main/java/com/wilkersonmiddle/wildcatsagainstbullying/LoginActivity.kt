package com.wilkersonmiddle.wildcatsagainstbullying

import android.app.Dialog
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.Window
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {
//TODO: need forgot password thing

    internal lateinit var forgotPassword : TextView
    internal lateinit var popup: Dialog
    internal lateinit var nevermind: Button
    internal lateinit var clickhere: Button
    internal lateinit var email: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        supportActionBar?.title= "Login"

        forgotPassword = findViewById<View>(R.id.txtview_login_forgotpassword) as TextView
        forgotPassword.setOnClickListener {
            showDialog()
        }

        btn_main_login.setOnClickListener {
            performLogin()
        }

        btn_main_newUser.setOnClickListener {
            //Log.d("LoginActivity", "Try to show register activity")

            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }




    }

    private fun performLogin(){
        val enteredLoginEmail = txt_main_emailEntry.text.toString()
        val enteredLoginPassword = txt_main_passwordEntry.text.toString()

        if (enteredLoginEmail.isEmpty() || enteredLoginPassword.isEmpty()){
            Toast.makeText(this, "Please enter an email and password.", Toast.LENGTH_SHORT).show()
            return
        }

        //Log.d("LoginActivity", "Email is: " + enteredLoginEmail)
        //Log.d("LoginActivity", "Password is: $enteredLoginPassword")


        FirebaseAuth.getInstance().signInWithEmailAndPassword(enteredLoginEmail, enteredLoginPassword)
            .addOnCompleteListener{
                if (!it.isSuccessful) return@addOnCompleteListener

                //else if successful -- only log user in if their email is verified
                if (FirebaseAuth.getInstance().currentUser?.isEmailVerified == true) {
                    //Log.d("Main", "Successfully logged in user with uid: ${it.result?.user?.uid}")
                    Toast.makeText(this, "Successfully logged in. Welcome!", Toast.LENGTH_SHORT).show()

                    //sends app to the homepage after login
                    val intent = Intent(this, HomepageActivity::class.java)
                    //clears the login page so the user can't go back to it
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                    startActivity(intent)
                }
                else{
                    Toast.makeText(this, "Please verify your email address in order to log in."
                        , Toast.LENGTH_SHORT).show()
                    return@addOnCompleteListener
                }
            }
            .addOnFailureListener{
                //Log.d("Register", "Failed to login user: ${it.message}")
                Toast.makeText(this, "Failed to login user: ${it.message}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun showDialog() {
        popup = Dialog(this)
        popup.requestWindowFeature(Window.FEATURE_NO_TITLE)
        popup.setContentView(R.layout.forgot_password_popup)
        popup.setTitle("Forgot Password")

        nevermind = popup.findViewById<View>(R.id.btn_popup_nevermind) as Button
        clickhere = popup.findViewById<View>(R.id.btn_popup_clickhere) as Button
        email = popup.findViewById<View>(R.id.txt_popup_email) as EditText
        nevermind.isEnabled = true
        nevermind.setOnClickListener {
            popup.cancel()
        }
        clickhere.setOnClickListener {
            if (email.text.toString() != "") {
                val auth = FirebaseAuth.getInstance()

                auth.sendPasswordResetEmail(email.text.toString())
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            popup.cancel()
                            //Log.d("LoginActivity", "Reset password email sent.")
                            Toast.makeText(this, "Reset password email sent.", Toast.LENGTH_SHORT).show()
                        }

                    }
                    .addOnFailureListener {
                        Toast.makeText(this, "Did not send password reset email: ${it.message}", Toast.LENGTH_SHORT)
                            .show()
                    }
            }
            else {
                Toast.makeText(this, "Please enter your email.", Toast.LENGTH_SHORT)
                    .show()
                return@setOnClickListener
            }
        }
        popup.show()
    }
}
