package com.android.dev.yashchuk.sickleaves.sickleaves.recycler

import android.support.v7.widget.RecyclerView
import android.view.View
import com.android.dev.yashchuk.sickleaves.R
import com.android.dev.yashchuk.sickleaves.data.SickLeave
import com.android.dev.yashchuk.sickleaves.utils.getFormattedDateString
import kotlinx.android.synthetic.main.item_sick_leave_opened.view.*

class SickLeaveOpenedVH (itemView: View?) : RecyclerView.ViewHolder(itemView){
    fun bindTo(sickLeave: SickLeave) {
        with(sickLeave) {
            itemView.title.text = title
            itemView.description.text = description
            itemView.status.text = status
            itemView.date.text = itemView.context.getString(
                    R.string.sick_list_date,
                    sickLeave.startDate?.getFormattedDateString(),
                    sickLeave.endDate?.getFormattedDateString()
            )
            itemView.close_btn.setOnClickListener{

            }
        }
    }
}