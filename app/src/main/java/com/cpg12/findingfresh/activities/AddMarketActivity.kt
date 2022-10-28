package com.cpg12.findingfresh.activities


import android.app.Dialog
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Window
import android.widget.Toast
import com.cpg12.findingfresh.R
import com.cpg12.findingfresh.database.Markets
import com.cpg12.findingfresh.databinding.ActivityAddMarketBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.net.URI


class AddMarketActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddMarketBinding

    /** Firebase object initialization **/
    private lateinit var auth : FirebaseAuth
    private lateinit var databaseReference: DatabaseReference
    private lateinit var storageReference: StorageReference

    /** pointer to location of image**/
    private lateinit var imageUri: Uri

    private lateinit var dialog: Dialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddMarketBinding.inflate(layoutInflater)
        setContentView(binding.root)

        /** object for firebase authentication**/
        auth  = FirebaseAuth.getInstance()

        /** References the unique id of the current user that is logged in**/
        val uid = auth.currentUser?.uid

        /** References the node to which market data is stored**/
        databaseReference = FirebaseDatabase.getInstance().getReference("Markets")

        /** Upload image button chooses 1 picture from your gallery**/
        binding.marketUploadImageBtn.setOnClickListener {
            selectImage()
        }


        /** Cancel button intents into main listing**/
        binding.marketCancelBtn.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            this.startActivity(intent)
        }


        /** Submit button saves market to firebase**/
        binding.marketSubmitBtn.setOnClickListener {

            showProgressBar()

            val marketName = binding.marketNameET.text.toString()
            val marketLocation = binding.marketLocationET.text.toString()
            val marketEmail = binding.marketEmailET.text.toString()
            val marketContactName = binding.marketContactNameET.text.toString()
            val marketCategory = binding.marketCategoryET.text.toString()
            val marketDescription = binding.marketDescriptionET.text.toString()
            //TODO: using a datetime object instead of string?
            val marketDateTime = binding.marketDateET.text.toString()

            /** create instance of market class**/
            val markets = Markets(
                marketName,
                marketLocation,
                marketEmail,
                marketContactName,
                marketCategory,
                marketDescription,
                marketDateTime)

            /** Creates the entity(child) into the node based on the market name**/
            databaseReference.child(marketName).setValue(markets).addOnCompleteListener {
                if (it.isSuccessful){
                    uploadImage()
                    hideProgressBar()
                    Toast.makeText(this@AddMarketActivity,"Market Listing Successful",Toast.LENGTH_LONG).show()


                    /**Go back to main screen**/
                    val intent = Intent(this, MainActivity::class.java)
                    this.startActivity(intent)

                }else{
                    hideProgressBar()
                    Toast.makeText(this@AddMarketActivity,"Failed to list your market",Toast.LENGTH_SHORT).show()
                }
            }

            /** temporary because no authentication implemented yet**/
/*            if (uid != null){
               databaseReference.child(uid).setValue(markets).addOnCompleteListener {
                    if (it.isSuccessful){
                        uploadPic()
                        hideProgressBar()
                        val intent = Intent(this, MainActivity::class.java)
                        this.startActivity(intent)
                    }else{
                        hideProgressBar()
                        Toast.makeText(this@AddMarketActivity,"Failed to list your market",Toast.LENGTH_SHORT).show()
                    }
                }
            }*/

        }


    }

    /** Function intents into image gallery**/
    private fun selectImage() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(intent, 100)
    }


    /** Function for when image selected from gallery
    Shows the image in the imageView
    Assigns pointer imageUri the image **/
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?){
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 100 && resultCode == RESULT_OK){

            imageUri = data?.data!!
            binding.marketPictureIV.setImageURI(imageUri)

        }
    }

    private fun uploadImage() {
        val marketName = binding.marketNameET.text.toString()


        // imageUri = Uri.parse("android.resource://$packageName/${R.drawable.fish}")
        // storageReference = FirebaseStorage.getInstance().getReference("Markets/"+auth.currentUser?.uid)

        /** References where to store the image**/
        storageReference = FirebaseStorage.getInstance().getReference("Markets/$marketName")

        /** putFile() stores in firebase **/
        storageReference.putFile(imageUri).addOnSuccessListener {
            Toast.makeText(this@AddMarketActivity,"Image upload Successful",Toast.LENGTH_LONG).show()
        }.addOnFailureListener {
            hideProgressBar()
            Toast.makeText(this@AddMarketActivity,"Image upload Failed",Toast.LENGTH_LONG).show()
        }
    }

    private fun showProgressBar(){
        dialog = Dialog(this@AddMarketActivity)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.dialog_wait)
        dialog.setCanceledOnTouchOutside(false)
        dialog.show()
    }

    private fun hideProgressBar(){
        dialog.dismiss()
    }
}