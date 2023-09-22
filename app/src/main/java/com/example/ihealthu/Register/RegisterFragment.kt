package com.example.ihealthu.register

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
import com.example.ihealthu.databinding.LoginRegisterBinding

class RegisterFragment : Fragment() {
    private lateinit var registerViewModel: RegisterViewModel
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: LoginRegisterBinding = DataBindingUtil.inflate(
            inflater, R.layout.login_register, container, false)
        val application = requireNotNull(this.activity).application
        val dataSource = AssDatabase.getInstance(application).assDatabaseDao
        val repository = AssDatabaseRepository(dataSource)
        val factory = RegisterViewModelFactory(repository, application)

        val backButton: Button = binding.backBtn
        // Set click listener for back button
        backButton.setOnClickListener {
            // Perform your action here, for example, navigate back
            fragmentManager?.popBackStack()
        }

        registerViewModel = ViewModelProvider(this,factory).get(RegisterViewModel::class.java)
        binding.registerViewModel = registerViewModel
        binding.lifecycleOwner = this

        registerViewModel.messageLiveData.observe(viewLifecycleOwner, Observer { message ->
            message?.let {
                // Use the context of the fragment to show the Toast
                Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
            }
        })

        return binding.root
    }
}