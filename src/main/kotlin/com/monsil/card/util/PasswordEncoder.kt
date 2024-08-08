package com.monsil.card.util

interface PasswordEncoder {

    // 최초 가입시 패스워드 해싱값
    fun initEncode(var1: CharSequence?): Map<String, Any>?

    // 패스워드 해싱
    fun encode(rawPassword: CharSequence?, salt: String) : String?

    // 패스워드 비교
    fun matches(va1: String?, var2: String?): Boolean

    fun upgradeEncoding(encodedPassword: String?): Boolean {
        return false
    }
}