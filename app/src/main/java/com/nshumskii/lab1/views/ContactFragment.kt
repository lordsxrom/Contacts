package com.nshumskii.lab1.views

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.TextView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.core.os.bundleOf
import androidx.lifecycle.Observer
import androidx.navigation.fragment.NavHostFragment
import com.nshumskii.lab1.MainActivity

import com.nshumskii.lab1.R
import com.nshumskii.lab1.viewmodels.ContactViewModel

class ContactFragment : Fragment() {

    private lateinit var viewModel: ContactViewModel

    private lateinit var fullnameView: TextView

    private lateinit var firstnameView: TextView

    private lateinit var lastnameView: TextView

    private lateinit var phoneView: TextView

    private lateinit var emailView: TextView

    private var personId: Long? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater.inflate(R.layout.contact_fragment, container, false)

        (activity as? MainActivity)?.supportActionBar?.setDisplayHomeAsUpEnabled(true)
        setHasOptionsMenu(true)

        personId = arguments?.get("personId") as? Long
        if (personId == null || personId == -1L) {
            NavHostFragment.findNavController(this@ContactFragment)
                .navigate(R.id.action_contactFragment_to_listFragment)
        }

        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    NavHostFragment.findNavController(this@ContactFragment)
                        .navigate(R.id.action_contactFragment_to_listFragment)
                }
            })

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

        personId?.let { viewModel.fetchPerson(it) }

        viewModel.person.observe(viewLifecycleOwner, Observer {
            it ?: return@Observer
            fullnameView.text = "${it.firstname} ${it.lastname}"
            firstnameView.text = it.firstname
            lastnameView.text = it.lastname
            phoneView.text = it.phone
            emailView.text = it.email
        })

        viewModel.navEvent.observe(viewLifecycleOwner, Observer {
            when (it.getContentIfNotHandled()) {
                R.id.action_contactFragment_to_listFragment -> {
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

        viewModel.msgEvent.observe(viewLifecycleOwner, Observer {
            val msg = it?.getContentIfNotHandled()
            Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
        })

    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.contact_menu, menu);
    }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.action_delete -> {
            viewModel.delete()
            true
        }
        R.id.action_update -> {
            viewModel.update()
            true
        }
        android.R.id.home -> {
            viewModel.home()
            true
        }
        else -> super.onOptionsItemSelected(item)
    }

//    override fun onDestroyView() {
//        (activity as? MainActivity)?.supportActionBar?.setDisplayHomeAsUpEnabled(false)
//        setHasOptionsMenu(false)
//        super.onDestroyView()
//    }

}