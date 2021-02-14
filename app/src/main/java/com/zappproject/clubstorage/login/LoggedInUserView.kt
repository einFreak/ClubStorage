package com.zappproject.clubstorage.login

/**
 * User details post authentication that is exposed to the UI
 */
data class LoggedInUserView(
    val firstName: String,
    val lastName: String,
    val mail: String,
    val permission: Int
    // ... other data fields that may be accessible to the UI
)
