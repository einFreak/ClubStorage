package com.zappproject.clubstorage.ViewModels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.zappproject.clubstorage.database.Repository;
import com.zappproject.clubstorage.database.User.User;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class UserViewModel extends AndroidViewModel {

    private Repository repository;
    private LiveData<List<User>> allUsers;

    public UserViewModel(@NonNull Application application) {
        super(application);
        repository = new Repository(application);
        allUsers = repository.getAllUsers();
    }

    public void insert(User user) {
        repository.insertUser(user);
    }

    public void update(User user) {
        repository.updateUser(user);
    }

    public void delete(User user) {
        repository.deleteUser(user);
    }

    public LiveData<List<User>> getAllUsers() {
        return allUsers;
    }

    public User getUser(String mail) throws ExecutionException, InterruptedException {
        return repository.getUser(mail);
    }
}
