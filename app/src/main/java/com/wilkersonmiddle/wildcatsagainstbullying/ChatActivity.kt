package com.wilkersonmiddle.wildcatsagainstbullying

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import com.google.firebase.auth.FirebaseAuth
import com.wilkersonmiddle.wildcatsagainstbullying.R
//import com.example.wabproject.ChatRoom1Activity
import kotlinx.android.synthetic.main.activity_chat.*

class ChatActivity : AppCompatActivity() {
//TODO: change title on chat rooms to fit
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        supportActionBar?.title = "Chat"

        btn_chat_room1.setOnClickListener {
            val intent = Intent(this, ChatRoom1Activity::class.java)
            startActivity(intent)
        }

        btn_chat_room2.setOnClickListener {
            val intent = Intent(this, ChatRoom2Activity::class.java)
            startActivity(intent)
        }

        btn_chat_room3.setOnClickListener {
            val intent = Intent(this, ChatRoom3Activity::class.java)
            startActivity(intent)
        }
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
