package com.nshumskii.lab1.data

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface PersonDataDao {

    @Query("SELECT * from persons_table ORDER BY id, id DESC")
    fun selectAll(): LiveData<List<PersonData>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(person: PersonData)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun update(person: PersonData)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(persons: List<PersonData>)

    @Query("DELETE from persons_table")
    suspend fun deleteAll()

    @Query("SELECT * from persons_table WHERE id = :id LIMIT 1")
    suspend fun selectById(id: Long): PersonData?

    @Delete
    suspend fun delete(person: PersonData)

}