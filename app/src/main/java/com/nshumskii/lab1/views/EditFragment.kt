package com.nshumskii.lab1.views

import android.os.Bundle
import android.view.*
import android.widget.EditText
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.NavHostFragment
import com.nshumskii.lab1.MainActivity
import com.nshumskii.lab1.R
import com.nshumskii.lab1.viewmodels.EditViewModel


class EditFragment : Fragment() {

    private lateinit var firstnameField: EditText

    private lateinit var lastnameField: EditText

    private lateinit var phoneField: EditText

    private lateinit var emailField: EditText

    private lateinit var viewModel: EditViewModel

    private var personId: Long? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.edit_fragment, container, false)

        (activity as? MainActivity)?.supportActionBar?.setDisplayHomeAsUpEnabled(true)
        setHasOptionsMenu(true)

        personId = arguments?.get("personId") as? Long

        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    onBackPressed()
                }
            })

        firstnameField = view.findViewById(R.id.ied_edit_firstname)
        lastnameField = view.findViewById(R.id.ied_edit_lastname)
        phoneField = view.findViewById(R.id.ied_edit_phone)
        emailField = view.findViewById(R.id.ied_edit_email)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(EditViewModel::class.java)

        personId?.let { viewModel.fetchPerson(it) }

        viewModel.person.observe(viewLifecycleOwner, Observer {
            it ?: return@Observer
            firstnameField.setText(it.firstname)
            lastnameField.setText(it.lastname)
            phoneField.setText(it.phone)
            emailField.setText(it.email)
        })

        viewModel.navEvent.observe(viewLifecycleOwner, Observer {
            val bundle = bundleOf("personId" to personId)
            NavHostFragment.findNavController(this@EditFragment)
                .navigate(R.id.action_editFragment_to_contactFragment, bundle)
        })

        viewModel.msgEvent.observe(viewLifecycleOwner, Observer {
            val msg = it?.getContentIfNotHandled()
            Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
        })
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.edit_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.action_save -> {
            save()
            true
        }
        android.R.id.home -> {
            onBackPressed()
            true
        }
        else -> super.onOptionsItemSelected(item)
    }

    private fun save() {
        val firstname = firstnameField.text.toString()
        val lastname = lastnameField.text.toString()
        val phone = phoneField.text.toString()
        val email = emailField.text.toString()
        viewModel.save(firstname, lastname, phone, email)
    }

    private fun onBackPressed() {
        val personId = arguments?.get("personId") as? Long
        val bundle = bundleOf("personId" to personId)
        NavHostFragment.findNavController(this@EditFragment)
            .navigate(R.id.action_editFragment_to_contactFragment, bundle)
    }

//    override fun onDestroyView() {
//        (activity as? MainActivity)?.supportActionBar?.setDisplayHomeAsUpEnabled(false)
//        setHasOptionsMenu(false)
//        super.onDestroyView()
//    }
}
