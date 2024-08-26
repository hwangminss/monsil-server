package com.monsil.card.handler.dto

data class FamilyDTO(
    var role: Int,
    var name: String,
    var bank: String,
    var account: String,
    var kakao: String?,
    var phone: String,
    var message: String,
    var groombride: Boolean,
)