package com.moimpay.web.exception

import org.springframework.web.server.ResponseStatusException

class CustomException(val errorCode: ErrorCode) : ResponseStatusException(errorCode.httpStatus, errorCode.detail)