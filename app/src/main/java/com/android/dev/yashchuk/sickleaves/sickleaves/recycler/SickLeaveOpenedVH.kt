package com.android.dev.yashchuk.sickleaves.sickleaves.recycler

import android.support.v7.widget.RecyclerView
import android.view.View
import com.android.dev.yashchuk.sickleaves.R
import com.android.dev.yashchuk.sickleaves.data.SickLeave
import com.android.dev.yashchuk.sickleaves.utils.getFormattedDateString
import com.android.dev.yashchuk.sickleaves.utils.getStatusStringRes
import kotlinx.android.synthetic.main.item_sick_leave_opened.view.*

class SickLeaveOpenedVH(itemView: View?) : RecyclerView.ViewHolder(itemView) {
    fun bindTo(
            sickLeave: SickLeave,
            itemClickListener: (SickLeave) -> Unit,
            closeClickListener: (SickLeave) -> Unit,
            deleteClickListener: (SickLeave) -> Unit
    ) {
        with(sickLeave) {
            itemView.title.text = title
            itemView.status.text = itemView.context.getString(status.getStatusStringRes())
            itemView.date.text = itemView.context.getString(
                    R.string.sick_list_item_date,
                    sickLeave.startDate?.getFormattedDateString(),
                    sickLeave.endDate?.getFormattedDateString() ?: ""
            )
            itemView.setOnClickListener { itemClickListener(this) }
            itemView.close_btn.setOnClickListener { closeClickListener(this) }
            itemView.delete_btn.isEnabled = true
            itemView.delete_btn.setOnClickListener { button ->
                button.isEnabled = false
                deleteClickListener(this)
            }
        }
    }
}