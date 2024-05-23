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
import com.register.databinding.FragmentPinBinding
import com.register.viewModel.AuthViewModel
import com.register.viewModel.AuthViewModelFactory
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject


class PinFragment : Fragment() {

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

        viewModel = ViewModelProvider(requireActivity(), authViewModelFactory).get(AuthViewModel::class.java)
        binding.setPinModel = viewModel

        binding.submitButton.apply {
            setOnClickListener {
                handleOnClickPin()
            }
        }

    }
    @SuppressLint("CheckResult")
    private fun handleOnClickPin() {
        viewModel.onClickPin()
            .subscribe ({ resource ->
                when (resource) {
                    is Resource.Loading -> {
                        Log.d("User Fragment", "Loading")
//                            Progress bar...
                    }
                    is Resource.Success -> {
                        resource.let {
                            viewModel.registerUser()
                        }
                        findNavController().navigate(R.id.action_pinFragment_to_homeScreen)
                        Toast.makeText(context, "Welcome", Toast.LENGTH_SHORT).show()
                    }
                    is Resource.Error -> {
//                        errorVisibility()
                        val errorMessage = resource.message
                        Toast.makeText(context, "Failed: $errorMessage", Toast.LENGTH_SHORT).show()
                        Log.e("PinFragment", "Error: $errorMessage")

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

