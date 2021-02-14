package com.zappproject.clubstorage.ViewModels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.zappproject.clubstorage.database.Contains.Contains;
import com.zappproject.clubstorage.database.Repository;

public class ContainsViewModel extends AndroidViewModel {
    private Repository repository;

    public ContainsViewModel(@NonNull Application application) {
        super(application);
        repository = new Repository(application);
    }

    public void addItemToShelf(Contains contains) {
        repository.addItemToShelf(contains);
    }

    public void addItemToShelf(int itemId, int shelfId, double number) {
        repository.addItemToShelf(new Contains(itemId, shelfId, number));
    }

    public void updateItemInShelf(Contains contains) {
        repository.updateItemInShelf(contains);
    }

    public void deleteItemFromShelf(Contains contains) {
        repository.deleteItemFromShelf(contains);
    }
}
