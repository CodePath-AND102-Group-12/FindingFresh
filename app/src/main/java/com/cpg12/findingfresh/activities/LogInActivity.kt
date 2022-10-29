package com.cpg12.findingfresh.activities

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.cpg12.findingfresh.R
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser


class LogInActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)


        val usernameET = findViewById<EditText>(R.id.usernameET)
        val passwordET = findViewById<EditText>(R.id.passwordInputET)
        val loginBtn = findViewById<Button>(R.id.loginButton)
        val registerBtn = findViewById<Button>(R.id.createAccountBtn)



        loginBtn.setOnClickListener{
            /** Conditional statement to make sure the EditText fields are not empty
             * Trim gets rid of spaces in front or after the text **/
            when {
                TextUtils.isEmpty(usernameET.text.toString().trim { it <= ' ' }) -> {
                    Toast.makeText(
                        this@LogInActivity,
                        "Please Enter a Email",
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

                    val email: String = usernameET.text.toString().trim { it <= ' ' }
                    val password: String = passwordET.text.toString().trim { it <= ' ' }

                        /** creates the account using email and password in firebase **/
                        FirebaseAuth.getInstance().signInWithEmailAndPassword(email,password).addOnCompleteListener(
                            OnCompleteListener<AuthResult> { task ->
                                if (task.isSuccessful) {

                                    Toast.makeText(
                                        this@LogInActivity,
                                        "Sign successful",
                                        Toast.LENGTH_SHORT
                                    ).show()

                                    /** if sucessfully sign in, intents to main activity and sends the data uid and email there too**/
                                    val intent = Intent(this@LogInActivity, MainActivity::class.java)
                                    intent.flags =
                                        Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                    intent.putExtra("user_id", FirebaseAuth.getInstance().currentUser!!.uid)
                                    intent.putExtra("email_id", email)
                                    startActivity(intent)
                                    finish()

                                } else {
                                    /** If sign in unsucessfull, show toast **/
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

        //TODO: Relocate "list market" button to better place
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



}
