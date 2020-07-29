package com.example.fair2share

import org.json.JSONObject
import retrofit2.HttpException
import java.lang.StringBuilder

class Utils {
    companion object {
        fun formExceptionsToString(exception: HttpException): String{
            val sb = StringBuilder()
            val charStream = exception.response().errorBody()?.charStream()
            if (charStream != null){
                var json = JSONObject(charStream.readText())

                json = json.getJSONObject("errors")
                json.keys().forEach {
                    val array = json.getJSONArray(it)
                    for (i in 0 until array.length()){
                        sb.append(array.getString(i))
                        sb.append("\n")
                    }
                }
            }

            return sb.toString()
        }
    }
}