package com.example.trashapp

import com.google.gson.Gson
import com.google.gson.JsonObject

object LocalizationToAddress {

    val gson = Gson()

    fun getAddress(json: String?): String{
        val jsonObject = gson.fromJson(json, JsonObject::class.java)
        var addrString= ""
        if (jsonObject.get("address").asJsonObject.has("amenity")){
            addrString += jsonObject.get("address").asJsonObject.get("amenity").asString + ", "
        }
        addrString += jsonObject.get("address").asJsonObject.get("road").asString
        if (jsonObject.get("address").asJsonObject.has("house_number")){
            addrString += " " + jsonObject.get("address").asJsonObject.get("house_number").asString
        }
        return addrString
    }
}