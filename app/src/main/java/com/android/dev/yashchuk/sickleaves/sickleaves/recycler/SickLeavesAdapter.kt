package com.android.dev.yashchuk.sickleaves.sickleaves.recycler

import android.content.Context
import android.support.v7.recyclerview.extensions.ListAdapter
import android.support.v7.util.DiffUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.android.dev.yashchuk.sickleaves.R
import com.android.dev.yashchuk.sickleaves.data.SickLeave
import com.android.dev.yashchuk.sickleaves.data.Status

class SickLeavesAdapter(context: Context)
    : ListAdapter<SickLeave, RecyclerView.ViewHolder>(SickLeaveDiffCallBack()) {

    init {
        setHasStableIds(true)
    }

    private val layoutInflater: LayoutInflater = LayoutInflater.from(context)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view: View = layoutInflater.inflate(viewType, parent, false)
        return SickLeaveClosedVH(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is SickLeaveClosedVH -> holder.bindTo(getItem(position))
        }
    }

    override fun getItemViewType(position: Int): Int {
        val sickLeave = getItem(position)

        if (Status.OPEN.name == sickLeave.status) {
            return R.layout.item_sick_leave_opened
        }

        return R.layout.item_sick_leave_closed
    }

    override fun getItemId(position: Int): Long {
        return getItem(position).startDate.time
    }

    class SickLeaveDiffCallBack : DiffUtil.ItemCallback<SickLeave>() {
        override fun areItemsTheSame(oldItem: SickLeave?, newItem: SickLeave?): Boolean {
            return oldItem?.id == newItem?.id
        }

        override fun areContentsTheSame(oldItem: SickLeave?, newItem: SickLeave?): Boolean {
            return oldItem == newItem
        }

    }

}