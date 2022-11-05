package com.cpg12.findingfresh.activities

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.cpg12.findingfresh.R
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth


class LogInActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)


        // if a Firebase session already exists, skip to Main activity
        if (FirebaseAuth.getInstance().uid != null) {

            // set the user and email to intent to the Main activity
            val uID = FirebaseAuth.getInstance().currentUser!!.uid
            val emailID = FirebaseAuth.getInstance().currentUser!!.email

            val intent = Intent(this@LogInActivity, MainActivity::class.java)
            intent.flags =
                Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            intent.putExtra("user_id", uID)
            intent.putExtra("email_id", emailID)
            startActivity(intent)
            finish()
        }

        val emailET = findViewById<EditText>(R.id.emailET)
        val passwordET = findViewById<EditText>(R.id.passwordInputET)
        val loginBtn = findViewById<Button>(R.id.loginButton)
        val registerBtn = findViewById<Button>(R.id.createAccountBtn)

        // let enter on the keyboard act as an OnClick event for the loginBtn
        passwordET.setOnKeyListener(object : View.OnKeyListener {
            override fun onKey(v: View?, keyCode: Int, event: KeyEvent): Boolean {
                // if the event is a key down event on the enter button
                if (event.action == KeyEvent.ACTION_DOWN &&
                    keyCode == KeyEvent.KEYCODE_ENTER
                ) {
                    hideSoftKeyboard()
                    loginBtn.callOnClick()
                    return true
                }
                return false
            }
        })


        loginBtn.setOnClickListener{
            /** Conditional statement to make sure the EditText fields are not empty
             * Trim gets rid of spaces in front or after the text **/
            when {
                TextUtils.isEmpty(emailET.text.toString().trim { it <= ' ' }) -> {
                    Toast.makeText(
                        this@LogInActivity,
                        "Please Enter an Email",
                        Toast.LENGTH_SHORT
                    ).show()
                }

                TextUtils.isEmpty(passwordET.text.toString().trim { it <= ' ' }) -> {
                    Toast.makeText(
                        this@LogInActivity,
                        "Please Enter a Password",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                else -> {

                    val email: String = emailET.text.toString().trim { it <= ' ' }
                    val password: String = passwordET.text.toString().trim { it <= ' ' }

                        /** creates the account using email and password in firebase **/
                        FirebaseAuth.getInstance().signInWithEmailAndPassword(email,password).addOnCompleteListener(
                            OnCompleteListener<AuthResult> { task ->
                                if (task.isSuccessful) {

                                    Toast.makeText(
                                        this@LogInActivity,
                                        "Sign in successful",
                                        Toast.LENGTH_SHORT
                                    ).show()

                                    /** if successfully sign in, intents to main activity and sends the data uid and email there too**/
                                    val intent = Intent(this@LogInActivity, MainActivity::class.java)
                                    intent.flags =
                                        Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                    intent.putExtra("user_id", FirebaseAuth.getInstance().currentUser!!.uid)
                                    intent.putExtra("email_id", email)
                                    startActivity(intent)
                                    finish()

                                } else {
                                    /** If sign in unsuccessful, show toast **/
                                    Toast.makeText(
                                        this@LogInActivity,
                                        task.exception!!.message.toString(),
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            })
                }
            }
        }

        val tempButton = findViewById<Button>(R.id.addMarketBtn)
        tempButton.setOnClickListener {
            val intent = Intent(this, AddMarketActivity::class.java)
            this.startActivity(intent)
        }

        registerBtn.setOnClickListener{
            val intent = Intent(this, RegisterActivity::class.java)
            this.startActivity(intent)
        }
    }


    private fun hideSoftKeyboard() {
        (getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager).apply {
            hideSoftInputFromWindow(currentFocus?.windowToken, 0)
        }
    }


}
