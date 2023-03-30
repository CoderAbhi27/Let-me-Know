package com.example.letmeknow.DataClass

data class PollDataClass(
    var uid : String?= null,
    var PollQues: String?= null,
    var QuesImg: String? = null,
    var isMulti: Boolean = false,
    var isQuiz: Boolean = false,
    var Options: ArrayList<String> = ArrayList<String>(),
    var Votes: ArrayList<Int> = ArrayList<Int>(),
    var endTimeMilli : Long = 0L,
    var votedUid: ArrayList<String> = ArrayList<String>()
)