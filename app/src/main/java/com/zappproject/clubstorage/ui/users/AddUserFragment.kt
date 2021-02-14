package com.zappproject.clubstorage.ui.users

import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.zappproject.clubstorage.R
import com.zappproject.clubstorage.SpinnerObject
import com.zappproject.clubstorage.ViewModels.UserViewModel
import com.zappproject.clubstorage.database.User.User

class AddUserFragment : Fragment(R.layout.fragment_add_user) {

    private lateinit var spinner: Spinner
    private lateinit var commitButton: Button
    private lateinit var newFirstName: TextInputEditText
    private lateinit var newLastName: TextInputEditText
    private lateinit var newEmail: TextInputEditText
    private lateinit var newPassword: TextInputEditText
    private lateinit var newPasswordRepeat: TextInputEditText

    private val userViewModel: UserViewModel by viewModels()
    companion object {
        @SuppressWarnings
        private const val minPasswordLength = 5
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        spinner = requireView().findViewById(R.id.sp_choosePermission)
        commitButton = requireView().findViewById(R.id.btn_updateUserData)
        newFirstName = requireView().findViewById(R.id.txt_FirstName)
        newLastName = requireView().findViewById(R.id.txt_LastName)
        newEmail = requireView().findViewById(R.id.txt_Mail)
        newPassword = requireView().findViewById(R.id.txt_changePassword)
        newPasswordRepeat = requireView().findViewById(R.id.txt_ChangePasswordRepeat)

        setupSpinner()
        setupButtonOnClickListener(view)
    }

    private fun setupButtonOnClickListener(view: View) {
        commitButton.setOnClickListener {

            val firstName = newFirstName.text.toString().trim()
            val lastName = newLastName.text.toString().trim()
            val email = newEmail.text.toString().trim()
            val password = newPassword.text.toString().trim()
            val passwordRepeat = newPasswordRepeat.text.toString().trim()
            val permission = spinner.selectedItemPosition

            val nameChecked = checkName(firstName = firstName, lastName = lastName, email = email)
            val pwChecked = checkPW(password = password, passwordRepeat = passwordRepeat)

            if (nameChecked || pwChecked) {
                Snackbar.make(view, getString(R.string.user_insufficient_input), Snackbar.LENGTH_SHORT)
                    .show()
            } else {
                val newUser = User(firstName, lastName, email, password, permission)
                userViewModel.insert(newUser)
                Snackbar.make(
                    view,
                    resources.getString(R.string.add_a_new_user),
                    Snackbar.LENGTH_SHORT
                ).show()
            }
        }
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

        ePassword.error = when {
            password.isEmpty() -> getString(R.string.required)
            password.length <= minPasswordLength -> getString(R.string.user_pw_length)
            else -> null
        }
        ePasswordRepeat.error = when {
            passwordRepeat.isEmpty() -> getString(R.string.required)
            password != passwordRepeat -> getString(R.string.user_pw_compare)
            else -> null
        }

        return when {
            password.isEmpty() || passwordRepeat.isEmpty() -> true
            password != passwordRepeat || password.length <= minPasswordLength -> true
            else -> false
        }
    }

    private fun setupSpinner() {
        // For the spinnervalues
        val names = resources.getStringArray(R.array.permission_string_array)
        val permissionList = ArrayList<SpinnerObject>()

        for (i in names.indices) {
            permissionList.add(SpinnerObject(i, names[i]))
        }

        val adapter: ArrayAdapter<SpinnerObject> = ArrayAdapter(
            this.requireContext(),
            android.R.layout.simple_spinner_item,
            permissionList
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter
    }
}
