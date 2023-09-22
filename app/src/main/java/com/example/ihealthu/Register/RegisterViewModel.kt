package com.example.ihealthu.register

import android.app.Application
import androidx.databinding.Bindable
import androidx.databinding.Observable
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.ihealthu.database.AssDatabaseRepository
import com.example.ihealthu.database.User
import kotlinx.coroutines.*

class RegisterViewModel(private val repository: AssDatabaseRepository, application: Application): AndroidViewModel(application),
    Observable {

    private val _messageLiveData = MutableLiveData<String>()
    private val viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)
    val messageLiveData: LiveData<String>
        get() = _messageLiveData

    val users = repository.userList

    @Bindable
    val inputUsername = MutableLiveData<String>()

    @Bindable
    val inputEmail = MutableLiveData<String>()

    @Bindable
    val inputContact = MutableLiveData<String>()

    @Bindable
    val inputPass = MutableLiveData<String>()

    @Bindable
    val inputConfirmPass = MutableLiveData<String>()


    fun registerButton(){
        val username = inputUsername.value
        val email = inputEmail.value
        val contact = inputContact.value
        val password = inputPass.value
        val confirmPass = inputConfirmPass.value



        //Log.i("Testing",inputUsername.value.toString())
        if(username == null || email == null || contact == null || password == null || confirmPass == null){
            _messageLiveData.value = "Each field is required."
        }else if(password != confirmPass){
            _messageLiveData.value = "Password field is not match."
        }
        else{
            uiScope.launch {
                val userList = repository.getUsername(username!!)
                if(userList !=null){
                    _messageLiveData.value = "Username has been register before."
                }else{
                    repository.insert(User(null,username, email, contact, password, null))
                    inputUsername.value = null
                    inputEmail.value = null
                    inputContact.value = null
                    inputPass.value = null
                    inputConfirmPass.value = null
                    _messageLiveData.value = "Register successfully."
                }
            }
        }
    }

    fun backToLogin(){

    }

    override fun addOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback?) {}

    override fun removeOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback?) {}
}

