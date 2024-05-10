package com.rwmendes.modulodelogin.ui.login

import android.app.Activity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.rwmendes.modulodelogin.R
import com.rwmendes.modulodelogin.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {

    private lateinit var loginViewModel: LoginViewModel
    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Usando View Binding para inflar o layout
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Referências aos elementos de UI
        val usernameEditText = binding.username
        val passwordEditText = binding.password
        val loginButton = binding.login
        val loadingProgressBar = binding.loading

        // Coloca foco no campo de nome de usuário inicialmente
        usernameEditText.requestFocus()

        // Inicialize o ViewModel usando ViewModelFactory
        loginViewModel = ViewModelProvider(this, LoginViewModelFactory(applicationContext))
            .get(LoginViewModel::class.java)

        // Observa mudanças no estado do formulário de login
        loginViewModel.loginFormState.observe(this@LoginActivity, Observer { loginState ->
            loginState ?: return@Observer
            loginButton.isEnabled = loginState.isDataValid

            if (loginState.usernameError != null) {
                usernameEditText.error = getString(loginState.usernameError)
            }
            if (loginState.passwordError != null) {
                passwordEditText.error = getString(loginState.passwordError)
            }
        })

        // Observa mudanças no resultado do login
        loginViewModel.loginResult.observe(this@LoginActivity, Observer { loginResult ->
            loginResult ?: return@Observer
            loadingProgressBar.visibility = View.GONE

            when (loginResult) {
                is LoginResult.Success -> {
                    updateUiWithUser(loginResult.userView)
                    setResult(Activity.RESULT_OK)
                    finish()
                }
                is LoginResult.Error -> {
                    showLoginFailed(loginResult.errorType)
                }
            }
        })

        // Monitora as alterações de texto e ações do teclado
        usernameEditText.afterTextChanged {
            loginViewModel.loginDataChanged(usernameEditText.text.toString(), passwordEditText.text.toString())
        }

        passwordEditText.apply {
            afterTextChanged {
                loginViewModel.loginDataChanged(usernameEditText.text.toString(), it.toString())
            }

            setOnEditorActionListener { _, actionId, _ ->
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    loadingProgressBar.visibility = View.VISIBLE
                    loginViewModel.login(usernameEditText.text.toString(), passwordEditText.text.toString())
                }
                false
            }
        }

        // Listener para o botão de login
        loginButton.setOnClickListener {
            loadingProgressBar.visibility = View.VISIBLE
            loginViewModel.login(usernameEditText.text.toString(), passwordEditText.text.toString())
        }
    }

    private fun updateUiWithUser(model: LoggedInUserView) {
        val welcome = getString(R.string.welcome)
        val displayName = model.displayName
        Toast.makeText(applicationContext, "$welcome $displayName", Toast.LENGTH_LONG).show()
    }

    private fun showLoginFailed(@StringRes errorString: Int) {
        Toast.makeText(applicationContext, getString(errorString), Toast.LENGTH_SHORT).show()
    }

    fun EditText.afterTextChanged(afterTextChanged: (String) -> Unit) {
        addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {
                afterTextChanged.invoke(s.toString())
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
        })
    }
}
