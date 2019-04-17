package com.wilkersonmiddle.wildcatsagainstbullying

import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_homepage.*
//TODO: documentation to change chat room titles
//TODO: documentation telling them not to delete certain firebase nodes
//TODO: email thing!!
class HomepageActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_homepage)

        supportActionBar?.title = "Wildcats Against Bullying"

        verifyUserIsLoggedIn()

        val uid = FirebaseAuth.getInstance().uid
        if (uid == null){
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        btn_homepage_game.setOnClickListener {
            val intent = Intent(this, GamesActivity::class.java)
            startActivity(intent)
        }

        btn_homepage_music.setOnClickListener {
            val intent = Intent(this, MusicActivity::class.java)
            startActivity(intent)
        }

        btn_homepage_chat.setOnClickListener {
            //open up "BeforeChatActivity" only the first time the app is opened
            val isFirstRun = getSharedPreferences("PREFERENCE", Context.MODE_PRIVATE)
                .getBoolean("isFirstRunChat", true)

            if (isFirstRun) {
                //show BeforeReport activity
                startActivity(Intent(this, BeforeChatActivity::class.java))
            }
            //else if this isn't the first time the app is being run, open up chat
            else {
                val intent = Intent(this, ChatActivity::class.java)
                startActivity(intent)
            }

            getSharedPreferences("PREFERENCE", Context.MODE_PRIVATE).edit()
                .putBoolean("isFirstRunChat", false).apply()
            ////////////////////////////////////////////////////////////////////////
        }

        btn_homepage_report.setOnClickListener {

            //open up "BeforeReportActivity" only the first time the app is opened
            val isFirstRun = getSharedPreferences("PREFERENCE", Context.MODE_PRIVATE)
                .getBoolean("isFirstRunReport", true)

            if (isFirstRun) {
                //show BeforeReport activity
                startActivity(Intent(this, BeforeReportActivity::class.java))
            }
            //else if this isn't the first time the app is being run, open up report
            else {
                val intent = Intent(this, ReportActivity::class.java)
                startActivity(intent)
            }

            getSharedPreferences("PREFERENCE", Context.MODE_PRIVATE).edit()
                .putBoolean("isFirstRunReport", false).apply()
            ////////////////////////////////////////////////////////////////////////
        }

        btn_homepage_links.setOnClickListener {
            val intent = Intent(this, LinksActivity::class.java)
            startActivity(intent)
        }
    }

    private fun verifyUserIsLoggedIn() {
        val uid = FirebaseAuth.getInstance().uid
        if (uid == null || FirebaseAuth.getInstance().currentUser?.isEmailVerified == false) {
            val intent = Intent(this, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        }
    }

    //renders the buttons that appear on the toolbar
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
