package com.example.ihealthu.login

import android.app.Application
import androidx.databinding.Bindable
import androidx.databinding.Observable
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.ihealthu.Global
import com.example.ihealthu.database.AssDatabaseRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class LoginViewModel (private val repository: AssDatabaseRepository, application: Application): AndroidViewModel(application),
    Observable {
    private val _messageLiveData = MutableLiveData<String>()
    private val viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)
    private val _navigatetoHome = MutableLiveData<Boolean>()
    val messageLiveData: LiveData<String>
        get() = _messageLiveData

    @Bindable
    val inputEmail = MutableLiveData<String>()

    @Bindable
    val inputPass = MutableLiveData<String>()

    fun loginButton(){
        val email = inputEmail.value
        val password = inputPass.value

        if(email == null || password == null){
            _messageLiveData.value = "Each field is required."
        }else{
            uiScope.launch {
                val userList = repository.getUsername(email!!)
                if(userList != null){
                    if(userList.password == password){
                        inputEmail.value == null
                        inputPass.value == null
                        _messageLiveData.value = "Login Successfully."
                        _navigatetoHome.value = true
                        Global.loginUser = email
                    }else{
                        _messageLiveData.value = "Invalid username or password."
                    }
                }
                else{
                    _messageLiveData.value = "Invalid username or password."
                }
            }
        }
    }

    override fun removeOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback?) {
    }

    override fun addOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback?) {
    }
}