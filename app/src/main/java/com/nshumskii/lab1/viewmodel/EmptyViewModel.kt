package com.nshumskii.lab1.viewmodel

import android.app.Activity
import android.app.Application
import android.content.Intent
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import com.nshumskii.lab1.data.AppDatabase
import com.nshumskii.lab1.data.PersonRepository
import com.nshumskii.lab1.model.Event
import com.nshumskii.lab1.model.Person
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.IOException

class EmptyViewModel(application: Application) : AndroidViewModel(application) {

    private var personInteractor =
        PersonRepository(AppDatabase.getInstance(application).personDataDao())

    var fileEvent: MutableLiveData<Event<String>> = MutableLiveData()

    companion object {
        const val REQUEST_TO_IMPORT_FILE = 10
        const val ACTION_FINISH_IMPORT_FILE = "finish_import_file"
        const val ACTION_START_IMPORT_FILE = "start_import_file"
        const val ACTION_ERROR_IMPORT_FILE = "error_import_file"
    }

    fun fileToImport(resultCode: Int, data: Intent?) {
        if (Activity.RESULT_OK == resultCode) {
            data?.data?.let { uri ->
                CoroutineScope(Dispatchers.IO).launch {
                    val stream = getApplication<Application>().contentResolver.openInputStream(uri)
                    stream?.let {
                        withContext(Main) { fileEvent.value = Event(ACTION_START_IMPORT_FILE) }

                        val fileContent: String
                        try {
                            val size = it.available()
                            val buffer = ByteArray(size)
                            it.read(buffer)
                            it.close()
                            fileContent = String(buffer)
                        } catch (e: IOException) {
                            withContext(Main) { fileEvent.value = Event(ACTION_ERROR_IMPORT_FILE) }
                            return@launch
                        }

                        val persons = GsonBuilder().create().fromJson<List<Person>>(
                            fileContent,
                            object : TypeToken<List<Person>>() {}.type
                        )

                        personInteractor.insertAll(persons = persons)
                        withContext(Main) { fileEvent.value = Event(ACTION_FINISH_IMPORT_FILE) }
                    }
                }
            }
        }
    }

}