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
import com.example.dailyplanner.database.*
import com.example.dailyplanner.databinding.FragmentSignInBinding
import com.example.dailyplanner.utilities.*
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException

class SignInFragment : Fragment(R.layout.fragment_sign_in) {
    private var _binding: FragmentSignInBinding? = null
    private val binding get() = _binding!!
    lateinit var launcher: ActivityResultLauncher<Intent>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSignInBinding.inflate(inflater, container, false)
        launchGoogleAccountWindow()
        replaceToLogin()
        createProfile()
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun createProfile() {
        //Регистрация через почту и пароль
        binding.buttonLoginIn.setOnClickListener {
            val name = binding.signEditName.text.toString()
            val email = binding.signEditEmail.text.toString()
            val password = binding.signEditPassword.text.toString()
            val passwordRepeat = binding.signEditPasswordRepeat.text.toString()

            if (name.isEmpty()) showToast("Введите имя!")
            else if (email.isEmpty()) showToast("Введите почту!")
            else if (password.isEmpty()) showToast("Введите пароль!")
            else if (passwordRepeat.isEmpty()) showToast("Введите пароль повторно!")
            else {
                if (password != passwordRepeat) {
                    showToast("Пароли не совпадают")
                } else {
                    createUserUploadToDatabase(name, email, password) {
                        showToast("Регистрация прошла успешно")
                        restartActivity()
                    }
                }
            }
        }

        //Регистрация через учётную запись Google
        binding.googleReg.setOnClickListener {
            createProfileWithGoogle()
        }
    }

    private fun replaceToLogin() {
        binding.buttonSignScreen.setOnClickListener {
            replaceFragment(LogInFragment(), false)
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
                    } } } catch (e: ApiException) { showToast("Проблемы с входом через Google") } } }
}