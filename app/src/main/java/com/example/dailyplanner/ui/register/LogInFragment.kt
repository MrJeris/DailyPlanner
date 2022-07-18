package com.example.dailyplanner.ui.register

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import com.example.dailyplanner.R
import com.example.dailyplanner.database.AUTH
import com.example.dailyplanner.database.firebaseAuthWithGoogle
import com.example.dailyplanner.databinding.FragmentLogInBinding
import com.example.dailyplanner.utilities.APP_ACTIVITY
import com.example.dailyplanner.utilities.replaceFragment
import com.example.dailyplanner.utilities.restartActivity
import com.example.dailyplanner.utilities.showToast
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException


class LogInFragment : Fragment(R.layout.fragment_log_in) {
    private var _binding: FragmentLogInBinding? = null
    private val binding get() = _binding!!
    lateinit var launcher: ActivityResultLauncher<Intent>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLogInBinding.inflate(inflater, container, false)
        launchGoogleAccountWindow()
        loginProfile()
        replaceToSignIn()
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun loginProfile() {
        //Авторизация через почту и пароль
        binding.buttonLoginIn.setOnClickListener {
            val email = binding.loginEditEmail.text.toString()
            val password = binding.loginEditPassword.text.toString()

            when {
                email.isEmpty() -> showToast("Введите почту!")
                password.isEmpty() -> showToast("Введите пароль!")
                else -> {
                    AUTH.signInWithEmailAndPassword(email, password).addOnSuccessListener {
                        restartActivity()
                    }.addOnFailureListener {
                        showToast("Невозможно войти в аккаунт.")
                    }
                }
            }
        }

        //Авторизация через учётную запись Google
        binding.googleReg.setOnClickListener {
            createProfileWithGoogle()
        }
    }

    private fun replaceToSignIn() {
        binding.buttonSignScreen.setOnClickListener {
            replaceFragment(SignInFragment(), false)
        }
    }

    private fun createProfileWithGoogle() {
        val signInClient = getClient()
        launcher.launch(signInClient.signInIntent)
    }

    private fun getClient(): GoogleSignInClient {
        val gso = GoogleSignInOptions
            .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        return GoogleSignIn.getClient(APP_ACTIVITY, gso)
    }

    private fun launchGoogleAccountWindow() {
        launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(it.data)
            try {
                val account = task.getResult(ApiException::class.java)
                if (account != null) {
                    firebaseAuthWithGoogle(
                        account.idToken!!,
                        account.displayName.toString(),
                        account.email.toString(),
                        account.photoUrl.toString()
                    ) {
                        showToast("Вход через Google выполнен успешно")
                        restartActivity()
                    }
                }
            } catch (e: ApiException) {
                showToast("Проблемы с входом через Google")
            }
        }
    }
}