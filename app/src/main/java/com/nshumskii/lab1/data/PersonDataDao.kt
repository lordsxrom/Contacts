package com.nshumskii.lab1.data

import androidx.lifecycle.LiveData
import androidx.room.*
import com.nshumskii.lab1.model.Person

@Dao
interface PersonDataDao {

    @Query("SELECT * from personData ORDER BY id, id DESC")
    fun selectAll(): LiveData<List<PersonData>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(person: PersonData)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun update(person: PersonData)

//    @Transaction
//    suspend fun updateAll(persons: List<PersonData>) {
//        deleteAllContacts()
//        insertAll(persons)
//    }

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(persons: List<PersonData>)

    @Query("DELETE from personData")
    suspend fun deleteAll()

    @Query("SELECT * from personData WHERE id = :id LIMIT 1")
    suspend fun selectById(id: Long): PersonData?

    @Delete
    suspend fun delete(person: PersonData)

}