package com.cpg12.findingfresh.activities

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.cpg12.findingfresh.databinding.ActivityRegisterBinding
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding

    /** Firebase object initialization **/
    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        /** object for firebase authentication**/
        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        /** References the unique id of the current user that is logged in**/
        val uid = auth.currentUser?.uid

        /** Listener for the 'register' button **/
        binding.RegisterAccountBtn.setOnClickListener {
            /** Conditional statement to make sure the EditText fields are not empty
             * Trim gets rid of spaces in front or after the text **/
            when {
                TextUtils.isEmpty(binding.emailET.text.toString().trim { it <= ' ' }) -> {
                    Toast.makeText(
                        this@RegisterActivity,
                        "Please Enter an Email",
                        Toast.LENGTH_SHORT
                    ).show()
                }

                TextUtils.isEmpty(binding.password1ET.text.toString().trim { it <= ' ' }) -> {
                    Toast.makeText(
                        this@RegisterActivity,
                        "Please Enter a Password",
                        Toast.LENGTH_SHORT
                    ).show()
                }

                TextUtils.isEmpty(binding.password2ET.text.toString().trim { it <= ' ' }) -> {
                    Toast.makeText(
                        this@RegisterActivity,
                        "Please Confirm the Password",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            else -> {

                val email: String = binding.emailET.text.toString().trim { it <= ' ' }
                val password: String = binding.password1ET.text.toString().trim { it <= ' ' }
                val passwordConfirm: String = binding.password2ET.text.toString().trim { it <= ' ' }

                /** Conditional statement to check if passwords match**/
                if (password != passwordConfirm) {
                    Toast.makeText(
                        this@RegisterActivity,
                        "Entered passwords do not match",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    /** creates the account using email and password in firebase **/
                    FirebaseAuth.getInstance().createUserWithEmailAndPassword(email,password).addOnCompleteListener(
                        OnCompleteListener<AuthResult> { task ->
                            if (task.isSuccessful) {
                                val firebaseUser: FirebaseUser = task.result!!.user!!
                                Toast.makeText(
                                    this@RegisterActivity,
                                    "Registration is successful",
                                    Toast.LENGTH_SHORT
                                ).show()

                                /** if successfully register, intents to main activity and sends the data uid and email there too**/
                                val intent = Intent(this@RegisterActivity, MainActivity::class.java)
                                intent.flags =
                                    Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                intent.putExtra("user_id", firebaseUser.uid)
                                intent.putExtra("email_id", email)
                                startActivity(intent)
                                finish()

                            } else {
                                /** If registration unsuccessful, show toast **/
                                Toast.makeText(
                                    this@RegisterActivity,
                                    task.exception!!.message.toString(),
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        })
                    }
                }
            }
        }
    }
}
