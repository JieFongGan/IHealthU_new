package com.example.ihealthu.database

import android.util.Log

class AssDatabaseRepository (private val dao: AssDatabaseDao) {
    val userList = dao.getAllUser()

    suspend fun insert(user: User){
        return dao.insert(user)
    }

    suspend fun getUsername(username: String):User?{
        return dao.getUsername(username)
    }

    suspend fun updateProfilePic(username: String, userImage: String){
        return dao.updateProfilePic(username, userImage)
    }

    suspend fun getLoginUserImage(username: String): String?{
        Log.i("Testing", "Repo" + username)
        return dao.getLoginUserImage(username)
    }
}
