package com.zappproject.clubstorage.login

import android.app.Application
import android.util.Log
import android.util.Patterns
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.zappproject.clubstorage.R
import com.zappproject.clubstorage.database.Repository
import com.zappproject.clubstorage.database.User.User
import java.io.IOException

class LoginViewModel(application: Application) : AndroidViewModel(application) {

    private val _loginForm = MutableLiveData<LoginFormState>()
    val loginFormState: LiveData<LoginFormState> = _loginForm

    private val _loginResult = MutableLiveData<LoginResult>()
    val loginResult: LiveData<LoginResult> = _loginResult

    private var repository: Repository? = null

    init {
        repository = Repository(application)
    }

    fun login(username: String, password: String) {
        // can be launched in a separate asynchronous job
        val result = checkLogin(username, password)

        if (result is Result.Success) {
            _loginResult.value = LoginResult(
                success = LoggedInUserView(
                    firstName = result.userdata.firstname,
                    lastName = result.userdata.lastname,
                    mail = result.userdata.email,
                    permission = result.userdata.permission
                )
            )
        } else if (result is Result.Failure) {
            _loginResult.value = LoginResult(
                failure = result.failure
            )
        } else {
            _loginResult.value = LoginResult(error = R.string.login_failed)
        }
    }

    fun loginDataChanged(username: String, password: String) {
        if (!isUserNameValid(username)) {
            _loginForm.value = LoginFormState(usernameError = R.string.invalid_username)
        } else if (!isPasswordValid(password)) {
            _loginForm.value = LoginFormState(passwordError = R.string.invalid_password)
        } else {
            _loginForm.value = LoginFormState(isDataValid = true)
        }
    }

    // A placeholder username validation check
    private fun isUserNameValid(username: String): Boolean {
        return if (username.contains('@')) {
            Patterns.EMAIL_ADDRESS.matcher(username).matches()
        } else {
            username.isNotBlank()
        }
    }

    // A placeholder password validation check
    @SuppressWarnings("MagicNumber")
    private fun isPasswordValid(password: String): Boolean {
        return password.length > 5
    }

    @SuppressWarnings("ReturnCount", "exceptions")
    private fun checkLogin(username: String, password: String): Result<User> {
        try {
            val repository = Repository(getApplication())
            val user = repository.getUser(username)

            /* TODO(Felix): remove before release,
              default user for login that is contained
              in dummy data in StorageRoomDatabase */
            if (username == "test") {
                val testingUser = repository.getUser("fw@cs.com")
                Toast.makeText(
                    getApplication(),
                    "for testing only",
                    Toast.LENGTH_LONG
                ).show()
                Log.i("login", "jumped in test account")
                return Result.Success(testingUser)
            }

            if (username == user?.email) {
                return if (password == user.password) {
                    Result.Success(user)
                } else Result.Failure("Wrong Password")
            }

            if (username != user?.email) {
                return Result.Failure("User not found")
            }

            val error = Throwable("unknown login error")
            return Result.Error(IOException("Error logging in", error))
        } catch (e: Throwable) {
            return Result.Error(IOException("Error logging in", e))
        }
    }
}
