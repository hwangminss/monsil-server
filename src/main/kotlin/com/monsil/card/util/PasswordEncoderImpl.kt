package com.monsil.card.util

import org.springframework.security.crypto.bcrypt.BCrypt
import java.security.SecureRandom

class PasswordEncoderImpl {
    companion object : PasswordEncoder {
        val randomStrength = 10

        // 최초에 한번만 쓰임
        override fun initEncode(rawPassword: CharSequence?): Map<String, String> {
            val secureRandom = SecureRandom()
            val salt = if (randomStrength > 0) {
                BCrypt.gensalt(randomStrength, secureRandom)
            } else {
                BCrypt.gensalt()
            }

            val hashpw = BCrypt.hashpw(rawPassword.toString(), salt)
            return mapOf("salt" to salt, "hashpw" to hashpw)
        }

        // 비밀번호 인코더
        override fun encode(rawPassword: CharSequence?, salt: String): String? {
            return BCrypt.hashpw(rawPassword.toString(), salt)
        }

        // encoded : db에서 가져온 해시값
        override fun matches(inputHashpw: String?, hashpw: String?): Boolean {
            return BCrypt.checkpw(inputHashpw, hashpw)
        }
    }
}
