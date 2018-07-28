package com.android.dev.yashchuk.sickleaves.sickleaves

import com.android.dev.yashchuk.sickleaves.data.SickLeave
import com.android.dev.yashchuk.sickleaves.data.Status
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.iid.InstanceIdResult
import java.util.*

class SickLeavesPresenter(private val view: SickLeavesContract.View) : SickLeavesContract.Presenter {

    override fun showLoading(show: Boolean) {
        if (show) view.hideEmptyView()
        view.showLoading(show)
    }

    override fun updateUi(sickLeaves: List<SickLeave>?) {
        if (sickLeaves != null && sickLeaves.isNotEmpty()) {
            view.hideEmptyView()
            view.showData()
            view.updateUi(sickLeaves)
        } else {
            view.hideData()
            view.showEmptyView()
        }
    }

    override fun closeSickLeave(sickLeave: SickLeave) {
        sickLeave.apply {
            status = Status.CLOSE.name
            endDate = Date()
        }

        view.closeSickLeave(sickLeave)
    }

    override fun deleteSickLeave(sickLeave: SickLeave) {
        view.deleteSickLeave(sickLeave)
    }

    override fun showAll() {
        view.showAll()
    }

    override fun showOpened() {
        view.showOpened()
    }

    override fun showClosed() {
        view.showClosed()
    }

    override fun logFcmToken() {
        FirebaseInstanceId.getInstance().instanceId
                .addOnCompleteListener(object : OnCompleteListener<InstanceIdResult> {
                    override fun onComplete(task: Task<InstanceIdResult>) {
                        if (!task.isSuccessful) {
                            return
                        }

                        view.logToken(task.result.token)
                    }
                })
    }
}