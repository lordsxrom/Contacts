package com.nshumskii.lab1.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.nshumskii.lab1.models.Person

@Entity(tableName = "persons_table")
data class PersonData(
    @PrimaryKey(autoGenerate = true) var id: Long?,
    var firstname: String?,
    var lastname: String?,
    var phone: String?,
    var email: String?
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