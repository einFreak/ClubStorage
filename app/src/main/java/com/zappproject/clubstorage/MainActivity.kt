package com.zappproject.clubstorage

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.navigation.NavigationView
import com.zappproject.clubstorage.login.LoginActivity

class MainActivity : AppCompatActivity() {

    // Navigation zwischen fragments durch appBarConfiguration - wird in onCreate definiert
    private lateinit var appBarConfiguration: AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        // Navigation zwischen fragments durch appBarConfiguration
        val drawerLayout: DrawerLayout =
            findViewById(R.id.drawer_layout) // drawer_layout defined in main_activity
        val navView: NavigationView =
            findViewById(R.id.nav_view) // nav_view defined in main_activity (NavView)

        // nav_host_fragment in content_main hosts fragments, navigation options passed
        val navController = findNavController(R.id.nav_host_fragment)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(setOf(
                R.id.nav_shelf, R.id.nav_article, R.id.nav_users, R.id.nav_settings), drawerLayout)
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        val sharedPreferences = this.getSharedPreferences("userdata", Context.MODE_PRIVATE) ?: return
        val user = sharedPreferences.getString("user", "error")
        if (user == "error") {
            val login = Intent(this, LoginActivity::class.java)
            startActivity(login)
        }
    }

    // ... menu in toolbar - calls main.xml in menu
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_logout -> {
                val sharedPreferences = this.getSharedPreferences("userdata", Context.MODE_PRIVATE)
                val username = sharedPreferences.getString("user", "")
                val logoutMsg = getString(R.string.logout_msg)
                sharedPreferences.edit().remove("user").apply()

                Toast.makeText(
                    applicationContext,
                    "$username $logoutMsg",
                    Toast.LENGTH_LONG
                ).show()

                val login = Intent(this, LoginActivity::class.java)
                startActivity(login)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    // to go back via software in a fragment
    fun goBack() {
        onBackPressed()
    }

    // to collapse the keyboard after input is done
    fun hideSoftKeyBoard(view: View) {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

}
