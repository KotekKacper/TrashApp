package com.example.trashapp

import com.example.trashapp.classes.CleaningCompany

object ConvertResponse {
    fun convertCompanies(str: String) : ArrayList<CleaningCompany> {
        val companies = str.split("\n")
        val out: ArrayList<CleaningCompany> = arrayListOf()
        for (company in companies){
            val attributes = company.split(";")
            out.add(
                CleaningCompany(
                    email = attributes[0],
                    NIP = attributes[1],
                    phone = attributes[2].toInt(),
                    country = attributes[3],
                    city = attributes[4],
                    street = attributes[5]
                )
            )
        }
        return out
    }
}