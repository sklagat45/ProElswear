package com.sklagat46.proelswear.activities

import android.os.Bundle
import android.text.TextUtils
import android.view.WindowManager
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.sklagat46.proelswear.R
import com.sklagat46.proelswear.firebase.FirestoreClass
import com.sklagat46.proelswear.models.User
import kotlinx.android.synthetic.main.activity_sign_in.*
import kotlinx.android.synthetic.main.activity_sign_up.*

class SignUpActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN)

        setUpActionBar()

        // Click event for sign-up button.
        btn_sign_up.setOnClickListener {
            registerUser()
        }
    }
    private fun setUpActionBar() {

        setSupportActionBar(toolbar_sign_in_activity)

        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.back_btn)
        }
        toolbar_sign_up_activity.setNavigationOnClickListener {
            onBackPressed()
        }
    }


        /**
         * A function to register a user to our app using the Firebase.
         * For more details visit: https://firebase.google.com/docs/auth/android/custom-auth
         */
        fun registerUser() {
            // Here we get the text from editText and trim the space
            val name: String = et_name.text.toString().trim { it <= ' ' }
            val email: String = et_email.text.toString().trim { it <= ' ' }
            val password: String = et_password.text.toString().trim { it <= ' ' }

            if (validateForm(name, email, password)) {
                // Show the progress dialog.
                showProgressDialog(resources.getString(R.string.please_wait))
                FirebaseAuth.getInstance()
                    .createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener { task ->

                        // If the registration is successfully done
                        if (task.isSuccessful) {
                            // Firebase registered user
                            val firebaseUser: FirebaseUser = task.result!!.user!!
                            // Registered Email
                            val registeredEmail = firebaseUser.email!!

                            val user = User(
                                firebaseUser.uid, name, registeredEmail
                            )

                            // call the registerUser function of FirestoreClass to make an entry in the database.
                            FirestoreClass().registerUser(this, user)
                        } else {
                            Toast.makeText(
                                this@SignUpActivity,
                                "Registration Failed",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
            }


        }
        /**
         * A function to validate the entries of a new user.
         */
        fun validateForm(name: String, email: String, password: String): Boolean {
            return when {
                TextUtils.isEmpty(name) -> {
                    showErrorSnackBar("Please enter name.")
                    false
                }
                TextUtils.isEmpty(email) -> {
                    showErrorSnackBar("Please enter email.")
                    false
                }
                TextUtils.isEmpty(password) -> {
                    showErrorSnackBar("Please enter password.")
                    false
                }
                else -> {
                    true
                }
            }

        }
        /**
         * A function to be called the user is registered successfully and entry is made in the firestore database.
         */
        fun userRegisteredSuccess() {

            Toast.makeText(
                this@SignUpActivity,
                "You have successfully registered.",
                Toast.LENGTH_SHORT
            ).show()

            // Hide the progress dialog
            hideProgressDialog()

            /**
             * Here the new user registered is automatically signed-in so we just sign-out the user from firebase
             * and send him to Intro Screen for Sign-In
             */
            FirebaseAuth.getInstance().signOut()
            // Finish the Sign-Up Screen
            finish()
        }

}