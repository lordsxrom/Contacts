package com.nshumskii.lab1.data

import com.nshumskii.lab1.models.Person

class PersonRepository(private val personDataDao: PersonDataDao) {

    fun getContacts() = personDataDao.selectAll()

    suspend fun insertAll(persons: List<Person>) =
        personDataDao.insertAll(persons.map { PersonData(it) })

    suspend fun getPersonById(id: Long): PersonData? = personDataDao.selectById(id)

    suspend fun deleteAll() = personDataDao.deleteAll()

    suspend fun insert(person: Person) {
        personDataDao.insert(PersonData(person))
    }

    suspend fun update(person: PersonData) {
        personDataDao.update(person)
    }

    suspend fun delete(person: PersonData) {
        personDataDao.delete(person)
    }

    companion object {

        @Volatile private var instance: PersonRepository? = null

        fun getInstance(plantDao: PersonDataDao) =
            instance ?: synchronized(this) {
                instance ?: PersonRepository(plantDao).also { instance = it }
            }
    }

}