package com.example.trashapp

import android.util.Base64.*
import java.util.*
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.SecretKeySpec

object Encryption {

    private val key: SecretKey = KeyGenerator.getInstance("AES").generateKey()
    private val cipher: Cipher = Cipher.getInstance("AES")

    fun encrypt(password: String): String {
        cipher.init(Cipher.ENCRYPT_MODE, key)
        val encrypted = cipher.doFinal(password.toByteArray())
        return encodeToString(encrypted, DEFAULT)
    }

    fun decrypt(password: String): String {
        cipher.init(Cipher.DECRYPT_MODE, key)
        val decrypted = cipher.doFinal(decode(password, DEFAULT))
        return String(decrypted)
    }
}