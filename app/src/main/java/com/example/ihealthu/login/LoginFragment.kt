package com.example.ihealthu.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.ihealthu.R
import com.example.ihealthu.database.AssDatabase
import com.example.ihealthu.database.AssDatabaseRepository
import com.example.ihealthu.databinding.LoginBinding
import com.example.ihealthu.home.HomeFragment
import com.example.ihealthu.register.RegisterFragment


class LoginFragment : Fragment() {
    private lateinit var loginViewModel: LoginViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: LoginBinding = DataBindingUtil.inflate(inflater, R.layout.login,container,false)
        val application = requireNotNull(this.activity).application
        val dataSource = AssDatabase.getInstance(application).assDatabaseDao
        val repository = AssDatabaseRepository(dataSource)
        val factory = LoginViewModelFactory(repository, application)

        loginViewModel = ViewModelProvider(this,factory).get(LoginViewModel::class.java)
        binding.loginViewModel = loginViewModel
        binding.lifecycleOwner = this

        val button: Button = binding.logInBtn
        button.setOnClickListener {
            val fragmentManager = parentFragmentManager
            val fragmentTransaction = fragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.framelayout_activitymain, HomeFragment())
            fragmentTransaction.addToBackStack(null)
            fragmentTransaction.commit()
        }

        val button1: Button = binding.goRegisterPageBtn
        button1.setOnClickListener {
            val fragmentManager = parentFragmentManager
            val fragmentTransaction = fragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.framelayout_activitymain, RegisterFragment())
            fragmentTransaction.addToBackStack(null)
            fragmentTransaction.commit()
        }

        loginViewModel.messageLiveData.observe(viewLifecycleOwner, Observer { message ->
            message?.let {
                // Use the context of the fragment to show the Toast
                Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
            }
        })

        return binding.root
    }
}