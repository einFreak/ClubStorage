package com.zappproject.clubstorage.ui.shelf

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
import com.zappproject.clubstorage.ViewModels.ShelfViewModel
import com.zappproject.clubstorage.database.Shelf.Shelf
import com.zappproject.clubstorage.database.Shelf.ShelfWithNumber

class ShelfFragment : Fragment() {

    private lateinit var floatingActionButton: FloatingActionButton
    private val shelfViewModel: ShelfViewModel by viewModels()
    private lateinit var adapter: ShelfAdapter
    private var swipeBackground: ColorDrawable = ColorDrawable(Color.parseColor("#FF0000"))
    private lateinit var deleteIcon: Drawable

    // needs to be called, so the fragment is initialized on back-button from shelf detail fragment
    override fun onResume() {
        super.onResume()
        addDataToRv()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_shelf, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
        setupSearch(view)
    }

    private fun init() {
        addDataToRv()
        setupDeleteFunction()
        setupFabOnClickListener()
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

                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                    val currentShelf: Shelf = adapter.getShelfAt(viewHolder.adapterPosition).shelf
                    shelfViewModel.delete(currentShelf)
                    Snackbar.make(
                        viewHolder.itemView,
                        currentShelf.title + " " + resources.getString(
                            R.string.was_deleted
                        ),
                        Snackbar.LENGTH_SHORT
                    ).setAction("WIEDERHERSTELLEN") {
                        shelfViewModel.insert(currentShelf)
                    }.show()
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

            }).attachToRecyclerView(view?.findViewById<RecyclerView>(R.id.recycler_view_shelf))
        }
    }

    private fun addDataToRv() {
        // set Layout für RecView on shelf fragment with id "recyclerViewShelf"
        val rv = view?.findViewById<RecyclerView>(R.id.recycler_view_shelf)
        rv?.layoutManager = LinearLayoutManager(activity)

        // Draws horizontal lines between the list items
        rv?.addItemDecoration(
            DividerItemDecoration(
                rv.context, DividerItemDecoration.VERTICAL
            )
        )

        // Link data to RV
        adapter = ShelfAdapter()
        rv?.adapter = adapter
        shelfViewModel.allShelves.observe(viewLifecycleOwner,
            Observer<List<ShelfWithNumber>> { shelves -> adapter.setShelf(shelves) }
        )
    }

    private fun setupFabOnClickListener() {
        val actionAddShelf = ShelfFragmentDirections.actionNavShelfToAddShelfFragment(
            getString(R.string.add_shelf_title),
            -1,
            null,
            null
        )
        floatingActionButton = requireView().findViewById<View>(R.id.fab_addShelf) as FloatingActionButton
        floatingActionButton.setOnClickListener({
            findNavController().navigate(actionAddShelf)
        })

        // Hide button if permission is insufficient
        val sharedPreferences = activity?.getSharedPreferences("userdata", Context.MODE_PRIVATE) ?: return
        val permission = sharedPreferences.getInt("permission", 0)
        if (permission < 2) {
            floatingActionButton.hide()
        }
    }

    private fun setupSearch(view: View) {
        val search = view.findViewById<SearchView>(R.id.search_shelves)
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
}
