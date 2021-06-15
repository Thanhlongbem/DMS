package com.ziperp.dms.core.tracking

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.google.android.gms.location.ActivityRecognitionResult


class TransitionsReceiver : BroadcastReceiver() {

    override fun onReceive(
        context: Context,
        intent: Intent
    ) {
//        Toast.makeText(context.applicationContext, "Receive Intent $intent", Toast.LENGTH_LONG).show()
        if (intent.action === ActivityRecognitionManager.TRANSITION_ACTION_RECEIVER &&
            ActivityRecognitionResult.hasResult(intent)) {
            val result = ActivityRecognitionResult.extractResult(intent)
            ActivityRecognitionManager.handleDetectedActivities(result.probableActivities)
        }
    }
}