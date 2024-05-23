package com.register

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.jakewharton.rxbinding2.view.RxView
import com.jakewharton.rxbinding2.widget.RxTextView
import com.register.Model.User
import com.register.Utils.Resource
import com.register.Utils.StreamListener
import com.register.databinding.FragmentRegisterBinding
import com.register.viewModel.AuthViewModel
import com.register.viewModel.AuthViewModelFactory
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject


class RegisterFragment : Fragment() {

    private lateinit var viewModel: AuthViewModel
    private lateinit var binding: FragmentRegisterBinding
    private var disposables = CompositeDisposable()
//    private val streamListener: StreamListener? = null

    @Inject
    lateinit var authViewModelFactory: AuthViewModelFactory


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRegisterBinding.inflate(inflater, container, false)
        return binding.root

    }

    @SuppressLint("CheckResult")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (requireActivity().application as MyApplication).appComponent.inject(this)
//        viewModel = ViewModelProvider(this).get(AuthViewModel::class.java)

        viewModel = ViewModelProvider(requireActivity(), authViewModelFactory).get(AuthViewModel::class.java)
        binding.userRegister = viewModel

//        viewModel.streamListener = this

        binding.registerButton.apply {
            setOnClickListener {
                handleOnClick()
            }
        }

        binding.accountTextClick.setOnClickListener {
            findNavController().navigate(R.id.action_register_to_login)
        }

    }

    @SuppressLint("CheckResult")
    private fun handleOnClick() {
        viewModel.onClickReg()
            .subscribe ({ resource ->
                when (resource) {
                    is Resource.Loading -> {
                        Log.d("User Fragment", "Loading")
//                            Progress bar...
                    }
                    is Resource.Success-> {
                        resource.data.let {
                            viewModel.registerUser()
                            Log.d("Register Fragment", "User: ${resource.data}")
                        }
                            Toast.makeText(context, "Successful", Toast.LENGTH_SHORT).show()
                        findNavController().navigate(R.id.action_register_to_pinFragment)
                        }
//                        Toast.makeText(context, "Welcome: ${newUser}", Toast.LENGTH_SHORT).show()

                    is Resource.Error -> {
//                        errorVisibility()
                        val errorMessage = resource.message
                        Toast.makeText(context, "Failed: $errorMessage", Toast.LENGTH_SHORT).show()
                        Log.e("RegFragment", "Error: $errorMessage")
                    }
                }
            }, { error ->

                Toast.makeText(context, "Registration Failed: ${error.message}", Toast.LENGTH_SHORT).show()
                Log.e("PinFragment", "Error: $error.message")


            })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        disposables.clear()
    }


}






