package com.example.fair2share

import org.json.JSONObject
import retrofit2.HttpException
import java.lang.StringBuilder

class Utils {
    companion object Companion {
        fun formExceptionsToString(excetion: HttpException): String{
            var json = JSONObject(excetion.response().errorBody()!!.string())
            val sb = StringBuilder()
            json = json.getJSONObject("errors")
            json.keys().forEach {
                val array = json.getJSONArray(it)
                for (i in 0 until array.length()){
                    sb.append(array.getString(i))
                    sb.append("\n")
                }
            }
            return sb.toString()
        }
    }
}