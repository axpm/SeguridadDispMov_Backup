package com.uc3m.searchyourrecipe.repository

import androidx.lifecycle.LiveData
import com.uc3m.searchyourrecipe.database.UserDAO
import com.uc3m.searchyourrecipe.models.User

class UserRepository(private val userDao: UserDAO) {

    val readAll:LiveData<List<User>> = userDao.getAllUsers()

    suspend fun addUser(newUser: User){
        userDao.addUser(newUser)
    }

    fun deleteAllUsers(){
        userDao.deleteAllUsers()
    }

    fun modifyUser(modifyUser: User){
        userDao.modifyUser(modifyUser)
    }

    fun getUserByEmail(email: String): User{
        return userDao.getUserByEmail(email)
    }

}
