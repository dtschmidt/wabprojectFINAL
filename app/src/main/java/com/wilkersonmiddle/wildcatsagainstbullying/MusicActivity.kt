package com.wilkersonmiddle.wildcatsagainstbullying

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.content.Intent
import android.net.Uri
import android.view.Menu
import android.view.MenuItem
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_music.*


//import com.sun.org.apache.xalan.internal.xsltc.compiler.Constants.REDIRECT_URI


class MusicActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_music)

        supportActionBar?.title = "Music"

        btn_music_spotify1.setOnClickListener {
            val url = "https://open.spotify.com/playlist/75ARDXD31P1pM0jtVmc8eZ"
            val i = Intent(Intent.ACTION_VIEW)
            i.data = Uri.parse(url)
            startActivity(i)
        }

        btn_music_spotify2.setOnClickListener {
            val url = "https://open.spotify.com/playlist/4dJ8Ys2bOgWQIRvs7QSkeE"
            val i = Intent(Intent.ACTION_VIEW)
            i.data = Uri.parse(url)
            startActivity(i)
        }

        btn_music_youtube1.setOnClickListener {
            val url = "https://www.youtube.com/watch?v=Uim4GwSfzxY&list=PLBhIAkl3-Iovud5DyiASzw83S6d0fwOT2"
            val i = Intent(Intent.ACTION_VIEW)
            i.data = Uri.parse(url)
            startActivity(i)
        }

        btn_music_youtube2.setOnClickListener {
            val url = "https://www.youtube.com/watch?v=8rLTERuuxm4&list=PLBhIAkl3-Iovo1Bbw-k_NXGus-RnG2FtN"
            val i = Intent(Intent.ACTION_VIEW)
            i.data = Uri.parse(url)
            startActivity(i)
        }


    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(com.wilkersonmiddle.wildcatsagainstbullying.R.menu.mymenu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when(item?.itemId) {
            com.wilkersonmiddle.wildcatsagainstbullying.R.id.menu_sign_out -> {
                FirebaseAuth.getInstance().signOut()
                val intent = Intent(this, LoginActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
            }
            com.wilkersonmiddle.wildcatsagainstbullying.R.id.menu_report -> {
                val intent = Intent(this, ReportActivity::class.java)
                startActivity(intent)
            }
        }
        return super.onOptionsItemSelected(item)
    }
}

