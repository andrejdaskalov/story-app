package com.adaskalov.storyapp.data.model

import android.util.Log
import com.google.gson.Gson
import org.json.JSONObject

class ModelResponse(
    val response: String,
    val actions: List<String>

) {
    companion object {
        fun fromJson(json: String): ModelResponse {
            Log.d("ModelResponse", "fromJson: $json")
            val gson = Gson()
            val openingBrace = json.indexOf("{")
            val closingBrace = json.lastIndexOf("}")
            if (openingBrace == -1 || closingBrace == -1) {
                return ModelResponse("", emptyList())
            }
            val jsonFixed = json.substring(openingBrace, closingBrace + 1)
            try {
                val jsonObject = gson.fromJson(jsonFixed, ModelResponse::class.java)
                Log.d("C", "fromJson: $jsonObject")

                return jsonObject

            } catch (e: Exception) {
                Log.e("ModelResponseError", "fromJson: $e")
                return ModelResponse("", emptyList())
            }
        }
    }
}