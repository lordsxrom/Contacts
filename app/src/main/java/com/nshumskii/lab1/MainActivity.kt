package com.nshumskii.lab1

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation.findNavController
import com.nshumskii.lab1.view.ListFragment
import com.nshumskii.lab1.viewmodel.ListViewModel
import com.nshumskii.lab1.viewmodel.MainViewModel

class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: MainViewModel

    companion object{
        const val REQUEST_TO_IMPORT_FILE = 10
        const val ACTION_FINISH_IMPORT_FILE = "finish_import_file"
        const val ACTION_START_IMPORT_FILE = "start_import_file"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when(requestCode) {
            REQUEST_TO_IMPORT_FILE -> viewModel.fileToImport(resultCode, data)
        }
    }
}