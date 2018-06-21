package com.android.dev.yashchuk.sickleaves.sickleaves

import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import com.android.dev.yashchuk.sickleaves.data.SickLeave
import kotlinx.android.synthetic.main.item_sick_leave.view.*

class SickLeavesAdapter: RecyclerView.Adapter<RecyclerView.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getItemCount(): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    class SickLeavesViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView) {

        fun bindTo(sickLeave: SickLeave) {
            with(sickLeave) {
                itemView.title.text = title
                itemView.description.text = description
            }


        }
    }
}