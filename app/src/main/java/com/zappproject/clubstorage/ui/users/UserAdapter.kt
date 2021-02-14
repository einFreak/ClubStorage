package com.zappproject.clubstorage.ui.users

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.zappproject.clubstorage.R
import com.zappproject.clubstorage.database.User.User
import java.util.Locale

import kotlin.collections.ArrayList

class UserAdapter : RecyclerView.Adapter<UserAdapter.UserViewHolder>(), Filterable {

    private var users: List<User> = ArrayList()
    private var usersFiltered: List<User> = ArrayList()

    fun setUser(users: List<User>) {
        this.users = users
        this.usersFiltered = users
        notifyDataSetChanged()
    }

    fun getUserAt(position: Int): User? {
        return usersFiltered[position]
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        return UserViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.rv_layout_user, parent, false)
        )
    }

    override fun getItemCount(): Int = usersFiltered.size

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val currentUser = usersFiltered[position]

        holder.textViewFirstName.text = currentUser.firstname
        holder.textViewLastName.text = currentUser.lastname
        holder.textViewEmail.text = currentUser.email
        holder.textViewPassword.text = currentUser.password
        holder.textViewPermission.text = User.PERMISSION[currentUser.permission]

        // Hide button if permission is insufficient
        val sharedPreferences = holder.itemView.context.getSharedPreferences("userdata", Context.MODE_PRIVATE) ?: return
        val permission = sharedPreferences.getInt("permission", 0)
        @SuppressWarnings("MagicNumber")
        if (permission < 3) {
            holder.textViewPassword.visibility = View.GONE
        }
    }

    class UserViewHolder(userView: View) : RecyclerView.ViewHolder(userView) {
        val textViewFirstName: TextView = userView.findViewById(R.id.User_FirstName)
        val textViewLastName: TextView = userView.findViewById(R.id.User_Surname)
        val textViewEmail: TextView = userView.findViewById(R.id.User_Mail)
        val textViewPassword: TextView = userView.findViewById(R.id.User_PW)
        val textViewPermission: TextView = userView.findViewById(R.id.User_Permission)
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val charSearch = constraint.toString()
                if (charSearch.isEmpty()) {
                    usersFiltered = users
                } else {
                    val results: MutableList<User> = ArrayList()

                    for (row in users) {
                        if (row.firstname.toLowerCase(Locale.ROOT)
                                .contains(charSearch.toLowerCase(Locale.ROOT))
                        ) {
                            results.add(row)
                        }
                    }

                    usersFiltered = results
                }
                val filterResult = FilterResults()
                filterResult.values = usersFiltered
                return filterResult
            }

            @Suppress("UNCHECKED_CAST")
            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                usersFiltered = results?.values as List<User>
                notifyDataSetChanged()
            }
        }
    }
}
