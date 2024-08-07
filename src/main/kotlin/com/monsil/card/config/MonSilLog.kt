package com.monsil.card.config

import org.slf4j.Logger
import org.slf4j.LoggerFactory

interface MonSilLog {
    val log: Logger
        get() = LoggerFactory.getLogger(this.javaClass)
}