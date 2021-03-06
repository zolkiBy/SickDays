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

class SickLeavesAdapter(
        context: Context,
        private val itemClickListener: (SickLeave) -> Unit,
        private val closeClickListener: (SickLeave) -> Unit,
        private val deleteClickListener: (SickLeave) -> Unit)
    : ListAdapter<SickLeave, RecyclerView.ViewHolder>(SickLeaveDiffCallBack()) {

    init {
        setHasStableIds(true)
    }

    private val layoutInflater: LayoutInflater = LayoutInflater.from(context)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view: View = layoutInflater.inflate(viewType, parent, false)
        return when (viewType) {
            R.layout.item_sick_leave_opened -> SickLeaveOpenedVH(view)
            R.layout.item_sick_leave_closed -> SickLeaveClosedVH(view)
            else -> throw IllegalArgumentException("Unknown ViewType")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is SickLeaveClosedVH -> holder.bindTo(
                    getItem(position),
                    itemClickListener,
                    deleteClickListener)
            is SickLeaveOpenedVH -> holder.bindTo(
                    getItem(position),
                    itemClickListener,
                    closeClickListener,
                    deleteClickListener)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position).status) {
            Status.OPEN.name -> R.layout.item_sick_leave_opened
            Status.CLOSE.name -> R.layout.item_sick_leave_closed
            else -> throw IllegalArgumentException("Unknown Status")
        }
    }

    override fun getItemId(position: Int): Long {
        return getItem(position).id
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