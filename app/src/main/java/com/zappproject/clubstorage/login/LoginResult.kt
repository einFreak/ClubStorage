package com.zappproject.clubstorage.login

/**
 * Authentication result : success (user details) or error message.
 */
data class LoginResult(
    val success: LoggedInUserView? = null,
    val failure: String? = null,
    val error: Int? = null
)
