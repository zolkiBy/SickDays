package com.android.dev.yashchuk.sickleaves.sickleaves

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.android.dev.yashchuk.sickleaves.R
import com.android.dev.yashchuk.sickleaves.data.SickLeave
import com.android.dev.yashchuk.sickleaves.data.Status
import com.android.dev.yashchuk.sickleaves.utils.getFormattedDate
import kotlinx.android.synthetic.main.item_sick_leave.view.*

@JvmField
val TYPE_OPENED = Status.OPEN.ordinal
@JvmField
val TYPE_CLOSED = Status.CLOSE.ordinal

class SickLeavesAdapter(val items: List<SickLeave>,
                        val context : Context) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val layoutInflater: LayoutInflater = LayoutInflater.from(context)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view: View
        when (viewType) {
            TYPE_OPENED -> view = layoutInflater.inflate(R.layout.item_sick_leave, parent, false)
            // TODO - change layout
            TYPE_CLOSED -> view = layoutInflater.inflate(R.layout.item_sick_leave, parent, false)
        }

        return SickLeaveViewHolder(view)
    }

    override fun getItemCount(): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getItemViewType(position: Int): Int {
        val sickLeave = items[position]

        if (Status.OPEN.name == sickLeave.status) {
            return TYPE_OPENED
        }

        return TYPE_CLOSED
    }

    class SickLeaveViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView) {

        fun bindTo(sickLeave: SickLeave) {
            with(sickLeave) {
                itemView.title.text = title
                itemView.description.text = description
                itemView.status.text = status
                itemView.date.text = itemView.context.getString(
                        R.string.sick_list_date,
                        sickLeave.startDate.toString().getFormattedDate(),
                        sickLeave.endDate.toString().getFormattedDate()
                )
            }
        }
    }
}