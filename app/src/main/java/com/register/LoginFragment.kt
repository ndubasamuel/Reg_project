package com.register

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.jakewharton.rxbinding2.view.RxView
import com.jakewharton.rxbinding2.widget.RxTextView
import com.register.Utils.StreamListener
import com.register.viewModel.AuthViewModel
import com.register.databinding.FragmentLoginBinding
import com.register.viewModel.AuthViewModelFactory
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject


class LoginFragment : Fragment(){

    private lateinit var binding: FragmentLoginBinding
    private lateinit var viewModel: AuthViewModel
    private val disposables = CompositeDisposable()

    @Inject
    lateinit var authViewModelFactory: AuthViewModelFactory

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (requireActivity().application as MyApplication).appComponent.inject(this)
//        viewModel = ViewModelProvider(this).get(AuthViewModel::class.java)

        viewModel = ViewModelProvider(this, authViewModelFactory).get(AuthViewModel::class.java)
        binding.loginModel = this.viewModel


//      Login action
        val loginDisposable = RxView.clicks(binding.loginButton)
            .subscribe({
                viewModel.loginUser()
            },{ error ->
        Log.e("Login Fragment", "Button Click Error: ${error.message}")
        Toast.makeText(requireContext(), "Button Click Error: ${error.message}", Toast.LENGTH_SHORT).show()
    })
        disposables.add(loginDisposable)

    }

}