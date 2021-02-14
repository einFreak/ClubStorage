package com.zappproject.clubstorage.ui.article

import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import com.zappproject.clubstorage.MainActivity
import com.zappproject.clubstorage.R
import com.zappproject.clubstorage.ViewModels.ItemViewModel
import com.zappproject.clubstorage.database.Item.Item

class AddArticleFragment : Fragment(R.layout.fragment_add_article) {

    private lateinit var spinner: Spinner
    private lateinit var commitButton: Button
    private lateinit var newTitle: TextInputEditText
    private lateinit var newPrice: TextInputEditText
    private lateinit var newNote: TextInputEditText
    private lateinit var titel: TextView

    private val args: AddArticleFragmentArgs by navArgs()

    private val itemViewModel: ItemViewModel by viewModels()

    class SpinnerObject(val value: Int, val name: String) {
        override fun toString(): String {
            return name
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        spinner = requireView().findViewById(R.id.sp_chooseUnit)
        commitButton = requireView().findViewById(R.id.btn_add_article_commit)
        newTitle = requireView().findViewById(R.id.txt_add_title)
        newPrice = requireView().findViewById(R.id.txt_AddPrice)
        newNote = requireView().findViewById(R.id.txt_AddNote)
        titel = requireView().findViewById(R.id.txt_fragment_add_title)

        if (args.title != null) { // if the title isn't NULL, an article has to edit.
            // In this case the textViews have to fill with the data from args
            titel.text = getString(R.string.edit_article)
            commitButton.text = getString(R.string.save_article)
            newTitle.setText(args.title)
            newPrice.setText(args.price)
            newNote.setText(args.note)
        }
        setupSpinner()
        setupButtonOnClickListener(view)
    }

    private fun setupButtonOnClickListener(view: View) {
        commitButton.setOnClickListener {
            val title = newTitle.text.toString().trim()
            val price = newPrice.text.toString().trim()
            val note = newNote.text.toString().trim()
            val unit = spinner.selectedItemPosition

            if (title.isEmpty()) {   // if a field is empty
                Snackbar.make(
                    view,
                    resources.getString(R.string.error_add_article_empty_field),
                    Snackbar.LENGTH_LONG
                ).show()
            } else if (price.isEmpty()) {
                Snackbar.make(
                    view,
                    resources.getString(R.string.error_add_article_empty_field),
                    Snackbar.LENGTH_LONG
                ).show()
            } else if (args.title == null) {    // If a new Article is created
                val newArticle = Item(title, price.toDouble(), note, unit)
                itemViewModel.insert(newArticle)
                Snackbar.make(
                    view,
                    resources.getString(R.string.add_a_new_Article),
                    Snackbar.LENGTH_LONG
                ).show()
                newTitle.setText("")
                newPrice.setText("")
                newNote.setText("")
            } else {    // if an existing article is updated
                val editArticle = Item(args.id, title, price.toDouble(), note, unit)
                itemViewModel.update(editArticle)
                Snackbar.make(
                    view,
                    resources.getString(R.string.edit_a_Article),
                    Snackbar.LENGTH_LONG
                ).show()
                (activity as MainActivity).hideSoftKeyBoard(view)
                (activity as MainActivity).goBack()
            }
        }
    }

    private fun setupSpinner() {
        // For the spinner-values
        val names = resources.getStringArray(R.array.Units_string_array)
        val unitList = ArrayList<SpinnerObject>()
        for (i in names.indices) {
            unitList.add(SpinnerObject(i, names[i]))
        }

        val adapter: ArrayAdapter<SpinnerObject> =
            ArrayAdapter(
                this.requireContext(),
                android.R.layout.simple_spinner_item,
                unitList
            )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter

        if (args.title != null) {
            spinner.setSelection(args.unit) // Set the spinner-value from the database if the article is edit
        }
    }
}
