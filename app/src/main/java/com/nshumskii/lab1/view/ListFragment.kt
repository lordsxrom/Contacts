package com.nshumskii.lab1.view

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.*
import android.widget.EditText
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.NavHostFragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.nshumskii.lab1.R
import com.nshumskii.lab1.adapter.PersonsAdapter
import com.nshumskii.lab1.data.PersonData
import com.nshumskii.lab1.viewmodel.ListViewModel
import kotlinx.android.synthetic.main.list_fragment.*


class ListFragment : Fragment() {

    private lateinit var personsRecyclerView: RecyclerView

    private lateinit var search: EditText

    private lateinit var personsAdapter: PersonsAdapter

    private lateinit var viewModel: ListViewModel

    interface OnItemClickListener {
        fun onClick(person: PersonData)
        fun onLongClick(person: PersonData)
        fun onCallClick(person: PersonData)
    }

    private val textWatcher = object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {
            viewModel.search(s.toString())
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)
        val view = inflater.inflate(R.layout.list_fragment, container, false)

        search = view.findViewById(R.id.et_search)

        personsAdapter = PersonsAdapter(emptyList())
        personsAdapter.callback = object : OnItemClickListener {
            override fun onClick(person: PersonData) {
                val bundle = bundleOf("personId" to person.id)
                findNavController(this@ListFragment).navigate(
                    R.id.action_listFragment_to_contactFragment,
                    bundle
                )
            }

            override fun onLongClick(person: PersonData) {
                fragmentManager?.beginTransaction()?.let {
                    val newFragment = OptionsDialog.newInstance(person)
                    newFragment.show(it, "dialog")
                }
            }

            override fun onCallClick(person: PersonData) {
                if (ContextCompat.checkSelfPermission(
                        requireContext(),
                        Manifest.permission.CALL_PHONE
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    requestPermissions(
                        arrayOf(Manifest.permission.CALL_PHONE),
                        OptionsDialog.MY_PERMISSIONS_REQUEST_PHONE_CALL
                    )
                } else {
                    val intent = Intent(Intent.ACTION_CALL, Uri.parse("tel:${person?.phone}"))
                    startActivity(intent)
                }
            }
        }

        personsRecyclerView = view.findViewById(R.id.rv_persons)
        personsRecyclerView.adapter = personsAdapter
        personsRecyclerView.layoutManager = LinearLayoutManager(context)
        personsRecyclerView.addItemDecoration(
            DividerItemDecoration(
                context,
                LinearLayoutManager.VERTICAL
            )
        )

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(ListViewModel::class.java)

        viewModel.persons.observe(viewLifecycleOwner, Observer<List<PersonData>> {
            group_list.visibility = View.VISIBLE
            pb_progress_bar.visibility = View.GONE

            personsAdapter.data = it
            personsAdapter.notifyDataSetChanged() // TODO improve notify
        })

        viewModel.navEvent.observe(viewLifecycleOwner, Observer {
            it?.getContentIfNotHandled()?.let { id ->
                findNavController(this@ListFragment).navigate(id)
            }
        })

        // TODO swipeRefresh
//        swipeRefresh.setOnRefreshListener {
//
//        }
    }

    override fun onResume() {
        super.onResume()
        search.addTextChangedListener(textWatcher)
    }

    override fun onPause() {
        super.onPause()
        search.removeTextChangedListener(textWatcher)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.list_menu, menu);
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_sort -> {
                viewModel.sort()
                return true
            }
            R.id.action_delete_all -> {
                viewModel.deleteAll()
                return true
            }
            R.id.action_insert_new -> {
                viewModel.insertNew()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

}

