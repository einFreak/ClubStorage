package com.zappproject.clubstorage.ViewModels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.zappproject.clubstorage.database.Item.Item;
import com.zappproject.clubstorage.database.Item.ItemWithNumber;
import com.zappproject.clubstorage.database.Repository;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class ItemViewModel extends AndroidViewModel {

    private Repository repository;
    private LiveData<List<ItemWithNumber>> allItems;

    public ItemViewModel(@NonNull Application application) {
        super(application);
        repository = new Repository(application);
        allItems = repository.getAllItems();
    }

    public void insert(Item item){
        repository.insertItem(item);
    }

    public void update(Item item){
        repository.updateItem(item);
    }

    public void delete(Item item) {
        repository.deleteItem(item);
    }

    public LiveData<List<ItemWithNumber>> getAllItems() {
        return allItems;
    }

    public List<ItemWithNumber> getAllItemsOfList(int shelfId) throws ExecutionException, InterruptedException {
        return repository.getAllItemsOfList(shelfId);
    }

    public List<ItemWithNumber> getAllItemsWithNumberOfList(int shelfId) throws ExecutionException, InterruptedException {
        return repository.getAllItemsWithNumberOfList(shelfId);
    }
}
