package com.nshumskii.lab1.view

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.navigation.fragment.NavHostFragment
import com.nshumskii.lab1.MainActivity

import com.nshumskii.lab1.R
import kotlinx.android.synthetic.main.empty_fragment.*

class EmptyFragment : Fragment() {

    private lateinit var importButton: Button

    private lateinit var addButton: Button

    private val receiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            when (intent?.action) {
                MainActivity.ACTION_START_IMPORT_FILE -> {
                    // TODO show file size
                    // val fileSize = intent.extras?.get("import_file_size") as Boolean
                    group_import.visibility = View.GONE
                    group_progress.visibility = View.VISIBLE
                }
                MainActivity.ACTION_FINISH_IMPORT_FILE -> {
                    val fileImportResult = intent.extras?.get("import_success") as Boolean
                    if (fileImportResult) {
                        NavHostFragment.findNavController(this@EmptyFragment)
                            .navigate(R.id.action_emptyFragment_to_listFragment)
                    } else {
                        group_import.visibility = View.VISIBLE
                        group_progress.visibility = View.GONE
                        Toast.makeText(
                            context,
                            getString(R.string.failed_import),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.empty_fragment, container, false)

        importButton = view.findViewById(R.id.btn_import_file)
        importButton.setOnClickListener {
            val intent = Intent().apply {
                action = Intent.ACTION_OPEN_DOCUMENT
                type = "*/*"
            }
            activity?.startActivityForResult(intent, MainActivity.REQUEST_TO_IMPORT_FILE)
        }

        addButton = view.findViewById(R.id.btn_add_new)
        addButton.setOnClickListener {
            NavHostFragment.findNavController(this@EmptyFragment)
                .navigate(R.id.action_emptyFragment_to_editFragment)
        }

        val filter = IntentFilter().apply {
            addAction(MainActivity.ACTION_START_IMPORT_FILE)
            addAction(MainActivity.ACTION_FINISH_IMPORT_FILE)
        }
        context?.let { LocalBroadcastManager.getInstance(it).registerReceiver(receiver, filter) }

        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        context?.let { LocalBroadcastManager.getInstance(it).unregisterReceiver(receiver) }
    }

}