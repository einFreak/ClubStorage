package com.zappproject.clubstorage.login

/**
 * A generic class that holds a value with its loading status.
 * @param <T>
 */
sealed class Result<out T : Any> {

    data class Success<out T : Any>(val userdata: T) : Result<T>()
    data class Error(val exception: Exception) : Result<Nothing>()
    data class Failure(val failure: String) : Result<Nothing>()

    override fun toString(): String {
        return when (this) {
            is Success<*> -> "Success[data=$userdata]"
            is Error -> "Error[exception=$exception]"
            is Failure -> failure
        }
    }
}
