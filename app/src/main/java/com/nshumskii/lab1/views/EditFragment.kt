package com.nshumskii.lab1.views

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.EditText
import androidx.core.os.bundleOf
import androidx.lifecycle.Observer
import androidx.navigation.fragment.NavHostFragment
import com.nshumskii.lab1.viewmodels.EditViewModel
import com.nshumskii.lab1.R


class EditFragment : Fragment() {

    private lateinit var firstnameField: EditText

    private lateinit var lastnameField: EditText

    private lateinit var phoneField: EditText

    private lateinit var emailField: EditText

    private lateinit var viewModel: EditViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)
        val view = inflater.inflate(R.layout.edit_fragment, container, false)

        firstnameField = view.findViewById(R.id.ied_edit_firstname)
        lastnameField = view.findViewById(R.id.ied_edit_lastname)
        phoneField = view.findViewById(R.id.ied_edit_phone)
        emailField = view.findViewById(R.id.ied_edit_email)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(EditViewModel::class.java)

        val personId: Long? = arguments?.get("personId") as? Long
        personId?.let { viewModel.getPersonById(it) }

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
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.edit_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_save -> {
                save()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun save() {
        val firstname = firstnameField.text.toString()
        val lastname = lastnameField.text.toString()
        val phone = phoneField.text.toString()
        val email = emailField.text.toString()
        viewModel.save(firstname, lastname, phone, email)
    }

}
