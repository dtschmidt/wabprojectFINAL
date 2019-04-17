package com.wilkersonmiddle.wildcatsagainstbullying

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.KeyEvent
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.wilkersonmiddle.wildcatsagainstbullying.R
import com.wilkersonmiddle.wildcatsagainstbullying.classes.ChatFromItem
import com.wilkersonmiddle.wildcatsagainstbullying.classes.ChatMessage
import com.wilkersonmiddle.wildcatsagainstbullying.classes.ChatToItem
//import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.activity_chat_room1.*
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class ChatRoom1Activity : AppCompatActivity() {

    companion object {
        //tag used in logs for the activity
        val TAG = "ChatRoom1Log"

        val roomName = "Chat Room 1"

        val profanity = arrayOf("A55",  "a55hole",  "aeolus",  "ahole",  "anal",  "analprobe",  "anilingus",  "anus",  "areola",  "areole",  "arian",  "aryan",  "ass",  "assbang",  "assbanged",  "assbangs",  "asses",  "assfuck",  "assfucker",  "assh0le",  "asshat",  "asshole",  "ass hole",  "assholes",  "assmaster",  "assmunch",  "asswipe",  "asswipes", "azazel", "azz", "b1tch", "babe", "babes", "ballsack", "bang", "banger", "barf", "bastard", "bastards", "bawdy", "beaner", "beardedclam", "beastiality", "beatch", "beater", "beaver", "beer", "beeyotch", "beotch", "biatch", "bigtits", "big tits", "bimbo", "bitch", "bitched", "bitches", "bitchy", "blow job", "blow", "blowjob", "blowjobs", "bod", "bodily", "boink", "bollock", "bollocks", "bollok", "bone", "boned", "boner", "boners", "bong", "boob", "boobies", "boobs", "booby", "booger", "bookie", "bootee", "bootie", "booty", "booze", "boozer", "boozy", "bosom", "bosomy", "bowel", "bowels", "bra", "brassiere", "breast", "breasts", "bugger", "bukkake", "bullshit", "bull shit", "bullshits", "bullshitted", "bullturds", "bung", "busty", "butt", "buttfuck", "buttfuck", "buttfucker", "buttfucker", "buttplug", "c.0.c.k", "c.o.c.k.", "c.u.n.t", "c0ck", "c-0-c-k", "caca", "cahone", "cameltoe", "carpetmuncher", "cawk", "cervix", "chinc", "chincs", "chink", "chink", "chode", "chodes", "cl1t", "climax", "clit", "clitoris", "clitorus", "clits", "clitty", "cocain", "cocaine", "cock", "c-o-c-k", "cockblock", "cockholster", "cockknocker", "cocks", "cocksmoker", "cocksucker", "cock sucker", "coital", "commie", "condom", "coon", "coons", "corksucker", "crabs", "crack", "cracker", "crackwhore", "crap", "crappy", "cum", "cummin", "cumming", "cumshot", "cumshots", "cumslut", "cumstain", "cunilingus", "cunnilingus", "cunny", "cunt", "cunt", "c-u-n-t", "cuntface", "cunthunter", "cuntlick", "cuntlicker", "cunts", "d0ng", "d0uch3", "d0uche", "d1ck", "d1ld0", "d1ldo", "dago", "dagos", "dammit", "damn", "damned", "damnit", "dawgie-style", "dick", "dickbag", "dickdipper", "dickface", "dickflipper", "dickhead", "dickheads", "dickish", "dick-ish", "dickripper", "dicksipper", "dickweed", "dickwhipper", "dickzipper", "diddle", "dike", "dildo", "dildos", "diligaf", "dillweed", "dimwit", "dingle", "dipship", "doggie-style", "doggy-style", "dong", "doofus", "doosh", "dopey", "douch3", "douche", "douchebag", "douchebags", "douchey", "drunk", "dumass", "dumbass", "dumbasses", "dummy", "dyke", "dykes", "ejaculate", "enlargement", "erect", "erection", "erotic", "essohbee", "extacy", "extasy", "f.u.c.k", "fack", "fag", "fagg", "fagged", "faggit", "faggot", "fagot", "fags", "faig", "faigt", "fannybandit", "fart", "fartknocker", "fat", "felch", "felcher", "felching", "fellate", "fellatio", "feltch", "feltcher", "fisted", "fisting", "fisty", "floozy", "foad", "fondle", "foobar", "foreskin", "freex", "frigg", "frigga", "fubar", "fuck", "f-u-c-k", "fuckass", "fucked", "fucked", "fucker", "fuckface", "fuckin", "fucking", "fucknugget", "fucknut", "fuckoff", "fucks", "fucktard", "fuck-tard", "fuckup", "fuckwad", "fuckwit", "fudgepacker", "fuk", "fvck", "fxck", "gae", "gai", "ganja", "gay", "gays", "gey", "gfy", "ghay", "ghey", "gigolo", "glans", "goatse", "godamn", "godamnit", "goddam", "goddammit", "goddamn", "goldenshower", "gonad", "gonads", "gook", "gooks", "gringo", "gspot", "g-spot", "gtfo", "guido", "h0m0", "h0mo", "handjob", "hard on", "he11", "hebe", "heeb", "hell", "hemp", "heroin", "herp", "herpes", "herpy", "hitler", "hiv", "hobag", "hom0", "homey", "homo", "homoey", "honky", "hooch", "hookah", "hooker", "hoor", "hootch", "hooter", "hooters", "horny", "hump", "humped", "humping", "hussy", "hymen", "inbred", "incest", "injun", "j3rk0ff", "jackass", "jackhole", "jackoff", "jap", "japs", "jerk", "jerk0ff", "jerked", "jerkoff", "jism", "jiz", "jizm", "jizz", "jizzed", "junkie", "junky", "kike", "kikes", "kill", "kinky", "kkk", "klan", "knobend", "kooch", "kooches", "kootch", "kraut", "kyke", "labia", "lech", "leper", "lesbians", "lesbo", "lesbos", "lez", "lezbian", "lezbians", "lezbo", "lezbos", "lezzie", "lezzies", "lezzy", "lmao", "lmfao", "loin", "loins", "lube", "lusty", "mams", "massa", "masterbate", "masterbating", "masterbation", "masturbate", "masturbating", "masturbation", "maxi", "menses", "menstruate", "menstruation", "meth", "m-fucking", "mofo", "molest", "moolie", "moron", "motherfucka", "motherfucker", "motherfucking", "mtherfucker", "mthrfucker", "mthrfucking", "muff", "muffdiver", "murder", "muthafuckaz", "muthafucker", "mutherfucker", "mutherfucking", "muthrfucking", "nad", "nads", "naked", "napalm", "nappy", "nazi", "nazism", "negro", "nigga", "niggah", "niggas", "niggaz", "nigger", "nigger", "niggers", "niggle", "niglet", "nimrod", "ninny", "nipple", "nooky", "nympho", "opiate", "opium", "oral", "orally", "organ", "orgasm", "orgasmic", "orgies", "orgy", "ovary", "ovum", "ovums", "p.u.s.s.y.", "paddy", "paki", "pantie", "panties", "panty", "pastie", "pasty", "pcp", "pecker", "pedo", "pedophile", "pedophilia", "pedophiliac", "pee", "peepee", "penetrate", "penetration", "penial", "penile", "penis", "perversion", "peyote", "phalli", "phallic", "phuck", "pillowbiter", "pimp", "pinko", "piss", "pissed", "pissoff", "piss-off", "pms", "polack", "pollock", "poon", "poontang", "porn", "porno", "pornography", "pot", "potty", "prick", "prig", "prostitute", "prude", "pube", "pubic", "pubis", "punkass", "punky", "puss", "pussies", "pussy", "pussypounder", "puto", "queaf", "queef", "queef", "queer", "queero", "queers", "quicky", "quim", "racy", "rape", "raped", "raper", "rapist", "raunch", "rectal", "rectum", "rectus", "reefer", "reetard", "reich", "retard", "retarded", "revue", "rimjob", "ritard", "rtard", "r-tard", "rum", "rump", "rumprammer", "ruski", "s.h.i.t.", "s.o.b.", "s0b", "sadism", "sadist", "scag", "scantily", "schizo", "schlong", "screw", "screwed", "scrog", "scrot", "scrote", "scrotum", "scrud", "scum", "seaman", "seamen", "seduce", "semen", "sex", "sexual", "sh1t", "s-h-1-t", "shamedame", "shit", "s-h-i-t", "shite", "shiteater", "shitface", "shithead", "shithole", "shithouse", "shits", "shitt", "shitted", "shitter", "shitty", "shiz", "sissy", "skag", "skank", "slave", "sleaze", "sleazy", "slut", "slutdumper", "slutkiss", "sluts", "smegma", "smut", "smutty", "snatch", "sniper", "snuff", "s-o-b", "sodom", "souse", "soused", "sperm", "spic", "spick", "spik", "spiks", "spooge", "spunk", "steamy", "stfu", "stiffy", "stoned", "strip", "stroke", "stupid", "suck", "sucked", "sucking", "sumofabiatch", "t1t", "tampon", "tard", "tawdry", "teabagging", "teat", "terd", "teste", "testee", "testes", "testicle", "testis", "thrust", "thug", "tinkle", "tit", "titfuck", "titi", "tits", "tittiefucker", "titties", "titty", "tittyfuck", "tittyfucker", "toke", "toots", "tramp", "transsexual", "trashy", "tubgirl", "turd", "tush", "twat", "twats", "ugly", "undies", "unwed", "urinal", "urine", "uterus", "uzi", "vag", "vagina", "valium", "viagra", "virgin", "vixen", "vodka", "vomit", "voyeur", "vulgar", "vulva", "wad", "wang", "wank", "wanker", "wazoo", "wedgie", "weed", "weenie", "weewee", "weiner", "weirdo", "wench", "wetback", "wh0re", "wh0reface", "whitey", "whiz", "whoralicious", "whore", "whorealicious", "whored", "whoreface", "whorehopper", "whorehouse", "whores", "whoring", "wigger", "womb", "woody", "wop", "wtf", "x-rated", "xxx", "yeasty", "yobbo", "zoophile")
    }

    val adapter = GroupAdapter<ViewHolder>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat_room1)

        supportActionBar?.title = "Physical Bullying"

        recyclerview_cr1_messages.adapter = adapter
        //setupDummyData()
        listenForMessages()

        //this block uses the enter button on the keyboard to execute code
        edittxt_cr1_messageentry.setOnKeyListener(View.OnKeyListener { v, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_UP) {

                //code to be executed
                performSendMessage()
                //////////////////////

                return@OnKeyListener true
            }
            false
        })

    }

    private fun listenForMessages(){
        //orderbychild orders the posts by their timestamps. limittolast gets the most recent 100 posts based on that order
        val ref = FirebaseDatabase.getInstance().getReference("/chats/physicalbullyingchatroom").limitToLast(100)

        ref.addChildEventListener(object: ChildEventListener {
            override fun onChildAdded(p0: DataSnapshot, p1: String?) {
                val chatMessage = p0.getValue(ChatMessage::class.java)

                if(chatMessage != null) {

                    //if the sender of the message is the logged in user, display it as such
                    if (chatMessage.sender_id == FirebaseAuth.getInstance().currentUser?.email)
                        adapter.add(ChatToItem(chatMessage?.text, chatMessage?.name, chatMessage?.timestamp))
                    //else display it as a message from another person
                    else
                        adapter.add(ChatFromItem(chatMessage?.text, chatMessage?.name, chatMessage?.timestamp))
                }

                //sets screen to view recent messages first
                recyclerview_cr1_messages.scrollToPosition(adapter.itemCount - 1)
            }


            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onChildChanged(p0: DataSnapshot, p1: String?) {

            }

            override fun onChildMoved(p0: DataSnapshot, p1: String?) {

            }

            override fun onChildRemoved(p0: DataSnapshot) {

            }
        })
    }

    private fun performSendMessage(){
        val text = edittxt_cr1_messageentry.text.toString()

        //prevents user from sending empty messages up to 3 spaces
        if (text != "" && text != " " && text != "   ") {

            //check each message for profanity
            /*since the list of profanity is all lower case, convert to lowercase in
            order to check against it*/
            val textlower = text.toLowerCase()
            val delimiter = " "
            val split = textlower.split(delimiter)

            //checks intersection of user's input vs list of profane words
            var intersection = split.intersect(profanity.toList()).toTypedArray()
            if (intersection.isNotEmpty()) {
                Toast.makeText(this, "Profanity detected. Please rephrase your message.", Toast.LENGTH_SHORT).show()
                return
            }


            //get id and display name from current user
            val instance = FirebaseAuth.getInstance()
            val sender_id = instance.currentUser?.email
            val name = instance.currentUser?.displayName

            //get current time
            val currentTime = LocalDateTime.now()
            val formatter = DateTimeFormatter.ofPattern("HH:mm")
            val currentTimeFormatted = currentTime.format(formatter).toString()


            if (sender_id == null) return

            val reference = FirebaseDatabase.getInstance().getReference("/chats/physicalbullyingchatroom").push()

            val chatMessage = ChatMessage(text, sender_id, name, currentTimeFormatted)
            reference.setValue(chatMessage)
                .addOnSuccessListener {
                    edittxt_cr1_messageentry.text.clear()
                }
        }
        else {
            Toast.makeText(this, "Type a message first!", Toast.LENGTH_SHORT).show()
            return
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
