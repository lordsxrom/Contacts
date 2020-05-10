package com.nshumskii.lab1.interactor

import com.nshumskii.lab1.data.PersonData
import com.nshumskii.lab1.data.PersonDataDao
import com.nshumskii.lab1.model.Person

class PersonInteractor(
    private val personDataDao: PersonDataDao
) {

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


}