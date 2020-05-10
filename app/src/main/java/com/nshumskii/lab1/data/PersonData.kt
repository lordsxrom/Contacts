package com.nshumskii.lab1.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.nshumskii.lab1.model.Person

@Entity(tableName = "personData")
data class PersonData(
    @PrimaryKey(autoGenerate = true) var id: Long?,
    @ColumnInfo(name = "firstname") var firstname: String?,
    @ColumnInfo(name = "lastname") var lastname: String?,
    @ColumnInfo(name = "phone") var phone: String?,
    @ColumnInfo(name = "email") var email: String?
) {
    constructor(person: Person) :
            this(
                null,
                person.firstname,
                person.lastname,
                person.phone,
                person.email
            )
}