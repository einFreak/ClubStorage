package com.zappproject.clubstorage.ui.settings

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.zappproject.clubstorage.R
import com.zappproject.clubstorage.ViewModels.UserViewModel
import com.zappproject.clubstorage.database.User.User

class SettingsFragment : Fragment() {

    private lateinit var settingsViewModel: SettingsViewModel
    private val userViewModel: UserViewModel by viewModels()
    private var user: String? = null

    private lateinit var fieldFirstname: TextInputEditText
    private lateinit var fieldLastname: TextInputEditText
    private lateinit var fieldEMail: TextInputEditText
    private lateinit var fieldPermission: TextView
    private lateinit var fieldOldPassword: TextInputEditText
    private lateinit var fieldChangePasswordRepeat: TextInputEditText
    private lateinit var fieldChangePassword: TextInputEditText
    private lateinit var submitButton: Button

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        settingsViewModel = ViewModelProvider(this).get(SettingsViewModel::class.java)
        return inflater.inflate(R.layout.fragment_settings, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val sharedPref = activity?.getSharedPreferences("userdata", Context.MODE_PRIVATE)
        user = sharedPref?.getString("user", "error")

        fieldFirstname = view.findViewById(R.id.txt_FirstName)
        fieldLastname = view.findViewById(R.id.txt_LastName)
        fieldEMail = view.findViewById(R.id.txt_Mail)
        fieldPermission = view.findViewById(R.id.txt_permission)
        fieldOldPassword = view.findViewById(R.id.txt_oldPassword)
        fieldChangePasswordRepeat = view.findViewById(R.id.txt_ChangePasswordRepeat)
        fieldChangePassword = view.findViewById(R.id.txt_changePassword)
        submitButton = view.findViewById(R.id.btn_updateUserData)

        val userData: User? = userViewModel.getUser(user)
        if (userData != null) {
            submitButton.isEnabled = true
            val permission = arrayOf("no Access", "read-only", "read/write", "Admin")
            fieldFirstname.setText(userData.firstname)
            fieldLastname.setText(userData.lastname)
            fieldEMail.setText(userData.email)
            fieldPermission.text = permission[userData.permission]

            setupButtonOnClickListener(view, userData)
        }
    }

    private fun setupButtonOnClickListener(view: View, userData: User) {
        submitButton.setOnClickListener({

            userData.firstname = fieldFirstname.text.toString().trim()
            userData.lastname = fieldLastname.text.toString().trim()
            userData.email = fieldEMail.text.toString().trim()

            val oldPassword = fieldOldPassword.text.toString().trim()
            val changePasswordRepeat = fieldChangePasswordRepeat.text.toString().trim()
            val changePassword = fieldChangePassword.text.toString().trim()

            val nameChecked = checkName(userData.firstname, userData.lastname, userData.email)
            var pwChecked = false
            if (oldPassword.isNotEmpty()) {
                pwChecked = true
                if (oldPassword == userData.password) {
                    pwChecked = checkPW(password = changePassword, passwordRepeat = changePasswordRepeat)
                } else {
                    val eOldPassword = view.findViewById<TextInputLayout>(R.id.layout_oldPassword)
                    eOldPassword.error = "Falsches Passwort"
                }
            }

            if (nameChecked || pwChecked) {
                Snackbar.make(view, getString(R.string.user_insufficient_input), Snackbar.LENGTH_SHORT)
                    .show()
            } else {
                if (!pwChecked) userData.password = changePassword
                userViewModel.update(userData)
                Snackbar.make(
                    view,
                    "Daten geändert",
                    Snackbar.LENGTH_SHORT
                ).show()
            }
        })
    }

    private fun checkName(firstName: String, lastName: String, email: String): Boolean {
        val eFirstName = requireView().findViewById<TextInputLayout>(R.id.layout_FirstName)
        val eLastName = requireView().findViewById<TextInputLayout>(R.id.layout_LastName)
        val eMail = requireView().findViewById<TextInputLayout>(R.id.layout_Mail)

        if (firstName.isEmpty()) eFirstName.error = getString(R.string.required) else eFirstName.error = null
        if (lastName.isEmpty()) eLastName.error = getString(R.string.required) else eLastName.error = null
        if (email.isEmpty()) eMail.error = getString(R.string.required) else eMail.error = null

        return (firstName.isEmpty() ||
                lastName.isEmpty() ||
                email.isEmpty())
    }

    private fun checkPW(password: String, passwordRepeat: String): Boolean {
        val ePasswordRepeat = requireView().findViewById<TextInputLayout>(R.id.layout_ChangePasswordRepeat)
        val ePassword = requireView().findViewById<TextInputLayout>(R.id.layout_changePassword)

        @SuppressWarnings("MagicNumber")
        val minLength = 5

        ePassword.error = when {
            password.isEmpty() -> getString(R.string.required)
            password.length <= minLength -> getString(R.string.user_pw_length)
            else -> null
        }
        ePasswordRepeat.error = when {
            passwordRepeat.isEmpty() -> getString(R.string.required)
            password != passwordRepeat -> getString(R.string.user_pw_compare)
            else -> null
        }

        return when {
            password.isEmpty() || passwordRepeat.isEmpty() -> true
            password != passwordRepeat || password.length <= minLength -> true
            else -> false
        }
    }
}
