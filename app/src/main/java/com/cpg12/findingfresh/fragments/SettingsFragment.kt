package com.cpg12.findingfresh.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.cpg12.findingfresh.R
import com.cpg12.findingfresh.activities.AddMarketActivity
import com.cpg12.findingfresh.activities.LogInActivity
import com.google.firebase.auth.FirebaseAuth

class SettingsFragment : Fragment() {


/*    val userID = (activity as? MainActivity)?.userId
    val emailID = (activity as? MainActivity)?.emailId*/

    /** retrieves firebase UID and email associated with current user **/
    private val uID = FirebaseAuth.getInstance().currentUser!!.uid
    private val emailID = FirebaseAuth.getInstance().currentUser!!.email

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_settings, container, false)

        val Email = view.findViewById(R.id.usernameTextView) as TextView
        val UserID = view.findViewById(R.id.emailTextView) as TextView

        /** Assigns the textviews to the email and UID**/
        Email.text = uID
        UserID.text = emailID.toString()

        /** Logout button signs out**/
        var logout = view.findViewById<Button>(R.id.logoutBtn)
        logout.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            val intent = Intent(activity, LogInActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
            activity?.finish()
        }


        val tempButton = view.findViewById<Button>(R.id.addMarketBtn)
        tempButton.setOnClickListener {
            val intent = Intent(requireContext(), AddMarketActivity::class.java)
            this.startActivity(intent)
        }

        return view
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



    }
}