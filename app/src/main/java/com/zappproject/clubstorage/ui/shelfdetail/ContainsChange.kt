package com.zappproject.clubstorage.ui.shelfdetail

import com.zappproject.clubstorage.database.Contains.Contains

data class ContainsChange(private var contains: Contains, private var change: Int) {

    fun getContains(): Contains {
        return contains
    }

    fun getChange(): Int {
        return change
    }

    fun setChange(change: Int) {
        this.change = change
    }
}
