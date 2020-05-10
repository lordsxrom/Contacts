package com.nshumskii.lab1.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.nshumskii.lab1.data.AppDatabase
import com.nshumskii.lab1.data.PersonData
import com.nshumskii.lab1.data.PersonRepository
import com.nshumskii.lab1.model.Event
import com.nshumskii.lab1.model.Person
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class EditViewModel(application: Application) : AndroidViewModel(application) {

    private var personInteractor =
        PersonRepository(AppDatabase.getInstance(application).personDataDao())

    var person: MutableLiveData<PersonData> = MutableLiveData()

    var navEvent: MutableLiveData<Event<Int>> = MutableLiveData()

    private var selectedPerson: PersonData? = null

    fun getPersonById(id: Long) {
        CoroutineScope(IO).launch {
            selectedPerson = personInteractor.getPersonById(id)
            withContext(Main) {
                person.value = selectedPerson
            }
        }
    }

    fun save(
        firstname: String,
        lastname: String,
        phone: String,
        email: String
    ) {
        CoroutineScope(IO).launch {
            if (person.value == null) {
                personInteractor.insert(Person(firstname, lastname, phone, email))
            } else {
                selectedPerson?.let {
                    it.firstname = firstname
                    it.lastname = lastname
                    it.phone = phone
                    it.email = email
                    personInteractor.update(it)
                }
            }
        }

    }

}
