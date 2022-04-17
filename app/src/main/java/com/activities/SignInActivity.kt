package com.activities

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.connectionlesson_4_chat_app.MainActivity
import com.example.connectionlesson_4_chat_app.R
import com.example.connectionlesson_4_chat_app.databinding.ActivitySignInBinding
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.database.*
import com.models.User
import com.utils.SharedPreference


class SignInActivity : AppCompatActivity(R.layout.activity_sign_in) {

    private val binding: ActivitySignInBinding by viewBinding()
    lateinit var googleSignInClient: GoogleSignInClient
    var RC_SIGN_IN = 1
    private val TAG = "MainActivity"
    lateinit var auth: FirebaseAuth
    lateinit var firebaseDatabase: FirebaseDatabase
    var userGmail = ""

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_sign_in)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        }
        auth = FirebaseAuth.getInstance()
        firebaseDatabase = FirebaseDatabase.getInstance()
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.your_web_client_id))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this, gso)

        binding.signInBtn.setOnClickListener {
            signIn()
        }

    }

    private fun signIn() {
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                // Google Sign In was successful, authenticate with Firebase
                val account = task.getResult(ApiException::class.java)!!
                Log.d(TAG, "firebaseAuthWithGoogle:" + account.id)
                firebaseAuthWithGoogle(account.idToken!!)
            } catch (e: ApiException) {
                // Google Sign In failed, update UI appropriately
                Log.w(TAG, "Google sign in failed", e)
            }
        }
    }

    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    lateinit var reference: DatabaseReference
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "signInWithCredential:success")
                    val currentUser = auth.currentUser

                    val user = User()
                    user.email = currentUser?.email
                    user.displayName = currentUser?.displayName
                    user.photoUrl = currentUser?.photoUrl.toString()
                    user.uid = currentUser?.uid
                    user.userState = "Online"
                    user.lastTime = ""
                    user.lastMessage = ""
                    userGmail = currentUser!!.email!!
                    reference = firebaseDatabase.getReference("Users")

                    reference.addValueEventListener(object : ValueEventListener {
                        override fun onDataChange(snapshot: DataSnapshot) {
                            val users = snapshot.children
                            var userList = ArrayList<String>()
                            for (us in users) {
                                val userValue = us.getValue(User::class.java)
                                if (userValue!!.uid != null && userValue.uid!!.isNotEmpty()) {
                                    userList.add(userValue.uid!!)
                                }
                            }

                            if (userList.size == 0) {
                                reference.child(currentUser!!.uid).setValue(user)
                            } else {
                                val contains = userList.contains(user.uid)
                                if (!contains) {
                                    reference.child(currentUser!!.uid).setValue(user)
                                }
                            }


                        }

                        override fun onCancelled(error: DatabaseError) {
                        }

                    })
                    updateUI()

                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "signInWithCredential:failure", task.exception)
//                        updateUI(null)
                    Toast.makeText(this, task.exception?.message, Toast.LENGTH_SHORT).show()
                }
            }
    }

    fun updateUI() {
        SharedPreference.init(this)
        SharedPreference.signUp = true
        SharedPreference.userExist = true
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}