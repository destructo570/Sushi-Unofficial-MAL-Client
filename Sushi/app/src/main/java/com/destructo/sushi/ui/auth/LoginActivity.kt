package com.destructo.sushi.ui.auth

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.destructo.sushi.*
import com.destructo.sushi.databinding.ActivityLoginBinding
import com.destructo.sushi.util.PKCE
import com.destructo.sushi.util.SessionManager
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var loginButton: Button
    private lateinit var loginProgress: ProgressBar

    private val loginViewModel: LoginViewModel by viewModels()

    @Inject
    lateinit var sessionManager: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (sessionManager.checkLogin()) {
            onLoginSuccess()
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
        val intent = Intent(
            Intent.ACTION_VIEW,
            Uri.parse(
                AUTH_CODE_URL
                        + "?response_type=code"
                        + "&client_id=$CLIENT_ID"
                        + "&code_challenge=$pkce"
            )
        )
        startActivity(intent)
    }

    private fun onLoginSuccess() {
        loginViewModel.onAuthenticationCompleted()
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

}