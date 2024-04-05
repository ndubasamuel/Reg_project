package com.register

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.BaseObservable
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.jakewharton.rxbinding2.view.RxView
import com.jakewharton.rxbinding2.widget.RxTextView
import com.register.Utils.AuthListener
import com.register.databinding.FragmentRegisterBinding
import com.register.viewModel.AuthViewModel
import com.register.viewModel.AuthViewModelFactory
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject


class RegisterFragment : Fragment(), AuthListener {

    private lateinit var viewModel: AuthViewModel
    private lateinit var binding: FragmentRegisterBinding
    private var disposables = CompositeDisposable()
    private val authListener: AuthListener? = null

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
        viewModel = ViewModelProvider(this, authViewModelFactory).get(AuthViewModel::class.java)

        binding.userRegister = viewModel
        viewModel.authListener = this


//      firstNameBinding
        val firstNameDisposable =RxTextView.afterTextChangeEvents(binding.inputFirstName)
            .subscribe{ event ->
                let {
                    viewModel.firstNameChanged(event.editable()!!)
                }
            }
        disposables.add(firstNameDisposable)


//      lastNameBinding
        val lastNameDisposable = RxTextView.afterTextChangeEvents(binding.inputLastName)
            .subscribe { event ->
                let {
                    viewModel.lastNameChanged(event.editable())

                }
            }
        disposables.add(lastNameDisposable)


//      ID Input binding
        val idDisposable = RxTextView.afterTextChangeEvents(binding.inputID)
            .subscribe{ event ->
                let {
                    viewModel.idChanged(event.editable())
                }
            }
        disposables.add(idDisposable)



//      Register user
        val buttonDisposable = RxView.clicks(binding.registerButton)
            .subscribe ({
                    viewModel.registerUser()
        },{ error ->
            Log.e("Register Fragment", "Button Click Error: ${error.message}")
                Toast.makeText(requireContext(), "Button Click Error: ${error.message}", Toast.LENGTH_SHORT).show()
        })
        disposables.add(buttonDisposable)


        binding.accountTextClick.setOnClickListener{
            findNavController().navigate(R.id.action_register_to_login)
        }

    }
    private fun validate() {

        //        firstName check
        if (viewModel.oFirstName.toString().isEmpty()) {
            authListener?.onFailure("First name Required")
            return
        } else { authListener?.onStarted()
        }

        //        LastName check
        if (viewModel.oLastName.toString().isEmpty()) {
            authListener?.onFailure("Last name is Required")
            return
        } else { authListener?.onStarted()
        }

        //        ID check
        if (!viewModel.oUid.toString().matches(Regex("^[a-zA-Z][0-9]{7}$")) || viewModel.oUid.toString().isEmpty()) {
            authListener?.onFailure("Invalid. Must be 8 digits")
            return
        } else {
            authListener?.onStarted()
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        disposables.clear()
    }

    override fun onStarted() {
        binding.ProgressBar.visibility = View.VISIBLE
    }

    override fun onSuccess() {
        binding.ProgressBar.visibility = View.GONE
        findNavController().navigate(R.id.action_register_to_pinFragment)
        Toast.makeText(context, "Successful", Toast.LENGTH_SHORT).show()
    }

    override fun onFailure(message: String) {
        binding.ProgressBar.visibility = View.GONE
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }
}





