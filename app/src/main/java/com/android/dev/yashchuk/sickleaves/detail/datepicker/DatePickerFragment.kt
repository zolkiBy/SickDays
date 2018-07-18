package com.android.dev.yashchuk.sickleaves.detail.datepicker

import android.app.DatePickerDialog
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.widget.DatePicker
import com.android.dev.yashchuk.sickleaves.callbacks.OnDateSetListener
import java.util.*

private const val ARG_REQUEST_CODE = "ARG_REQUEST_CODE"
private const val DEFAULT_REQUEST_CODE = -1

class DatePickerFragment : DialogFragment(), DatePickerDialog.OnDateSetListener {

    private var dateListener: OnDateSetListener? = null
    private var requestCode: Int? = DEFAULT_REQUEST_CODE

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        requestCode = arguments?.getInt(ARG_REQUEST_CODE, DEFAULT_REQUEST_CODE)

        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        return DatePickerDialog(targetFragment?.context, this, year, month, day)
    }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        val calendar = Calendar.getInstance()
        calendar.set(year, month, dayOfMonth)
        dateListener?.onDateSet(requestCode, calendar.time)
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if (targetFragment is OnDateSetListener) {
            dateListener = targetFragment as OnDateSetListener
        } else {
            throw RuntimeException(targetFragment.toString() + " must implement OnDateSetListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        dateListener = null
    }

    companion object {
        @JvmStatic
        fun newInstance(requestCode: Int) =
                DatePickerFragment().apply {
                    arguments = Bundle().apply {
                        putInt(ARG_REQUEST_CODE, requestCode)
                    }
                }
    }
}