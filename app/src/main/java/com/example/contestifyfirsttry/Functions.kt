package com.example.contestifyfirsttry

import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

class Functions {
    fun getTime(timestamp:String): String? {
        var parsedDate = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.ENGLISH).parse(timestamp)
        var diff = Date().time - parsedDate.time
        var displayTimeDay = SimpleDateFormat("dd",Locale.ENGLISH).format(diff)
        return if (displayTimeDay > "00"){
            displayTimeDay+"d"
        }else{
            displayTimeDay = SimpleDateFormat("HH:mm",Locale.ENGLISH).format(parsedDate)
            displayTimeDay
        }
        //var displayTime = SimpleDateFormat("dd'T'HH",Locale.ENGLISH).format(diff)

    }
    fun msToMin(ms:Int):String{
        return String.format("%02d:%02d",
            TimeUnit.MILLISECONDS.toMinutes(ms.toLong()),
            TimeUnit.MILLISECONDS.toSeconds(ms.toLong()) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(ms.toLong()))
        )
    }

    fun trackKey(key:Int):String{
        var k :String? = null
        when (key){
            0 -> k = "C"
            1 ->  k =  "C#"
            2 ->  k =  "D"
            3 ->  k =  "D#"
            4 ->  k =  "E"
            5 ->  k =  "F"
            6 ->  k =  "F#"
            7 ->  k =  "G"
            8 ->  k =  "G#"
            9 ->  k =  "A"
            10 ->  k =  "A#"
            11 ->  k =  "B"
        }
        return k!!
    }
    fun trackMode(mode:Int):String{
        var m: String? = null
        when(mode){
            0 -> m = "m"
            1 -> m = ""
        }
        return m!!
    }
}