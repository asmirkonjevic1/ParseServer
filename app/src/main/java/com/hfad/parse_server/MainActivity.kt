package com.hfad.parse_server



import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentTransaction
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import com.esafirm.imagepicker.features.ImagePicker
import com.hfad.parse_server.Fragments.FriendsFragment
import com.hfad.parse_server.Fragments.HomeFragment
import com.hfad.parse_server.Utils.BottomNagivationViewHelper
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx
import com.parse.*
import kotlinx.android.synthetic.main.activity_main.*
import java.io.ByteArrayOutputStream

class MainActivity : AppCompatActivity(), BottomNavigationView.OnNavigationItemSelectedListener {


    private lateinit var homeFragment : HomeFragment
    private lateinit var friendsFragment: FriendsFragment
    private lateinit var toolbar: Toolbar
    private lateinit var bottomNavBar : BottomNavigationViewEx

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        toolbar = findViewById(R.id.toolbar)
        toolbar.setTitle("Home")
        setSupportActionBar(toolbar)

        setupBottomNavigationView()

        if (ParseUser.getCurrentUser() != null){
            //User Loged In
            Toast.makeText(this, "Welcome " + ParseUser.getCurrentUser().username, Toast.LENGTH_LONG).show()
        } else {
            sendUserToLoginActivity()
        }

        homeFragment = HomeFragment()
        friendsFragment = FriendsFragment()

        setFragment(homeFragment)

        bottomNavBar.setOnNavigationItemSelectedListener(this)

        ParseAnalytics.trackAppOpenedInBackground(intent);
    }

    private fun setupBottomNavigationView() {
        bottomNavBar = findViewById(R.id.bottom_nav_bar)
        BottomNagivationViewHelper.setupBottomNavigationView(bottomNavBar)
    }

    override fun onResume() {
        super.onResume()
        bottomNavBar.selectedItemId = R.id.action_home
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.action_home -> {
                toolbar.setTitle("Home")
                setFragment(homeFragment)
                return true
            }
            R.id.action_add_photo -> {
                ImagePicker.create(this@MainActivity)
                        .limit(1)
                        .start();
                return true
            }
            R.id.action_friends -> {
                toolbar.setTitle("Friends")
                setFragment(friendsFragment)
                return true
            }
        }
        return false
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (ImagePicker.shouldHandle(requestCode, resultCode, data)) {
            // or get a single image only
            val image = ImagePicker.getFirstImageOrNull(data)
            val bitmap = BitmapFactory.decodeFile(image.path)
            uploadImage(bitmap)
        }

        super.onActivityResult(requestCode, resultCode, data)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        super.onOptionsItemSelected(item)
        when(item!!.itemId){
            R.id.logout -> {
                ParseUser.logOutInBackground()
                sendUserToLoginActivity()
                return true
            }
        }
        return false
    }

    private fun sendUserToLoginActivity() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun setFragment(fragment : Fragment){
        val fragmentTransaction : FragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.frame_layout, fragment)
        fragmentTransaction.commit()
    }

    private fun uploadImage(bitmap : Bitmap){
        val stream : ByteArrayOutputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 80, stream)
        val byteArray = stream.toByteArray()

        val file = ParseFile("image.png", byteArray)

        val parseObject = ParseObject("Image")
        parseObject.put("image", file)
        parseObject.put("username", ParseUser.getCurrentUser().username)
        parseObject.saveInBackground(object : SaveCallback{
            override fun done(e: ParseException?) {
                if (e == null){
                    Toast.makeText(applicationContext, "Image Uploaded", Toast.LENGTH_SHORT).show()
                }else {
                    Toast.makeText(applicationContext, "There has been an issue", Toast.LENGTH_SHORT).show()
                }
            }
        })
    }
}
