package com.register

import android.database.Observable
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
import com.register.Utils.AuthListener
import com.register.viewModel.AuthViewModel
import com.register.databinding.FragmentPinBinding
import com.register.viewModel.AuthViewModelFactory
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject


class PinFragment : Fragment(), AuthListener {

    private lateinit var binding: FragmentPinBinding
    private lateinit var viewModel: AuthViewModel
    private var disposables = CompositeDisposable()
    @Inject
    lateinit var authViewModelFactory: AuthViewModelFactory


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPinBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (requireActivity().application as MyApplication).appComponent.inject(this)
//        viewModel = ViewModelProvider(this).get(AuthViewModel::class.java)

        viewModel = ViewModelProvider(this, authViewModelFactory).get(AuthViewModel::class.java)
        binding.setPinModel = viewModel

        viewModel.authListener = this

        val pinObservable = RxTextView.afterTextChangeEvents(binding.setYourPin)
            .subscribe ({ event ->
                viewModel.pin(event.editable())
            }, { error ->
                Log.e("Pin Fragment", "Pin Setting Error: ${error.message}")
                Toast.makeText(requireContext(), "Pin Setting Error: ${error.message}", Toast.LENGTH_SHORT).show()
            })
        disposables.add(pinObservable)


        val confirmPinObservable = RxTextView.afterTextChangeEvents(binding.confirmYourPin)
            .subscribe({ event ->
                viewModel.pinChanged(event.editable()!!)
            }, { error ->
                Log.e("Pin Fragment", "Pin confirmation error: ${error.message}")
                Toast.makeText(requireContext(), "Pin confirmation Error: ${error.message}", Toast.LENGTH_SHORT).show()
            })
        disposables.add(confirmPinObservable)

        val submitObservables = RxView.clicks(binding.submitButton)
            .subscribe({
                viewModel.setPin()
            }, { error ->

                Log.e("Pin Fragment", "Button Click Error confirm pin: ${error.message}")
                Toast.makeText(requireContext(), "Button Click Error: ${error.message}", Toast.LENGTH_SHORT).show()
            })
        disposables.add(submitObservables)
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
        binding.submitButton.setOnClickListener {
            findNavController().navigate(R.id.action_pinFragment_to_homeScreen)
            Toast.makeText(context, "Registration Successful", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onFailure(message: String) {
        binding.ProgressBar.visibility = View.GONE
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()

    }


}