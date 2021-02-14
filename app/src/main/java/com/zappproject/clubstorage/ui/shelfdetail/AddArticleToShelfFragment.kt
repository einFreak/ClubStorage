package com.zappproject.clubstorage.ui.shelfdetail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.zappproject.clubstorage.R
import com.zappproject.clubstorage.ViewModels.ContainsViewModel
import com.zappproject.clubstorage.ViewModels.ItemViewModel
import java.util.Vector

class AddArticleToShelfFragment : Fragment() {

    private lateinit var adapter: AddArticleToShelfAdapter
    private lateinit var btnCommit: Button
    private val itemViewModel: ItemViewModel by viewModels()
    private val containsViewModel: ContainsViewModel by viewModels()
    private val args: AddArticleToShelfFragmentArgs by navArgs()
    private var shelfId: Int = 0
    private lateinit var shelfTitle: String

    companion object;

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_add_article_to_shelf, container, false)

        shelfId = args.argShelfId
        shelfTitle = args.argShelfTitle
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setButtonOnClickListeners()
        getDataToRv()
    }

    private fun setButtonOnClickListeners() {
        btnCommit = requireView().findViewById(R.id.btn_commit_add_article_to_shelf)

        btnCommit.setOnClickListener {
            val changes: Vector<ContainsChange> = adapter.getChanges()
            for (c in changes) {
                when {
                    c.getChange() == 0 -> {
                        containsViewModel.deleteItemFromShelf(c.getContains())
                    }
                    c.getChange() == 1 -> {
                        containsViewModel.addItemToShelf(c.getContains())
                    }
                    c.getChange() == 2 -> {
                        containsViewModel.updateItemInShelf(c.getContains())
                    }
                }
            }
        }
    }

    private fun getDataToRv() {
        val rv = view?.findViewById<RecyclerView>(R.id.recycler_view_articles_in_add_article_to_shelf)
        rv?.layoutManager = LinearLayoutManager(activity)
        adapter =
            AddArticleToShelfAdapter()
        rv?.adapter = adapter
        adapter.setArticles(itemViewModel.getAllItemsWithNumberOfList(shelfId), shelfId)
    }
}
