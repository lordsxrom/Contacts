package com.nshumskii.lab1.views

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.NavHostFragment
import com.nshumskii.lab1.R
import com.nshumskii.lab1.viewmodels.EmptyViewModel
import kotlinx.android.synthetic.main.empty_fragment.*

class EmptyFragment : Fragment() {

    private lateinit var importButton: Button

    private lateinit var addButton: Button

    private lateinit var viewModel: EmptyViewModel

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
            startActivityForResult(intent, EmptyViewModel.REQUEST_TO_IMPORT_FILE)
        }

        addButton = view.findViewById(R.id.btn_add_new)
        addButton.setOnClickListener {
            NavHostFragment.findNavController(this@EmptyFragment)
                .navigate(R.id.action_emptyFragment_to_editFragment)
        }

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(EmptyViewModel::class.java)

        viewModel.fileEvent.observe(viewLifecycleOwner, Observer {
            when (it?.getContentIfNotHandled()) {
                EmptyViewModel.ACTION_START_IMPORT_FILE -> {
                    group_import.visibility = View.GONE
                    group_progress.visibility = View.VISIBLE
                }
                EmptyViewModel.ACTION_FINISH_IMPORT_FILE -> {
                    NavHostFragment.findNavController(this@EmptyFragment)
                        .navigate(R.id.action_emptyFragment_to_listFragment)
                }
                EmptyViewModel.ACTION_ERROR_IMPORT_FILE -> {
                    group_import.visibility = View.VISIBLE
                    group_progress.visibility = View.GONE
                    Toast.makeText(
                        context,
                        getString(R.string.failed_import),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (requestCode) {
            EmptyViewModel.REQUEST_TO_IMPORT_FILE -> viewModel.fileToImport(resultCode, data)
        }
    }

}