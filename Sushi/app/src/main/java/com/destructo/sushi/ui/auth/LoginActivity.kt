package com.destructo.sushi.ui.auth

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.Window
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.browser.customtabs.CustomTabsIntent
import androidx.databinding.DataBindingUtil
import com.destructo.sushi.AUTH_CODE_URL
import com.destructo.sushi.CLIENT_ID
import com.destructo.sushi.MainActivity
import com.destructo.sushi.R
import com.destructo.sushi.databinding.ActivityLoginBinding
import com.destructo.sushi.network.Status
import com.destructo.sushi.util.SessionManager
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var loginButton: Button
    private lateinit var loginProgress: ProgressBar
    private lateinit var welcomeTxt:TextView

    private val loginViewModel: LoginViewModel by viewModels()

    @Inject
    lateinit var sessionManager: SessionManager

    @RequiresApi(Build.VERSION_CODES.P)
    override fun onCreate(savedInstanceState: Bundle?) {

        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= 30) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        }else{
            requestWindowFeature(Window.FEATURE_NO_TITLE)
            window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN)
        }
        super.onCreate(savedInstanceState)

        if (sessionManager.checkLogin()) {
            if(sessionManager.isTokenExpired()){
                binding = DataBindingUtil.setContentView(this, R.layout.activity_login)

                loginButton = binding.malLoginButton
                loginButton.visibility = View.GONE
                loginProgress = binding.loginProgress
                welcomeTxt = binding.welcomeTxt
                welcomeTxt.visibility = View.GONE

                loginViewModel.refreshAuthToken(sessionManager.getLatestRefreshToken())

                Toast.makeText(this, "Please wait refreshing token", Toast.LENGTH_LONG).show()

                loginViewModel.refreshComplete.observe(this){ resource ->
                    when(resource.status){
                        Status.LOADING -> {
                            loginProgress.visibility = View.VISIBLE
                        }
                        Status.SUCCESS -> {
                            onLoginSuccess()
                        }
                        Status.ERROR -> {
                            Toast.makeText(this, "Failed to refresh token", Toast.LENGTH_LONG)
                                .show()
                        }

                    }
                }


            }else{
                onLoginSuccess()
            }

        } else {
            binding = DataBindingUtil.setContentView(this, R.layout.activity_login)

            loginButton = binding.malLoginButton
            loginProgress = binding.loginProgress

            loginButton.setOnClickListener {
                getAuthCode(sessionManager.getPkce())
            }

            loginViewModel.authenticationCode.observe(this, { authCode ->
                authCode?.let {
                    loginViewModel.onAuthentication(it)
                }
            })

            loginViewModel.authComplete.observe(this){
                it?.let {
                    onLoginSuccess()
                }
            }

        }
    }

    override fun onResume() {
        super.onResume()
        val uri = intent.data
        uri?.let {
            loginProgress.visibility = View.VISIBLE
            loginViewModel.extractAuthCode(it)
        }
    }

    private fun getAuthCode(pkce: String?) {
        if(!pkce.isNullOrBlank()){
            launchBrowser(pkce)
        }else{
            sessionManager.generatePkce()
            launchBrowser(sessionManager.getPkce()!!)
        }
    }

    private fun launchBrowser(pkce: String){

        val builder = CustomTabsIntent.Builder()
        val customTabIntent = builder.build()
        customTabIntent.launchUrl(
            this,
            Uri.parse(
                AUTH_CODE_URL
                        + "?response_type=code"
                        + "&client_id=$CLIENT_ID"
                        + "&code_challenge=$pkce"
            )
        )

    }

    private fun onLoginSuccess() {
        loginViewModel.onAuthenticationCompleted()
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

}