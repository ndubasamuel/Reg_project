package com.register

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.register.Utils.AuthListener
import com.register.viewModel.AuthViewModel
import com.register.databinding.FragmentPinBinding


class PinFragment : Fragment(), AuthListener {

    private lateinit var binding: FragmentPinBinding
    private lateinit var viewModel: AuthViewModel
//    @Inject
//    lateinit var authViewModelFactory: AuthViewModelFactory


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPinBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this).get(AuthViewModel::class.java)

//        viewModel = ViewModelProvider(this, authViewModelFactory).get(AuthViewModel::class.java)
//        binding.viewModel = viewModel
    }

    override fun onStarted() {
        TODO("Not yet implemented")
    }

    override fun onSuccess() {
        TODO("Not yet implemented")
    }

    override fun onFailure(message: String) {
        TODO("Not yet implemented")
    }


}