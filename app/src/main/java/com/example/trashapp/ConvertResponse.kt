package com.example.trashapp

import com.example.trashapp.classes.CleaningCompany
import com.example.trashapp.classes.Role
import com.example.trashapp.classes.User

object ConvertResponse {
    fun convertCompanies(str: String) : ArrayList<CleaningCompany> {
        val companies = str.split("\n")
        val out: ArrayList<CleaningCompany> = arrayListOf()
        for (company in companies){
            if(!company.isEmpty()) {
                val attributes = company.split(";")
                out.add(
                    CleaningCompany(
                        NIP = attributes[0],
                        email = attributes[1],
                        phone = attributes[2].toInt(),
                        country = attributes[3],
                        city = attributes[4],
                        street = attributes[5]
                    )
                )
            }
        }
        return out
    }
    fun convertAllUsers(str: String) : ArrayList<User> {
        val users = str.split("\n")
        val out: ArrayList<User> = arrayListOf()
        for (user in users){
            if(!user.isEmpty()) {
                val attributes = user.split(";")
                out.add(
                    User(
                        login = attributes[0],
                        password = attributes[1],
                        email = attributes[2],
                        phone = attributes[3],
                        fullname = attributes[4],
                        roles = Role(attributes[5])
                    )
                )
            }
        }
        return out
    }
}