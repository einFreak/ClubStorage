package com.zappproject.clubstorage.database;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.zappproject.clubstorage.database.Contains.Contains;
import com.zappproject.clubstorage.database.Contains.ContainsDao;
import com.zappproject.clubstorage.database.Item.Item;
import com.zappproject.clubstorage.database.Item.ItemDao;
import com.zappproject.clubstorage.database.Shelf.Shelf;
import com.zappproject.clubstorage.database.Shelf.ShelfDao;
import com.zappproject.clubstorage.database.User.User;
import com.zappproject.clubstorage.database.User.UserDao;

@Database(entities = {User.class, Shelf.class, Item.class, Contains.class}, version = 1)
public abstract class StorageRoomDatabase extends RoomDatabase {

    private static final String DATABASE_NAME = "storage_room_database.db";
    private static volatile StorageRoomDatabase instance;

    public abstract UserDao userDao();

    public abstract ShelfDao shelfDao();

    public abstract ContainsDao containsDao();

    public abstract ItemDao itemDao();

    public static synchronized StorageRoomDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                    StorageRoomDatabase.class, DATABASE_NAME)
                    .fallbackToDestructiveMigration().addCallback(roomCallBack)
                    .build();
        }
        return instance;
    }

    private static RoomDatabase.Callback roomCallBack = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            new PopulateDbAsyncTask(instance).execute();
        }
    };

    private static class PopulateDbAsyncTask extends AsyncTask<Void, Void, Void> {
        private UserDao userDao;
        private ShelfDao shelfDao;
        private ItemDao itemDao;
        private ContainsDao containsDao;

        private PopulateDbAsyncTask(StorageRoomDatabase db) {
            userDao = db.userDao();
            shelfDao = db.shelfDao();
            itemDao = db.itemDao();
            containsDao = db.containsDao();
        }

        @Override
        protected Void doInBackground(Void... voids) {

            //TODO(Brandl): remove before release, just to have some dummy data to play with
            userDao.insert(new User("Thomas", "Brandl",
                    "thomas.brandl@cs.com", "PSW1234", 3));
            userDao.insert(new User("Felix", "Wild",
                    "fw@cs.com", "PSW1234", 3));

            shelfDao.insert(new Shelf("Regal1", "Notiz1"));
            shelfDao.insert(new Shelf("Regal2", "Notiz 2"));

            itemDao.insert(new Item("Banane", 1.5, "Alles Banane?", 2));
            itemDao.insert(new Item("Apfel", 1.4, "Gesund und munter", 2));
            itemDao.insert(new Item("Kiwi", 0.99, "nicht der Vogel!", 2));
            itemDao.insert(new Item("Bier", 1.5, "Radler ist kein Alkohol", 1));
            itemDao.insert(new Item("Wasser", 0.2, "zum waschen", 1));
            itemDao.insert(new Item("Weinschorle", 5.60, "aus Rotwein", 0));
            itemDao.insert(new Item("Schreibtisch", 120, "Eiche", 0));
            itemDao.insert(new Item("Labtop", 899.98, "von 2013", 0));
            itemDao.insert(new Item("Drucker", 250, "Tintenstrahl", 0));
            itemDao.insert(new Item("Bett", 459.98, "Maße: 200 x 140", 0));
            itemDao.insert(new Item("Stift", 0.7, "Bleistift", 0));
            itemDao.insert(new Item("Holzbalken", 20, "Fichte: 100 x 60 x 2000mm", 0));
            itemDao.insert(new Item("Autoreifen", 52.15, "Continental Winterreichfen", 0));
            itemDao.insert(new Item("Hundehalsband", 5, "in blau", 0));
            itemDao.insert(new Item("Leine", 1.5, "1,4m lang", 0));
            itemDao.insert(new Item("Frondlader", 1.5, "Marke Stoll", 0));
            itemDao.insert(new Item("Musik-CD", 1.5, "mit den besten Liedern", 0));
            itemDao.insert(new Item("Armbanduhr", 1.5, "eine Rolex", 0));
            itemDao.insert(new Item("Audi", 41265.12, "BrumBrum", 0));
            itemDao.insert(new Item("Cokusnuss", 15, "Wer hat die Cokusnuss versteckt?", 0));

            containsDao.insert(new Contains(1, 1, 5));
            containsDao.insert(new Contains(1, 2, 2));
            containsDao.insert(new Contains(1, 3, 7));
            //End of dummy data

            return null;
        }
    }
}
