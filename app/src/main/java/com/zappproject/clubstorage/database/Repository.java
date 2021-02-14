package com.zappproject.clubstorage.database;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.zappproject.clubstorage.database.Contains.Contains;
import com.zappproject.clubstorage.database.Contains.ContainsDao;
import com.zappproject.clubstorage.database.Item.Item;
import com.zappproject.clubstorage.database.Item.ItemDao;
import com.zappproject.clubstorage.database.Item.ItemWithNumber;
import com.zappproject.clubstorage.database.Shelf.Shelf;
import com.zappproject.clubstorage.database.Shelf.ShelfDao;
import com.zappproject.clubstorage.database.Shelf.ShelfWithNumber;
import com.zappproject.clubstorage.database.User.User;
import com.zappproject.clubstorage.database.User.UserDao;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class Repository {
    private ItemDao itemDao;
    private UserDao userDao;
    private ContainsDao containsDao;
    private ShelfDao shelfDao;

    private LiveData<List<ItemWithNumber>> allItemsWithNumber;
    private LiveData<List<User>> allUser;
    private LiveData<List<ShelfWithNumber>> allShelvesWithNumber;

    public Repository(Application application) {
        StorageRoomDatabase db = StorageRoomDatabase.getInstance(application);
        itemDao = db.itemDao();
        shelfDao = db.shelfDao();
        userDao = db.userDao();
        containsDao = db.containsDao();

        // provides the date for the different Recycler-views
        allItemsWithNumber = itemDao.getAllItemsWithNumber();
        allShelvesWithNumber = shelfDao.getAllShelvesWithNumber();
        allUser = userDao.getAllUsers();
    }

    /* Item */
    public void insertItem(Item item) {
        new InsertItemAsyncTask(itemDao).execute(item);
    }

    private static class InsertItemAsyncTask extends AsyncTask<Item, Void, Void> {
        private ItemDao itemDao;

        public InsertItemAsyncTask(ItemDao itemDao) {
            this.itemDao = itemDao;
        }

        @Override
        protected Void doInBackground(Item... items) {
            itemDao.insert(items[0]);
            return null;
        }
    }

    public void deleteItem(Item item) {
        new DeleteItemAsyncTask(itemDao).execute(item);
    }

    private static class DeleteItemAsyncTask extends AsyncTask<Item, Void, Void> {
        private ItemDao itemDao;

        public DeleteItemAsyncTask(ItemDao itemDao) {
            this.itemDao = itemDao;
        }

        @Override
        protected Void doInBackground(Item... items) {
            itemDao.delete(items[0]);
            return null;
        }
    }

    public void updateItem(Item item) {
        new UpdateItemAsyncTask(itemDao).execute(item);
    }

    private static class UpdateItemAsyncTask extends AsyncTask<Item, Void, Void> {
        private ItemDao itemDao;

        public UpdateItemAsyncTask(ItemDao itemDao) {
            this.itemDao = itemDao;
        }

        @Override
        protected Void doInBackground(Item... items) {
            itemDao.update(items[0]);
            return null;
        }
    }

    public LiveData<List<ItemWithNumber>> getAllItems() {
        return allItemsWithNumber;
    }

    public List<ItemWithNumber> getAllItemsOfList(int shelfId) throws ExecutionException, InterruptedException {
        return new GetAllItemsOfListAsyncTask(itemDao).execute(shelfId).get();
    }

    private static class GetAllItemsOfListAsyncTask extends AsyncTask<Integer, Void, List<ItemWithNumber>> {
        private ItemDao itemDao;

        public GetAllItemsOfListAsyncTask(ItemDao itemDao) {
            this.itemDao = itemDao;
        }

        @Override
        protected List<ItemWithNumber> doInBackground(Integer... integers) {
            return itemDao.getAllItemsOfList(integers[0]);
        }
    }

    public List<ItemWithNumber> getAllItemsWithNumberOfList(int shelfId) throws ExecutionException, InterruptedException {
        return new GetAllItemsWithNumberOfListAsyncTask(itemDao).execute(shelfId).get();
    }

    private static class GetAllItemsWithNumberOfListAsyncTask extends AsyncTask<Integer, Void, List<ItemWithNumber>> {
        private ItemDao itemDao;

        public GetAllItemsWithNumberOfListAsyncTask(ItemDao itemDao) {
            this.itemDao = itemDao;
        }

        @Override
        protected List<ItemWithNumber> doInBackground(Integer... integers) {
            return itemDao.getAllItemsWithNumberOfList(integers[0]);
        }
    }


    /* User */
    public void insertUser(User user) {
        new InsertUserAsyncTask(userDao).execute(user);
    }

    private static class InsertUserAsyncTask extends AsyncTask<User, Void, Void> {
        private UserDao userDao;

        public InsertUserAsyncTask(UserDao userDao) {
            this.userDao = userDao;
        }

        @Override
        protected Void doInBackground(User... users) {
            userDao.insert(users[0]);
            return null;
        }
    }

    public void deleteUser(User user) {
        new DeleteUserAsyncTask(userDao).execute(user);
    }

    private static class DeleteUserAsyncTask extends AsyncTask<User, Void, Void> {
        private UserDao userDao;

        public DeleteUserAsyncTask(UserDao userDao) {
            this.userDao = userDao;
        }

        @Override
        protected Void doInBackground(User... users) {
            userDao.delete(users[0]);
            return null;
        }
    }

    public void updateUser(User user) {
        new UpdateUserAsyncTask(userDao).execute(user);
    }

    private static class UpdateUserAsyncTask extends AsyncTask<User, Void, Void> {
        private UserDao userDao;

        public UpdateUserAsyncTask(UserDao userDao) {
            this.userDao = userDao;
        }

        @Override
        protected Void doInBackground(User... users) {
            userDao.update(users[0]);
            return null;
        }
    }

    public LiveData<List<User>> getAllUsers() {
        return allUser;
    }

    public User getUser(String email) throws ExecutionException, InterruptedException {
        return new GetUserAsyncTask(userDao).execute(email).get();
    }

    private static class GetUserAsyncTask extends AsyncTask<String, Void, User> {
        private UserDao userDao;

        public GetUserAsyncTask(UserDao userDao) {
            this.userDao = userDao;
        }

        @Override
        protected User doInBackground(String... strings) {
            return userDao.getUser(strings[0]);
        }
    }

//
//    public LiveData<List<User>> getUsersOfPermission(int permission) {
//        return null;
//    }

    /* Shelf */
    public void insertShelf(Shelf shelf) {
        new InsertShelfAsyncTask(shelfDao).execute(shelf);
    }

    private static class InsertShelfAsyncTask extends AsyncTask<Shelf, Void, Void> {
        private ShelfDao shelfDao;

        public InsertShelfAsyncTask(ShelfDao shelfDao) {
            this.shelfDao = shelfDao;
        }

        @Override
        protected Void doInBackground(Shelf... shelves) {
            shelfDao.insert(shelves[0]);
            return null;
        }
    }

    public void deleteShelf(Shelf shelf) {
        new DeleteShelfAsyncTask(shelfDao).execute(shelf);
    }

    private static class DeleteShelfAsyncTask extends AsyncTask<Shelf, Void, Void> {
        private ShelfDao shelfDao;

        public DeleteShelfAsyncTask(ShelfDao shelfDao) {
            this.shelfDao = shelfDao;
        }

        @Override
        protected Void doInBackground(Shelf... shelves) {
            shelfDao.delete(shelves[0]);
            return null;
        }
    }

    public void updateShelf(Shelf shelf) {
        new UpdateShelfAsyncTask(shelfDao).execute(shelf);
    }

    private static class UpdateShelfAsyncTask extends AsyncTask<Shelf, Void, Void> {
        private ShelfDao shelfDao;

        public UpdateShelfAsyncTask(ShelfDao shelfDao) {
            this.shelfDao = shelfDao;
        }

        @Override
        protected Void doInBackground(Shelf... shelves) {
            shelfDao.update(shelves[0]);
            return null;
        }
    }

    public LiveData<List<ShelfWithNumber>> getAllShelves() {
        return allShelvesWithNumber;
    }

    /* Contains */
    public void addItemToShelf(Contains contains) {
        new AddItemToShelfAsyncTask(containsDao).execute(contains);
    }

    private static class AddItemToShelfAsyncTask extends AsyncTask<Contains, Void, Void> {
        private ContainsDao containsDao;

        public AddItemToShelfAsyncTask(ContainsDao containsDao) {
            this.containsDao = containsDao;
        }

        @Override
        protected Void doInBackground(Contains... contains) {
            containsDao.insert(contains[0]);
            return null;
        }
    }

    public void updateItemInShelf(Contains contains) {
        new UpdateItemInShelfAsyncTask(containsDao).execute(contains);
    }

    private static class UpdateItemInShelfAsyncTask extends AsyncTask<Contains, Void, Void> {
        private ContainsDao containsDao;

        public UpdateItemInShelfAsyncTask(ContainsDao containsDao) {
            this.containsDao = containsDao;
        }

        @Override
        protected Void doInBackground(Contains... contains) {
            containsDao.update(contains[0]);
            return null;
        }
    }

    public void deleteItemFromShelf(Contains contains) {
        new DeleteItemFromShelfAsyncTask(containsDao).execute(contains);
    }

    private static class DeleteItemFromShelfAsyncTask extends AsyncTask<Contains, Void, Void> {
        private ContainsDao containsDao;

        public DeleteItemFromShelfAsyncTask(ContainsDao containsDao) {
            this.containsDao = containsDao;
        }

        @Override
        protected Void doInBackground(Contains... contains) {
            containsDao.delete(contains[0]);
            return null;
        }
    }
}
