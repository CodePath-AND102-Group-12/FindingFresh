package com.cpg12.findingfresh.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.cpg12.findingfresh.R



class LogInActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val btn = findViewById<Button>(R.id.loginButton)
        btn.setOnClickListener{
            val intent = Intent(this, MainActivity::class.java)
            this.startActivity(intent)
        }

        //TODO: Relocate "list market" button to better place
        val tempButton = findViewById<Button>(R.id.addMarketBtn)
        tempButton.setOnClickListener {
            val intent = Intent(this, AddMarketActivity::class.java)
            this.startActivity(intent)
        }

        val registerBtn = findViewById<Button>(R.id.createAccountBtn)
        registerBtn.setOnClickListener{
            val intent = Intent(this, RegisterActivity::class.java)
            this.startActivity(intent)
        }
    }
}
