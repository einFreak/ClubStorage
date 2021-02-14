package com.zappproject.clubstorage.database.User;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(tableName = UserTable.TABLE_NAME,
        indices = {@Index(value = {"email"}, unique = true)}
)
public class User {

    @PrimaryKey(autoGenerate = true)
    private int uId;
    private String firstname;
    private String lastname;
    private String email;
    private String password;
    private int permission;

    @Ignore
    public User(int uId, String firstname, String lastname, String email, String password, int permission) {
        this(firstname, lastname, email, password, permission);
        this.uId = uId;
    }

    public User(String firstname, String lastname, String email, String password, int permission) {
        this.firstname = firstname;

        this.lastname = lastname;
        this.email = email;
        this.password = password;
        setPermission(permission);
    }

    @Ignore
    public final static String[] PERMISSIONS = {"no Access", "read-only", "read/write", "Admin"};
    @Ignore
    public final static int NO_ACCESS = 0;
    @Ignore
    public final static int READ_ONLY = 1;
    @Ignore
    public final static int READ_WRITE = 2;
    @Ignore
    public final static int ADMIN = 3;

    @Ignore
    public final static String[] PERMISSION = {"no Access", "read only", "read/write", "Admin"};

    public int getUId() {
        return uId;
    }

    public void setUId(int uId) {
        this.uId = uId;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getPermission() {
        return permission;
    }

    public void setPermission(int permission) {
        if (permission >= NO_ACCESS && permission <= ADMIN) {
            this.permission = permission;
        } else {
            this.permission = NO_ACCESS;
        }
    }
}
