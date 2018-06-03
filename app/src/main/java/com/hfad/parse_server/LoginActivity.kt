package com.hfad.parse_server

import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import com.parse.LogInCallback
import com.parse.ParseException
import com.parse.ParseUser
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity(), View.OnClickListener, View.OnKeyListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        hideProgressBar()
        login_image_view.setOnClickListener(this)
        login_cont_layout.setOnClickListener(this)
        login_et_password.setOnKeyListener(this)

    }

    override fun onClick(v: View) {
        if (v.id == R.id.login_image_view || v.id == R.id.login_cont_layout){
            hideKeyboard()
        }
    }

    override fun onKey(v: View, keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_ENTER && event!!.action == KeyEvent.ACTION_DOWN){
            logIn(v)
        }
        return false
    }

    fun logIn(view : View){
        hideKeyboard()
        showProgressBar()

        val username = login_et_username.text.toString()
        val password = login_et_password.text.toString()
        ParseUser.logInInBackground(username, password, object : LogInCallback{
            override fun done(user: ParseUser?, e: ParseException?) {
                if (e == null){
                    sendUserToMainActivity()
                } else{
                    hideProgressBar()
                    Toast.makeText(applicationContext, e.message, Toast.LENGTH_LONG).show()
                    login_et_password.text.clear()
                }
            }
        })
    }

    fun signUp(view: View){
        val intent = Intent(this, RegisterActivity::class.java)
        startActivity(intent)
        finish()
    }


    private fun hideKeyboard() {
        try {
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(currentFocus!!.windowToken, 0)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun sendUserToMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        hideProgressBar()
        startActivity(intent)
        finish()
    }

    private fun hideProgressBar(){
        progressBar.visibility = View.INVISIBLE
        progressBar.isActivated = false
    }

    private fun showProgressBar(){
        progressBar.visibility = View.VISIBLE
        progressBar.isActivated = true
    }
}
