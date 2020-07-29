package com.example.fair2share

import android.widget.TableRow
import org.json.JSONException
import org.json.JSONObject
import retrofit2.HttpException
import java.lang.StringBuilder

class Utils {
    companion object {
        fun formExceptionsToString(exception: HttpException, customMessage: String? = null): String{
            val sb = StringBuilder()
            val charStream = exception.response().errorBody()?.charStream()
            if (charStream != null){
                var json = JSONObject(charStream.readText())
                try {
                    json = json.getJSONObject("errors")
                    json.keys().forEach {
                        val array = json.getJSONArray(it)
                        for (i in 0 until array.length()){
                            sb.append(array.getString(i))
                            sb.append("\n")
                        }
                    }
                } catch (e: JSONException){
                    if (customMessage != null){
                        sb.append(customMessage)
                    } else {
                        sb.append(exception.message())
                    }
                }
            }

            return sb.toString()
        }
    }
}