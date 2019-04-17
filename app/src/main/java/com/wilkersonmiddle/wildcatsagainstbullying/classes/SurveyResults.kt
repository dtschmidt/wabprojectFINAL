package com.wilkersonmiddle.wildcatsagainstbullying.classes

class SurveyResults(var q1avg:String,
                    var q1responses:String,
                    var q1runningsum:String,
                    var q2avg:String,
                    var q2responses:String,
                    var q2runningsum:String,
                    var q3avg:String,
                    var q3responses:String,
                    var q3runningsum:String,
                    var q4avg:String,
                    var q4responses:String,
                    var q4runningsum:String,
                    var q5avg:String,
                    var q5responses:String,
                    var q5runningsum:String
                    ){

    constructor() : this("","","","","", "", "",
        "", "", "", "", "", "", "", "")
}