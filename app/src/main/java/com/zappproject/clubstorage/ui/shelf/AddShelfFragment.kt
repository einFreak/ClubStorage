package com.zappproject.clubstorage.ui.shelf

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import com.zappproject.clubstorage.MainActivity
import com.zappproject.clubstorage.R
import com.zappproject.clubstorage.ViewModels.ShelfViewModel
import com.zappproject.clubstorage.database.Shelf.Shelf

class AddShelfFragment : Fragment(R.layout.fragment_add_shelf) {

    private lateinit var commitButton: Button
    private lateinit var newTitle: TextInputEditText
    private lateinit var newNote: TextInputEditText
    private lateinit var shelfTitle: TextView

    private val args: AddShelfFragmentArgs by navArgs()

    private val shelfViewModel: ShelfViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        commitButton = requireView().findViewById(R.id.btn_AddShelfCommit)
        newTitle = requireView().findViewById(R.id.txt_AddShelfTitle)
        newNote = requireView().findViewById(R.id.txt_AddShelfNote)
        shelfTitle = requireView().findViewById(R.id.txt_fragment_add_shelf_title)

        if (args.argTitle != null) {
            shelfTitle.text = getString(R.string.edit_shelf)
            commitButton.text = getString(R.string.save_shelf)
            newTitle.setText(args.argTitle)
            newNote.setText(args.argNote)
        }
        setupButtonOnClickListener(view)
    }

    private fun setupButtonOnClickListener(view: View) {
        commitButton.setOnClickListener {

            val title = newTitle.text.toString().trim()
            val note = newNote.text.toString().trim()

            when {
                title.isEmpty() -> { // Error handling
                    Snackbar.make(
                        view,
                        getString(R.string.error_add_shelf_empty_field),
                        Snackbar.LENGTH_LONG
                    ).show()
                }
                args.argTitle == null -> { // create a shelf
                    shelfViewModel.insert(Shelf(title, note))

                    Snackbar.make(view, getString(R.string.add_a_new_shelf), Snackbar.LENGTH_LONG)
                        .show()
                    newTitle.setText("")    // clear the editTextView
                    newNote.setText("")     // clear the editTextView
                }
                else -> {   // edit a shelf
                    shelfViewModel.update(Shelf(args.argId, title, note))
                    Snackbar.make(view, getString(R.string.edit_a_shelf), Snackbar.LENGTH_SHORT)
                        .show()
                    (activity as MainActivity).hideSoftKeyBoard(view)
                    (activity as MainActivity).goBack()
                }
            }
        }
    }
}
