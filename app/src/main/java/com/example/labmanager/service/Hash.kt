package com.example.labmanager.service

import com.example.labmanager.HEX_CHARS
import com.example.labmanager.SHA_256
import java.security.MessageDigest

object Hash{

    fun hashString(string: String): String {
        val bytes = MessageDigest
            .getInstance(SHA_256)
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