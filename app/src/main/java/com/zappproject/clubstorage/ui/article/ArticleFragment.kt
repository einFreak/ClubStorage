package com.zappproject.clubstorage.ui.article

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import com.zappproject.clubstorage.R
import com.zappproject.clubstorage.ViewModels.ItemViewModel
import com.zappproject.clubstorage.database.Item.Item
import com.zappproject.clubstorage.database.Item.ItemWithNumber

class ArticleFragment : Fragment() {

    private lateinit var floatingActionButton: FloatingActionButton
    private val itemViewModel: ItemViewModel by viewModels()
    private lateinit var adapter: ArticleAdapter
    private lateinit var deleteIcon: Drawable

    private var swipeBackground: ColorDrawable = ColorDrawable(Color.parseColor("#FF0000"))

    // needs to be called, so the fragment is initialized on back-button
    override fun onResume() {
        super.onResume()
        getDataToRv()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_article, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()

        val search = view.findViewById<SearchView>(R.id.search_articles)
        search.setOnQueryTextListener(object : SearchView.OnQueryTextListener,
            androidx.appcompat.widget.SearchView.OnQueryTextListener {

            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                adapter.filter.filter(newText)
                return false
            }
        })
    }

    private fun init() {
        getDataToRv()
        setupFabOnClickListener()
        setupDeleteFunction()
    }

    private fun setupDeleteFunction() {
        deleteIcon = ContextCompat.getDrawable(requireContext(), R.drawable.ic_delete_black)!!

        val sharedPreferences =
            activity?.getSharedPreferences("userdata", Context.MODE_PRIVATE) ?: return
        val permission = sharedPreferences.getInt("permission", 0)
        if (permission >= 2) {

            ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(
                0,
                ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
            ) {

                override fun onMove(
                    recyclerView: RecyclerView,
                    viewHolder: RecyclerView.ViewHolder,
                    target: RecyclerView.ViewHolder
                ): Boolean {
                    return false
                }

                override fun onChildDraw(
                    c: Canvas,
                    recyclerView: RecyclerView,
                    viewHolder: RecyclerView.ViewHolder,
                    dX: Float,
                    dY: Float,
                    actionState: Int,
                    isCurrentlyActive: Boolean
                ) {
                    val itemView = viewHolder.itemView
                    val iconMargin = (itemView.height - deleteIcon.intrinsicHeight) / 2

                    if (dX > 0) {
                        swipeBackground.setBounds(
                            itemView.left,
                            itemView.top,
                            dX.toInt(),
                            itemView.bottom
                        )
                        deleteIcon.setBounds(
                            itemView.left + iconMargin,
                            itemView.top + iconMargin,
                            itemView.left + iconMargin + deleteIcon.intrinsicWidth,
                            itemView.bottom - iconMargin
                        )
                    } else {
                        swipeBackground.setBounds(
                            itemView.right + dX.toInt(),
                            itemView.top,
                            itemView.right,
                            itemView.bottom
                        )
                        deleteIcon.setBounds(
                            itemView.right - iconMargin - deleteIcon.intrinsicWidth,
                            itemView.top + iconMargin,
                            itemView.right - iconMargin,
                            itemView.bottom - iconMargin
                        )
                    }
                    swipeBackground.draw(c)
                    c.save()
                    if (dX > 0) {
                        c.clipRect(
                            itemView.left,
                            itemView.top,
                            dX.toInt(),
                            itemView.bottom
                        )
                    } else {
                        c.clipRect(
                            itemView.right + dX.toInt(),
                            itemView.top,
                            itemView.right,
                            itemView.bottom
                        )
                    }
                    deleteIcon.draw(c)
                    c.restore()

                    super.onChildDraw(
                        c,
                        recyclerView,
                        viewHolder,
                        dX,
                        dY,
                        actionState,
                        isCurrentlyActive
                    )
                }

                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                    var currentArticle: Item? =
                        adapter.getArticleAt(viewHolder.adapterPosition)?.item
                    itemViewModel.delete(currentArticle)
                    Snackbar.make(
                        viewHolder.itemView,
                        currentArticle!!.title + " " + resources.getString(R.string.was_deleted),
                        Snackbar.LENGTH_SHORT
                    ).setAction("WIEDERHERSTELLEN") {
                        itemViewModel.insert(currentArticle)
                    }.show()
                }
            }).attachToRecyclerView(view?.findViewById(R.id.recycler_view_articles))
        }
    }

    private fun getDataToRv() {

        val rv = view?.findViewById<RecyclerView>(R.id.recycler_view_articles)
        rv?.layoutManager = LinearLayoutManager(activity)

        // Draws horizontal lines between the list items
        rv?.addItemDecoration(
            DividerItemDecoration(
                rv.context, DividerItemDecoration.VERTICAL
            )
        )

        // Link data to RecView Adapter
        adapter = ArticleAdapter()
        rv?.adapter = adapter
        itemViewModel.allItems.observe(viewLifecycleOwner,
            Observer<List<ItemWithNumber>> { articles -> adapter.setArticle(articles, true) })
    }

    private fun setupFabOnClickListener() {
        val action = ArticleFragmentDirections.actionNavArticleToNavAddArticle(
            getString(R.string.add_article_titel),
            -1,
            null
        )
        floatingActionButton =
            requireView().findViewById<View>(R.id.fab_addArticle) as FloatingActionButton
        floatingActionButton.setOnClickListener {
            findNavController().navigate(action)
        }

        // Hide button if permission is insufficient
        val sharedPreferences = activity?.getSharedPreferences("userdata", Context.MODE_PRIVATE) ?: return
        val permission = sharedPreferences.getInt("permission", 0)
        if (permission < 2) {
            floatingActionButton.hide()
        }
    }
}
