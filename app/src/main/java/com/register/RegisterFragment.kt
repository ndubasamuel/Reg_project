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
import com.register.Utils.StreamListener
import com.register.databinding.FragmentRegisterBinding
import com.register.viewModel.AuthViewModel
import com.register.viewModel.AuthViewModelFactory
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject


class RegisterFragment : Fragment(), StreamListener{

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

        viewModel = ViewModelProvider(this, authViewModelFactory).get(AuthViewModel::class.java)

        binding.userRegister = viewModel

        viewModel.streamListener = this


//      firstNameBinding
        val firstNameDisposable = RxTextView.afterTextChangeEvents(binding.inputFirstName)
            .skipInitialValue()
            .subscribe({ event ->
                viewModel.firstNameChanged(event.editable()!!)
            }, { error ->
                Log.e("Register Fragment", "First Name Input: $ ${error.message}")
                Toast.makeText(requireContext(), "First Name input Error", Toast.LENGTH_SHORT).show()

            })
        disposables.add(firstNameDisposable)


//      lastNameBinding
        val lastNameDisposable = RxTextView.afterTextChangeEvents(binding.inputLastName)
            .skipInitialValue()
            .subscribe({ event ->
                viewModel.lastNameChanged(event.editable())
            }, { error ->
                Log.e("Register Fragment", "Last Name Input: $ ${error.message}")
                Toast.makeText(requireContext(), "Last Name input Error", Toast.LENGTH_SHORT).show()

            })
        disposables.add(lastNameDisposable)


//      ID Input binding
        val idDisposable = RxTextView.afterTextChangeEvents(binding.inputID)
            .skipInitialValue()
            .subscribe({ event ->
                viewModel.idChanged(event.editable())

            }, { error ->
                Log.e("Register Fragment", "ID Input: $ ${error.message}")
                Toast.makeText(requireContext(), "ID input Error", Toast.LENGTH_SHORT).show()

            })
        disposables.add(idDisposable)


//      Register user
        val buttonDisposable = RxView.clicks(binding.registerButton)
            .subscribe({
                viewModel.onClickReg()
            }, { error ->
                Log.e("Register Fragment", "Button Click Error: ${error.message}")
                Toast.makeText(requireContext(), "Button Click Error: ${error.message}", Toast.LENGTH_SHORT).show()
            })
        disposables.add(buttonDisposable)


        binding.accountTextClick.setOnClickListener {
            findNavController().navigate(R.id.action_register_to_login)
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

        findNavController().navigate(RegisterFragmentDirections.actionRegisterToPinFragment())
        Toast.makeText(context, "Successful", Toast.LENGTH_SHORT).show()
    }

    override fun onFailure(message: String) {
        binding.ProgressBar.visibility = View.GONE
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

}






