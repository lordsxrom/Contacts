package com.nshumskii.lab1.viewmodel

import android.app.Application
import android.app.Dialog
import android.content.Intent
import android.view.Window
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.nshumskii.lab1.App
import com.nshumskii.lab1.R
import com.nshumskii.lab1.data.BaseDB
import com.nshumskii.lab1.data.PersonData
import com.nshumskii.lab1.interactor.PersonInteractor
import com.nshumskii.lab1.model.Event
import com.nshumskii.lab1.model.Person
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class ListViewModel(application: Application) : AndroidViewModel(application) {

    private val context = getApplication<Application>().applicationContext

    private var personInteractor = PersonInteractor(BaseDB.invoke().personDataDao())

    var persons: MutableLiveData<List<PersonData>> = MutableLiveData()

    var navEvent: MutableLiveData<Event<Int>> = MutableLiveData()

    var personsList: List<PersonData>? = null

    private val personsObserver: Observer<List<PersonData>> = Observer {
        if (it.isNullOrEmpty()) {
            navEvent.value = Event(R.id.action_listFragment_to_emptyFragment)
        } else {
            persons.value = it
            personsList = it
        }
    }

    init {
        personInteractor.getContacts().observeForever(personsObserver)
    }

    fun search(target: String) {
        CoroutineScope(IO).launch {
            val search = personsList?.filter {
                (it.firstname + " " + it.lastname).toLowerCase().contains(target.toLowerCase())
            }
            withContext(Main) {
                persons.value = search
            }
        }
    }

    fun sort() {
        CoroutineScope(IO).launch {
            val sort =
                personsList?.sortedWith(compareBy(PersonData::lastname))
            withContext(Main) {
                persons.value = sort
            }
        }
    }

    fun deleteAll() {
        CoroutineScope(IO).launch {
            personInteractor.deleteAll()
        }
    }

    fun insertNew() {
        navEvent.value = Event(R.id.action_listFragment_to_editFragment)
    }

    override fun onCleared() {
        super.onCleared()
        personInteractor.getContacts().removeObserver(personsObserver)
    }

}
