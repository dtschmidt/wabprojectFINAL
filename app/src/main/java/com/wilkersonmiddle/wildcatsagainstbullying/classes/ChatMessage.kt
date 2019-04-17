package com.wilkersonmiddle.wildcatsagainstbullying.classes

class ChatMessage(val text:String,
                  val sender_id:String?,
                  val name:String?,
                  val timestamp:String){

    constructor() : this("","","","")
}