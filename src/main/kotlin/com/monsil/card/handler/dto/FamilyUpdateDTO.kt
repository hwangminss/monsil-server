package com.monsil.card.handler.dto

data class FamilyUpdateDTO(
    var id: String,
    var bank: String,
    var account: String,
    var kakao: String?,
    var phone: String,
    var message: String,
)