package com.zappproject.clubstorage.ui.shelfdetail

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.zappproject.clubstorage.R
import com.zappproject.clubstorage.ViewModels.ItemViewModel
import com.zappproject.clubstorage.ui.article.ArticleAdapter

class ShelfDetailsFragment : Fragment() {

    private val itemViewModel: ItemViewModel by viewModels()
    private lateinit var floatingActionButton: FloatingActionButton
    private lateinit var adapter: ArticleAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_shelf_details, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val args: ShelfDetailsFragmentArgs by navArgs()

        val addArticleTitle = getString(R.string.add_article_to_shelf_title, args.argShelfName)
        setupFabOnClickListener(args.argShelfID, addArticleTitle)

        getDataToRv(args.argShelfID)
    }

    private fun getDataToRv(shelfId: Int) {
        // set Layout für RecView on shelf fragment with id "recyclerViewShelf"
        val rv = view?.findViewById<RecyclerView>(R.id.recycler_view_articles_in_shelf)
        rv?.layoutManager = LinearLayoutManager(activity)

        // Draws horizontal lines between the list items
        rv?.addItemDecoration(
            DividerItemDecoration(
                rv.context, DividerItemDecoration.VERTICAL
            )
        )

        adapter = ArticleAdapter()
        rv?.adapter = adapter
        adapter.setArticle(itemViewModel.getAllItemsOfList(shelfId), false)
    }

    private fun setupFabOnClickListener(shelfId: Int, shelfTitle: String) {

        val action =
            ShelfDetailsFragmentDirections.actionShelfDetailsToAddArticleToShelf(
                shelfId,
                shelfTitle
            )

        floatingActionButton = requireView().findViewById<View>(R.id.fab_add_article_to_shelf) as FloatingActionButton
        floatingActionButton.setOnClickListener {
            findNavController().navigate(action)
        }

        val sharedPreferences = activity?.getSharedPreferences("userdata", Context.MODE_PRIVATE) ?: return
        val permission = sharedPreferences.getInt("permission", 0)
        if (permission < 2) {
            floatingActionButton.hide()
        }
    }
}
