package com.uc3m.searchyourrecipe.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.uc3m.searchyourrecipe.models.User

@Dao
interface UserDAO {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addUser(user: User)

    @Query("SELECT * FROM user_table")
    fun getAllUsers(): LiveData<List<User>>

    @Query("DELETE FROM user_table")
    fun deleteAllUsers()

    @Update
    fun modifyUser(modifyUser: User)

    @Query("SELECT * FROM user_table WHERE email = :email")
    fun getUserByEmail(email: String): User
}