package com.example.labmanager.Service

import android.renderscript.Element
import java.math.BigInteger
import java.nio.charset.StandardCharsets
import java.security.MessageDigest

object Hash{
//    fun getHashedValue(string: String) : String{
//        val md = MessageDigest.getInstance("SHA-256")
//        // Change this to UTF-16 if needed
//        md.update(string.getBytes(StandardCharsets.UTF_8))
//        val digest = md.digest()
//        val hex = String.format("%064x", BigInteger(1, digest))
//        println(hex)
//        return hex
//    }

    fun hashString(string: String): String {
        val HEX_CHARS = "0123456789ABCDEF"
        val bytes = MessageDigest
            .getInstance("SHA-256")
            .digest(string.toByteArray())
        val result = StringBuilder(bytes.size * 2)

        bytes.forEach {
            val i = it.toInt()
            result.append(HEX_CHARS[i shr 4 and 0x0f])
            result.append(HEX_CHARS[i and 0x0f])
        }

        return result.toString()
    }
}