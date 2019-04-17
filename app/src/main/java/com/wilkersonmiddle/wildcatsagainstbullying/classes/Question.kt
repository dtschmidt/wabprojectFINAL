package com.wilkersonmiddle.wildcatsagainstbullying.classes

class Question(val actualQuestion:String,
            val option1:String,
           val option2:String,
           val option3:String?,
           val option4:String?,
           val correctAnswer:String?){

    constructor() : this("","","","","", "")
}