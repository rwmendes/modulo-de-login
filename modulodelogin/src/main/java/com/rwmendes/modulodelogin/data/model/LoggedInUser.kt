package com.rwmendes.modulodelogin.data.model

import java.util.UUID

data class LoggedInUser(
    val userId: String = UUID.randomUUID().toString(),
    val username: String
)
