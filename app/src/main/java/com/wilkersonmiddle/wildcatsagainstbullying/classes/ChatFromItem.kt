package com.wilkersonmiddle.wildcatsagainstbullying.classes

import com.wilkersonmiddle.wildcatsagainstbullying.R
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.chat_from_row.view.*

class ChatFromItem(val text: String, val username:String?, val time:String): Item<ViewHolder>() {
    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.itemView.txtview_fromrow_message.text = text
        viewHolder.itemView.txtview_fromrow_username.text = username
        viewHolder.itemView.txtview_fromrow_time.text = time
    }

    override fun getLayout(): Int {
        return R.layout.chat_from_row
    }
}