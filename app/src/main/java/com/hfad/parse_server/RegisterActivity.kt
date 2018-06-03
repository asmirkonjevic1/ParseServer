package com.hfad.parse_server

import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import com.parse.*
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_register.*

class RegisterActivity : AppCompatActivity(), View.OnClickListener, View.OnKeyListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        hideProgressBar()

        register_image_view.setOnClickListener(this)
        register_const_layout.setOnClickListener(this)
        register_et_confirm_password.setOnKeyListener(this)
    }

    override fun onClick(v: View) {
        if (v.id == R.id.register_image_view || v.id == R.id.register_const_layout){
            hideKeyboard()
        }
    }

    override fun onKey(v: View, keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_ENTER && event!!.action == KeyEvent.ACTION_DOWN){
            signUp(v)
        }
        return false
    }

    fun signUp(view : View){
        hideKeyboard()
        showProgressBar()

        val username = register_et_username.text.toString()
        val password = register_et_password.text.toString()
        val confirmPassword = register_et_confirm_password.text.toString()
        if (username == ""){
            hideProgressBar()
            Toast.makeText(this, "Please provide username", Toast.LENGTH_SHORT).show()
        } else if (password == ""){
            hideProgressBar()
            Toast.makeText(this, "Please provide password", Toast.LENGTH_SHORT).show()
        } else if (confirmPassword == ""){
            hideProgressBar()
            Toast.makeText(this, "Please confirm password", Toast.LENGTH_SHORT).show()
        } else if (password != confirmPassword){
            hideProgressBar()
            Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show()
            register_et_password.text.clear()
            register_et_confirm_password.text.clear()
        }else {
            signUpUser(username, password)
        }
    }

    fun logIn(view: View){
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun signUpUser(username : String, password : String) {
        val newUser = ParseUser()
        newUser.username = username
        newUser.setPassword(password)
        newUser.signUpInBackground(object : SignUpCallback{
            override fun done(e: ParseException?) {
                if (e == null){
                     logInUser(username, password)
                } else {
                    Toast.makeText(applicationContext, e.message, Toast.LENGTH_LONG).show()
                    register_et_password.text.clear()
                    register_et_confirm_password.text.clear()
                }
            }
        })
    }

    private fun logInUser(username : String, password: String){
        ParseUser.logInInBackground(username, password, object : LogInCallback{
            override fun done(user: ParseUser?, e: ParseException?) {
                val intent = Intent(applicationContext, MainActivity::class.java)
                hideProgressBar()
                startActivity(intent)
                finish()
            }
        })
    }

    private fun hideKeyboard(){
        try {
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(currentFocus!!.windowToken, 0)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun hideProgressBar(){
        register_progress_bar.visibility = View.INVISIBLE
        register_progress_bar.isActivated = false
    }

    private fun showProgressBar(){
        register_progress_bar.visibility = View.VISIBLE
        register_progress_bar.isActivated = true
    }
}
