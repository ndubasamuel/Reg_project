package com.register

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.register.Utils.AuthListener
import com.register.viewModel.AuthViewModel
import com.register.databinding.FragmentRegisterBinding
import com.register.viewModel.AuthViewModelFactory
import io.reactivex.disposables.Disposable
import javax.inject.Inject


class RegisterFragment : Fragment(), AuthListener {

    private lateinit var viewModel: AuthViewModel
    private lateinit var binding: FragmentRegisterBinding
//    private var disposable: Disposable? = null

    @Inject
    lateinit var authViewModelFactory: AuthViewModelFactory


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRegisterBinding.inflate(inflater, container, false)

        return binding.root

    }

    @RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
    @SuppressLint("CheckResult")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (requireActivity().application as MyApplication).appComponent.inject(this)
        
//        viewModel = ViewModelProvider(this).get(AuthViewModel::class.java)
        viewModel = ViewModelProvider(this, authViewModelFactory).get(AuthViewModel::class.java)

        binding.userRegister = viewModel
        binding.registerButton.setOnClickListener{
            viewModel.registerUser()
        }


    }

//    override fun onDestroy() {
//        super.onDestroy()
////        disposable.clear()
//    }
    override fun onStarted() {
        binding.ProgressBar.visibility = View.VISIBLE
    }
    override fun onSuccess() {
        binding.ProgressBar.visibility = View.GONE
        findNavController().navigate(R.id.action_register_to_pinFragment)
    }

    override fun onFailure(message: String) {
        binding.ProgressBar.visibility = View.GONE
    }




}