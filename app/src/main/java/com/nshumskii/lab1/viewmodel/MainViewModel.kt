package com.nshumskii.lab1.viewmodel

import android.app.Activity
import android.app.Application
import android.content.Intent
import androidx.lifecycle.AndroidViewModel
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import com.nshumskii.lab1.MainActivity
import com.nshumskii.lab1.data.BaseDB
import com.nshumskii.lab1.interactor.PersonInteractor
import com.nshumskii.lab1.model.Person
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.IOException

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private var mContext = getApplication<Application>().applicationContext

    private var personInteractor = PersonInteractor(BaseDB.invoke().personDataDao())

    fun fileToImport(resultCode: Int, data: Intent?) {
        if (Activity.RESULT_OK == resultCode) {
            data?.data?.let { uri ->
                CoroutineScope(Dispatchers.IO).launch {
                    val stream = mContext.contentResolver.openInputStream(uri)
                    stream?.let {
                        val fileContent: String
                        try {
                            val size = it.available()
                            val intent = Intent(MainActivity.ACTION_START_IMPORT_FILE)
                            intent.putExtra("import_file_size", size)
                            LocalBroadcastManager.getInstance(mContext).sendBroadcast(intent)
                            val buffer = ByteArray(size)
                            it.read(buffer)
                            it.close()
                            fileContent = String(buffer)
                        } catch (e: IOException) {
                            val intent = Intent(MainActivity.ACTION_FINISH_IMPORT_FILE)
                            intent.putExtra("import_success", false)
                            LocalBroadcastManager.getInstance(mContext).sendBroadcast(intent)
                            return@launch
                        }

                        val persons = GsonBuilder().create().fromJson<List<Person>>(
                            fileContent,
                            object : TypeToken<List<Person>>() {}.type
                        )

                        personInteractor.insertAll(persons = persons)

                        val intent = Intent().apply {
                            action = MainActivity.ACTION_FINISH_IMPORT_FILE
                            putExtra("import_success", true)
                        }
                        LocalBroadcastManager.getInstance(mContext).sendBroadcast(intent)
                    }
                }
            }
        }
    }

}