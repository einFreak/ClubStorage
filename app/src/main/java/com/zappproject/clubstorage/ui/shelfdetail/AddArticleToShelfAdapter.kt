package com.zappproject.clubstorage.ui.shelfdetail

import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.zappproject.clubstorage.R
import com.zappproject.clubstorage.database.Contains.Contains
import com.zappproject.clubstorage.database.Item.Item
import com.zappproject.clubstorage.database.Item.ItemWithNumber
import java.util.*
import kotlin.collections.ArrayList

class AddArticleToShelfAdapter :
    RecyclerView.Adapter<AddArticleToShelfAdapter.ShelfViewHolder>() {

    private var articles: List<ItemWithNumber> = ArrayList()
    private lateinit var changes: Vector<ContainsChange>

    fun setArticles(articles: List<ItemWithNumber>, shelfId: Int) {
        this.articles = articles
        changes = Vector()
        for (item in articles) {
            changes.add(ContainsChange(Contains(shelfId, item.item.iId, item.number), -1))
        }
    }

    fun getChanges(): Vector<ContainsChange> {
        return changes
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ShelfViewHolder {
        return ShelfViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.rv_layout_choose_article, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return articles.size
    }

    override fun onBindViewHolder(holder: ShelfViewHolder, position: Int) {

        holder.textViewArticle.text =
            articles[position].item.title + " [in " + Item.UNITS[articles[position].item.unit] + "]"
        if (articles[position].item.unit == 0) {
            holder.editTextCurrentNumber.setText(articles[position].number.toInt().toString())
        } else {
            holder.editTextCurrentNumber.setText(articles[position].number.toString())
        }
        holder.editTextCurrentNumber.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
                val numberStr: String = holder.editTextCurrentNumber.text.toString()
                if (numberStr.isNotEmpty()) {
                    when {
                        numberStr.toDouble() == articles[position].number -> {
                            changes[position].setChange(-1) // no changes
                        }
                        numberStr.toDouble() == 0.0 -> {
                            changes[position].setChange(0) // to delete
                        }
                        articles[position].number == 0.0 -> {
                            changes[position].setChange(1) // to create
                            changes[position].getContains().number = numberStr.toDouble()
                        }
                        else -> {
                            changes[position].setChange(2) // to update
                            changes[position].getContains().number = numberStr.toDouble()
                        }
                    }
                }
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                return
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                return
            }
        })

        holder.minusButton.setOnClickListener {
            if (articles[position].item.unit == 0) {
                val number: Int = holder.editTextCurrentNumber.text.toString().toDouble().toInt()
                if (number >= 1) {
                    holder.editTextCurrentNumber.setText((number - 1).toString())
                } else {
                    holder.editTextCurrentNumber.setText((0).toString())
                }
            } else {
                val number = holder.editTextCurrentNumber.text.toString().toDouble()

                if (number >= 1) {
                    holder.editTextCurrentNumber.setText((number - 1).toString())
                } else {
                    holder.editTextCurrentNumber.setText((0).toString())
                }
            }
        }

        holder.plusButton.setOnClickListener {
            if (articles[position].item.unit == 0) {
                val number = holder.editTextCurrentNumber.text.toString().toDouble().toInt()
                holder.editTextCurrentNumber.setText((number + 1).toString())
            } else {
                val number = holder.editTextCurrentNumber.text.toString().toDouble()
                holder.editTextCurrentNumber.setText((number + 1).toString())
            }
        }
    }

    class ShelfViewHolder(shelfView: View) : RecyclerView.ViewHolder(shelfView) {
        val textViewArticle: TextView = shelfView.findViewById(R.id.Article_Name_choose_Article)
        val editTextCurrentNumber: EditText =
            shelfView.findViewById(R.id.editText_number_of_item_in_shelf)
        val plusButton: Button = shelfView.findViewById(R.id.btn_rv_add_article_to_shelf_plus)
        val minusButton: Button = shelfView.findViewById(R.id.btn_rv_add_article_to_shelf_minus)
    }
}
