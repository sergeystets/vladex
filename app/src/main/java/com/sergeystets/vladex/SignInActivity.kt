package com.sergeystets.vladex

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.auth.api.signin.GoogleSignInOptions.DEFAULT_SIGN_IN
import com.google.android.gms.common.api.ApiException

class SignInActivity : Activity(), View.OnClickListener {

    private var signInClient: GoogleSignInClient? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)

        findViewById<View>(R.id.sign_in_button).setOnClickListener(this)

        setupGoogleSignInClient()
    }

    private fun setupGoogleSignInClient() {
        val gso = GoogleSignInOptions.Builder(DEFAULT_SIGN_IN)
                .requestEmail()
                .requestProfile()
                .build()

        signInClient = GoogleSignIn.getClient(this, gso)
    }

    override fun onClick(view: View) {
        if (view.id == R.id.sign_in_button) {
            signIn()
        }
    }

    private fun signIn() {
        Log.i(TAG, "signIn in...")
        val signInIntent = signInClient!!.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)
                goToHomeScreen(account)
            } catch (e: ApiException) {
                Log.w(TAG, "failed to sign in", e)
            }
        }
    }

    private fun goToHomeScreen(account: GoogleSignInAccount?) {
        if (account != null) {
            val homeScreen = Intent(this, HomeScreenActivity::class.java)
            startActivity(homeScreen)
            Log.i(TAG, "user with email: ${account.email}' successfully logged in")
        }
    }

    companion object {
        private const val TAG = "SignInActivity"
        private const val RC_SIGN_IN = 9001
    }
}
