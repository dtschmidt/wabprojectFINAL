package com.wilkersonmiddle.wildcatsagainstbullying.classes

import com.google.firebase.auth.FirebaseAuth
import com.wilkersonmiddle.wildcatsagainstbullying.R
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.chat_to_row.view.*

class ChatToItem(val text: String, val username:String?, val time:String): Item<ViewHolder>() {
    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.itemView.txtview_torow_message.text = text
        viewHolder.itemView.txtview_torow_username.text = FirebaseAuth.getInstance().currentUser?.displayName
        viewHolder.itemView.txtview_torow_time.text = time
    }

    override fun getLayout(): Int {
        return R.layout.chat_to_row
    }
}