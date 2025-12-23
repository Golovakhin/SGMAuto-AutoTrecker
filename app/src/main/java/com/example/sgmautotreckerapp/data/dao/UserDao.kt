package com.example.sgmautotreckerapp.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.sgmautotreckerapp.data.entity.User


@Dao
interface UserDao {

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insert(user: User): Long

    @Query("SELECT * FROM users WHERE email = :email")
    suspend fun getUserByEmail(email: String): User?

    @Query("SELECT COUNT(*) FROM users WHERE email = :email")
    suspend fun isEmailExists(email: String): Int

    @Query("SELECT * FROM users WHERE id = :userId")
    suspend fun getUserById(userId: Int): User?

    @Query("UPDATE users SET userName = :userName WHERE id = :userId")
    suspend fun updateUserName(userId: Int, userName: String)

    @Query("UPDATE users SET userName = :userName, phone = :phone, tgID = :tgId WHERE id = :userId")
    suspend fun updateUser(userId: Int, userName: String, phone: String, tgId: String)

    @Query("SELECT * FROM users")
    suspend fun getAllUsers(): List<User>
}
