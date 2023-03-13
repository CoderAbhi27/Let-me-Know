package com.example.letmeknow.DataClass

data class PollDataClass(
    var PollID : String?= null,
    var PollQues : String?= null,
    var isMulti : Boolean = false,
    var isQuiz : Boolean = false,
    var Options : ArrayList<String> = ArrayList<String>()
)