package com.zappproject.clubstorage.ViewModels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.zappproject.clubstorage.database.Repository;
import com.zappproject.clubstorage.database.Shelf.Shelf;
import com.zappproject.clubstorage.database.Shelf.ShelfWithNumber;

import java.util.List;

public class ShelfViewModel extends AndroidViewModel {

    private Repository repository;
    private LiveData<List<ShelfWithNumber>> allShelves;

    public ShelfViewModel(@NonNull Application application) {
        super(application);
        repository = new Repository(application);
        allShelves = repository.getAllShelves();
    }

    public void insert(Shelf shelf){
        repository.insertShelf(shelf);
    }

    public void update(Shelf shelf){
        repository.updateShelf(shelf);
    }

    public void delete(Shelf shelf){
        repository.deleteShelf(shelf);
    }

    public LiveData<List<ShelfWithNumber>> getAllShelves() {
        return allShelves;
    }
}
