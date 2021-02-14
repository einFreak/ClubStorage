package com.zappproject.clubstorage.ui.shelf

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Filter
import android.widget.Filterable
import android.widget.TextView
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.zappproject.clubstorage.R
import com.zappproject.clubstorage.database.Shelf.ShelfWithNumber
import java.util.Locale
import kotlin.collections.ArrayList

class ShelfAdapter : RecyclerView.Adapter<ShelfAdapter.ShelfViewHolder>(), Filterable {

    private var shelves: List<ShelfWithNumber> = ArrayList()
    private var shelvesFiltered: List<ShelfWithNumber> = ArrayList()

    fun setShelf(shelves: List<ShelfWithNumber>) {
        this.shelves = shelves
        this.shelvesFiltered = shelves
        notifyDataSetChanged()
    }

    fun getShelfAt(position: Int): ShelfWithNumber {
        return shelvesFiltered[position]
    }

    // sets RecyclerView Object Layout (here rv_layout_article) and inflates it
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShelfViewHolder {
        return ShelfViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.rv_layout_shelf, parent, false)
        )
    }

    // needed for handling data
    override fun getItemCount() = shelvesFiltered.size

    // sets given Data to according layout rv_layout_article
    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ShelfViewHolder, position: Int) {
        val currentShelf = shelvesFiltered[position]

        holder.textViewTitle.text = currentShelf.shelf.title
        holder.textViewNote.text = currentShelf.shelf.note
        val suffix = holder.itemView.context.getString(R.string.shelf_counter_suffix)
        holder.textViewNumber.text = currentShelf.number.toString() + suffix

        // Hide button if permission is insufficient
        val sharedPreferences = holder.itemView.context.getSharedPreferences("userdata", Context.MODE_PRIVATE) ?: return
        val permission = sharedPreferences.getInt("permission", 0)
        if (permission < 2) {
            holder.buttonShelfOptions.visibility = View.GONE
        }

        // Manage navigation for adding an article zu shelf and for edit the shelf
        val actionShelfDetails =
            ShelfFragmentDirections.actionNavShelfToShelfDetails(
                currentShelf.shelf.sId,
                currentShelf.shelf.title
            )

        val actionShelfEdit = ShelfFragmentDirections.actionNavShelfToAddShelfFragment(
                holder.itemView.context.getString(R.string.edit_shelf),
                currentShelf.shelf.sId,
                currentShelf.shelf.title,
                currentShelf.shelf.note
        )
        // set the onClickListeners for the tow buttons
        holder.buttonShelf.setOnClickListener(Navigation.createNavigateOnClickListener(actionShelfDetails))
        holder.buttonShelfOptions.setOnClickListener(Navigation.createNavigateOnClickListener(actionShelfEdit))
    }

    class ShelfViewHolder(shelfView: View) : RecyclerView.ViewHolder(shelfView) {
        val textViewTitle: TextView = shelfView.findViewById(R.id.Shelf_Name)
        val textViewNote: TextView = shelfView.findViewById(R.id.Shelf_Note)
        val textViewNumber: TextView = shelfView.findViewById(R.id.Shelf_NumberOfArticles)
        val buttonShelf: Button = shelfView.findViewById(R.id.Shelf_button)
        val buttonShelfOptions: TextView = shelfView.findViewById(R.id.Btn_Shelf_Options)
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val charSearch = constraint.toString()
                if (charSearch.isEmpty()) {
                    shelvesFiltered = shelves
                } else {
                    val results: MutableList<ShelfWithNumber> = ArrayList()

                    for (row in shelves) {
                        if (row.shelf.title.toLowerCase(Locale.ROOT)
                                .contains(charSearch.toLowerCase(Locale.ROOT))
                        ) {
                            results.add(row)
                        }
                    }

                    shelvesFiltered = results
                }
                val filterResult = FilterResults()
                filterResult.values = shelvesFiltered
                return filterResult
            }

            @Suppress("UNCHECKED_CAST")
            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                shelvesFiltered = results?.values as List<ShelfWithNumber>
                notifyDataSetChanged()
            }
        }
    }
}
