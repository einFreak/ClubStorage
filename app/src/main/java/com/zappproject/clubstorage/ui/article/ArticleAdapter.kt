package com.zappproject.clubstorage.ui.article

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.TextView
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.zappproject.clubstorage.R
import com.zappproject.clubstorage.database.Item.Item
import com.zappproject.clubstorage.database.Item.ItemWithNumber
import com.zappproject.clubstorage.ui.shelfdetail.ShelfDetailsFragmentDirections
import java.util.*
import kotlin.collections.ArrayList

// Adapter uses a List of the Article Class defined in Article.kt
class ArticleAdapter : RecyclerView.Adapter<ArticleAdapter.ArticleViewHolder>(), Filterable {

    private var articles: List<ItemWithNumber> = ArrayList()
    private var articlesFiltered: List<ItemWithNumber> = ArrayList()
    private var isArticleFragment: Boolean = false

    // needed for handling data
    override fun getItemCount(): Int = articlesFiltered.size

    fun setArticle(articles: List<ItemWithNumber>, isArticleFragment: Boolean) {
        this.articles = articles
        this.articlesFiltered = articles
        this.isArticleFragment = isArticleFragment
        notifyDataSetChanged()
    }

    fun getArticleAt(position: Int): ItemWithNumber? {
        return articlesFiltered[position]
    }

    // sets RecyclerView Object Layout (here rv_layout_article) and inflates it
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticleViewHolder {
        val itemView: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.rv_layout_article, parent, false)
        return ArticleViewHolder(itemView)
    }

    // sets given Data to according layout rv_layout_article
    override fun onBindViewHolder(holder: ArticleViewHolder, position: Int) {
        val currentArticle = articlesFiltered[position]

        // these are the ids in the layout
        val itemPrice = holder.itemView.context.getString(
            R.string.article_price,
            currentArticle.item.price.toString(),
            Item.UNITS[currentArticle.item.unit])

        holder.textViewArticleName.text = currentArticle.item.title
        holder.textViewArticlePrice.text = itemPrice
        holder.textViewArticleNote.text = currentArticle.item.note

        if (currentArticle.number != 0.0) {
            if (currentArticle.item.unit == 0) {
                val combineString = holder.itemView.context.getString(
                    R.string.space,
                    currentArticle.number.toInt().toString(),
                    Item.UNITS[currentArticle.item.unit]
                )
                holder.textViewArticleNumber.text = combineString
            } else {
                val combineString = holder.itemView.context.getString(
                    R.string.space,
                    currentArticle.number.toString(),
                    Item.UNITS[currentArticle.item.unit]
                )
                holder.textViewArticleNumber.text = combineString
            }
        } else {
            holder.textViewArticleNumber.text = "-"
        }

        // Hide button if permission is insufficient
        val sharedPreferences = holder.itemView.context.getSharedPreferences("userdata", Context.MODE_PRIVATE) ?: return
        val permission = sharedPreferences.getInt("permission", 0)
        if (permission < 2) {
            holder.buttonArticleOptions.visibility = View.GONE
        }

        if (isArticleFragment) {
            val action =
                ArticleFragmentDirections.actionNavArticleToNavAddArticle(
                    holder.itemView.context.getString(R.string.edit_article),
                    currentArticle.item.iId,
                    currentArticle.item.title
                )
            action.price = currentArticle.item.price.toString()
            action.note = currentArticle.item.note
            action.unit = currentArticle.item.unit
            holder.buttonArticleOptions.setOnClickListener(
                Navigation.createNavigateOnClickListener(action)
            )
        } else {
            val action =
                ShelfDetailsFragmentDirections.actionShelfDetailsToNavAddArticle(
                    holder.itemView.context.getString(R.string.edit_article),
                    currentArticle.item.iId,
                    currentArticle.item.title
                )
            action.price = currentArticle.item.price.toString()
            action.note = currentArticle.item.note
            action.unit = currentArticle.item.unit
            holder.buttonArticleOptions.setOnClickListener(
                Navigation.createNavigateOnClickListener(action)
            )
        }
    }

    // passes view object to be used in fragments
    class ArticleViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textViewArticleName: TextView = itemView.findViewById(R.id.Article_Name)
        val textViewArticlePrice: TextView = itemView.findViewById(R.id.Article_Price)
        val textViewArticleNumber: TextView = itemView.findViewById(R.id.Article_Number)
        val textViewArticleNote: TextView = itemView.findViewById(R.id.Article_Note)
        val buttonArticleOptions: TextView = itemView.findViewById(R.id.Btn_Article_Options)
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val charSearch = constraint.toString()
                if (charSearch.isEmpty()) {
                    articlesFiltered = articles
                } else {
                    val results: MutableList<ItemWithNumber> = ArrayList()

                    for (row in articles) {
                        if (row.item.title.toLowerCase(Locale.ROOT)
                                .contains(charSearch.toLowerCase(Locale.ROOT))
                        ) {
                            results.add(row)
                        }
                    }
                    articlesFiltered = results
                }
                val filterResult = FilterResults()
                filterResult.values = articlesFiltered
                return filterResult
            }

            @Suppress("UNCHECKED_CAST")
            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                articlesFiltered = results?.values as List<ItemWithNumber>
                // setArticle(articlesFiltered)
                notifyDataSetChanged()
            }
        }
    }
}
