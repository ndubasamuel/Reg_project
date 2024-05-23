package com.register

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.register.Model.User
import com.register.Model.UserAdapter.UserAdapter
import com.register.databinding.FragmentHomeScreenBinding
import com.register.viewModel.AuthViewModel
import com.register.viewModel.AuthViewModelFactory
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject


class HomeScreen : Fragment() {
    private lateinit var viewModel: AuthViewModel
    private lateinit var binding: FragmentHomeScreenBinding
    private var disposables = CompositeDisposable()
    private lateinit var adapter: UserAdapter
    val user: User? = null

    @Inject
    lateinit var authViewModelFactory: AuthViewModelFactory


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentHomeScreenBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("CheckResult")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (requireActivity().application as MyApplication).appComponent.inject(this)

        viewModel = ViewModelProvider(requireActivity(), authViewModelFactory).get(AuthViewModel::class.java)

        setUpRecyclerView()
        viewUser()

    }

    fun setUpRecyclerView() {
        adapter = UserAdapter()
        val recyclerView = binding.greetingView
        recyclerView.layoutManager = LinearLayoutManager(activity)
        binding.greetingView.adapter = adapter

    }
    private fun viewUser() {
        user?.let {
            Log.d("HomeScreen", "Adapter Data")
            viewModel.getUsers()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ userName ->
                           adapter.differ.submitList(userName)
                }, { error ->
                    // Handle error
                    Log.e("HomeFragment", "Error observing user name: $error")
                })
        }?.let {
            disposables.add(
                it
            )
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        disposables.clear()
    }




}