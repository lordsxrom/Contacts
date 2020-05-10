package com.nshumskii.lab1.views

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat.checkSelfPermission
import androidx.fragment.app.DialogFragment
import com.nshumskii.lab1.R
import com.nshumskii.lab1.data.PersonData

class OptionsDialog() : DialogFragment() {

    companion object {
        fun newInstance(person: PersonData) = OptionsDialog(person)
        const val MY_PERMISSIONS_REQUEST_PHONE_CALL = 1
        const val MY_PERMISSIONS_REQUEST_SEND_SMS = 2
    }

    var person: PersonData? = null

    constructor(person: PersonData) : this() {
        this.person = person
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.options_dialog, container, false)

        view.findViewById<TextView>(R.id.tv_dialog_name).text =
            "${person?.firstname} ${person?.lastname}"

        view.findViewById<ImageButton>(R.id.ib_dialog_call).setOnClickListener {
            if (checkSelfPermission(
                    requireContext(),
                    Manifest.permission.CALL_PHONE
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                requestPermissions(
                    arrayOf(Manifest.permission.CALL_PHONE),
                    MY_PERMISSIONS_REQUEST_PHONE_CALL
                )
            } else {
                val intent = Intent(Intent.ACTION_CALL, Uri.parse("tel:${person?.phone}"))
                startActivity(intent)
                dismiss()
            }
        }

        view.findViewById<ImageButton>(R.id.ib_dialog_msg).setOnClickListener {
            if (checkSelfPermission(
                    requireContext(),
                    Manifest.permission.SEND_SMS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                requestPermissions(
                    arrayOf(Manifest.permission.SEND_SMS),
                    MY_PERMISSIONS_REQUEST_SEND_SMS
                )
            } else {
                val intent = Intent(Intent.ACTION_SENDTO, Uri.parse("smsto:${person?.phone}"))
                startActivity(intent)
                dismiss()
            }
        }

        view.findViewById<ImageButton>(R.id.ib_dialog_email).setOnClickListener {
            val intent = Intent(Intent.ACTION_SENDTO, Uri.fromParts("mailto", person?.email, null))
            startActivity(intent)
        }

        return view
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            MY_PERMISSIONS_REQUEST_PHONE_CALL -> {
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    val intent = Intent(Intent.ACTION_CALL, Uri.parse("tel:${person?.phone}"))
                    startActivity(intent)
                    dismiss()
                } else {
                    Toast.makeText(context, "Permission was denied", Toast.LENGTH_SHORT).show()
                }
                return
            }
            MY_PERMISSIONS_REQUEST_SEND_SMS -> {
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    val intent = Intent(Intent.ACTION_SENDTO, Uri.parse("smsto:${person?.phone}"))
                    startActivity(intent)
                    dismiss()
                } else {
                    Toast.makeText(context, "Permission was denied", Toast.LENGTH_SHORT).show()
                }
                return
            }
            else -> {
            }
        }
    }

}