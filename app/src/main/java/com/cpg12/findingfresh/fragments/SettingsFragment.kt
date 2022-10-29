package com.cpg12.findingfresh.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import com.cpg12.findingfresh.R
import com.cpg12.findingfresh.activities.LogInActivity
import com.google.firebase.auth.FirebaseAuth

class SettingsFragment : Fragment() {




/*    val userID = (activity as? MainActivity)?.userId
    val emailID = (activity as? MainActivity)?.emailId*/


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_settings, container, false)

/*        val Email = view.findViewById(R.id.user_Tv) as TextView
        val UserID = view.findViewById(R.id.email_Tv) as TextView

        Email.text = userID.toString()
        UserID.text = emailID.toString()*/

        var logout = view.findViewById<Button>(R.id.logoutBtn)
        logout.setOnClickListener {
            FirebaseAuth.getInstance().signOut()

            val intent = Intent(activity, LogInActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
            activity?.finish()
        }

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



    }
}