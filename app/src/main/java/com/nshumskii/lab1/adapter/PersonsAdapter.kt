package com.nshumskii.lab1.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.nshumskii.lab1.R
import com.nshumskii.lab1.data.PersonData
import com.nshumskii.lab1.view.ListFragment

class PersonsAdapter(var data: List<PersonData>, var callback: ListFragment.OnItemClickListener) :
    RecyclerView.Adapter<PersonsAdapter.PersonViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): PersonViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return PersonViewHolder(inflater, parent)
    }

    override fun onBindViewHolder(holder: PersonViewHolder, position: Int) {
        val person: PersonData = data[position]
        holder.bind(person, callback)
    }

    override fun getItemCount() = data.size

    class PersonViewHolder(inflater: LayoutInflater, parent: ViewGroup) :
        RecyclerView.ViewHolder(inflater.inflate(R.layout.list_item, parent, false)) {

        private var mItem: ViewGroup? = null
        private var mNameView: TextView? = null
        private var mPhoneView: TextView? = null
        private var mEmailView: TextView? = null
        private var mCallBtn: ImageButton? = null
        private var person: PersonData? = null
        private var callback: ListFragment.OnItemClickListener? = null

        init {
            mItem = itemView.findViewById(R.id.cl_person_item)
            mNameView = itemView.findViewById(R.id.tv_person_name)
            mPhoneView = itemView.findViewById(R.id.tv_person_phone)
            mEmailView = itemView.findViewById(R.id.tv_person_email)
            mCallBtn = itemView.findViewById(R.id.ib_person_call)

            mItem?.setOnClickListener {
                person?.let { callback?.onClick(it) }
            }
            mItem?.setOnLongClickListener {
                person?.let { callback?.onLongClick(it) }
                return@setOnLongClickListener true
            }
            mCallBtn?.setOnClickListener {
                person?.let { callback?.onCallClick(it) }
            }
        }

        fun bind(
            person: PersonData,
            callback: ListFragment.OnItemClickListener?
        ) {
            this.person = person
            this.callback = callback

            mNameView?.text = "${person.firstname} ${person.lastname}"
            mPhoneView?.text = person.phone
            mEmailView?.text = person.email
        }

    }
}


