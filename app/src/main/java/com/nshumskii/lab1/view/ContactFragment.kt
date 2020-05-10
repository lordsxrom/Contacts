package com.nshumskii.lab1.view

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.TextView
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.lifecycle.Observer
import androidx.navigation.fragment.NavHostFragment

import com.nshumskii.lab1.R
import com.nshumskii.lab1.model.Person
import com.nshumskii.lab1.viewmodel.ContactViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ContactFragment : Fragment() {

    private lateinit var viewModel: ContactViewModel

    private lateinit var fullnameView: TextView

    private lateinit var firstnameView: TextView

    private lateinit var lastnameView: TextView

    private lateinit var phoneView: TextView

    private lateinit var emailView: TextView


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)
        val view: View = inflater.inflate(R.layout.contact_fragment, container, false)

        fullnameView = view.findViewById(R.id.tv_contact_fullname)
        firstnameView = view.findViewById(R.id.tv_contact_firstname)
        lastnameView = view.findViewById(R.id.tv_contact_lastname)
        phoneView = view.findViewById(R.id.tv_contact_phone)
        emailView = view.findViewById(R.id.tv_contact_email)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(ContactViewModel::class.java)

        val personId: Long? = arguments?.get("personId") as? Long
        personId?.let { viewModel.getPersonById(it) }

        viewModel.person.observe(viewLifecycleOwner, Observer {
            it?.let {
                fullnameView.text = "${it.firstname} ${it.lastname}"
                firstnameView.text = it.firstname
                lastnameView.text = it.lastname
                phoneView.text = it.phone
                emailView.text = it.email
            }
        })

        viewModel.navEvent.observe(viewLifecycleOwner, Observer {
            when (it.getContentIfNotHandled()) {
                R.id.action_contactFragment_to_listFragment -> {
                    Toast.makeText(context, getString(R.string.contact_removed), Toast.LENGTH_SHORT).show()
                    NavHostFragment.findNavController(this@ContactFragment)
                        .navigate(R.id.action_contactFragment_to_listFragment)
                }
                R.id.action_contactFragment_to_editFragment -> {
                    val bundle = bundleOf("personId" to personId)
                    NavHostFragment.findNavController(this@ContactFragment)
                        .navigate(
                            R.id.action_contactFragment_to_editFragment,
                            bundle
                        )
                }
            }
        })

    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.contact_menu, menu);
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_delete -> {
                viewModel.delete()
                return true
            }
            R.id.action_update -> {
                viewModel.update()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

}