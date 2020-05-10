package com.nshumskii.lab1.model

import java.io.Serializable


data class Person(
    val firstname: String?,
    val lastname: String?,
    val phone: String?,
    val email: String?
) : Serializable