package com.nshumskii.lab1.data

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import com.nshumskii.lab1.App
import com.nshumskii.lab1.model.Person
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


@Database(entities = [PersonData::class], version = 1, exportSchema = true)
abstract class BaseDB : RoomDatabase() {

    abstract fun personDataDao(): PersonDataDao

    companion object {
        private val initCallback = object : Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
//                CoroutineScope(Dispatchers.IO).launch {
//                    val assetManager = App.instance.assets
//                    val stream = assetManager.open("contacts_data.json") //TODO check warning
//                    val size = stream.available()
//                    val buffer = ByteArray(size)
//                    stream.read(buffer)
//                    stream.close()
//                    val fileContent = String(buffer)
//
//                    val gson = GsonBuilder().create()
//                    val persons = gson.fromJson<List<Person>>(
//                        fileContent,
//                        object : TypeToken<List<Person>>() {}.type
//                    )
//
//                    instance?.personDataDao()?.insertAll(persons = persons.map { PersonData(it) })
//                }
            }
        }

        private var instance: BaseDB? = null

        fun invoke() = instance ?: synchronized(BaseDB::class) {
            instance ?: buildDatabase().also {
                instance = it
            }
        }

        private fun buildDatabase() = Room.databaseBuilder(
            App.instance.applicationContext,
            BaseDB::class.java, "nav.db"
        )
            //.addCallback(initCallback)
            .build()
    }

}