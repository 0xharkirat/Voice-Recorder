package org.fossify.voicerecorder.oacp

import android.content.Context
import android.content.Intent
import org.fossify.voicerecorder.helpers.CANCEL_RECORDING
import org.fossify.voicerecorder.helpers.TOGGLE_PAUSE
import org.fossify.voicerecorder.services.RecorderService
import org.oacp.android.OacpParams
import org.oacp.android.OacpReceiver
import org.oacp.android.OacpResult

/**
 * Handles background OACP actions for Voice Recorder.
 *
 * Controls the RecorderService by sending it the same intent actions
 * that the app's own UI uses (TOGGLE_PAUSE, CANCEL_RECORDING, etc.).
 *
 * The foreground action (start_recording) is handled by MainActivity
 * via an activity intent filter.
 */
class OacpActionReceiver : OacpReceiver() {

    override fun onAction(
        context: Context,
        action: String,
        params: OacpParams,
        requestId: String?
    ): OacpResult? {
        return when {
            action.endsWith(".oacp.ACTION_PAUSE_RECORDING") -> {
                if (!RecorderService.isRunning) {
                    return OacpResult.error("unsupported_state", "No recording in progress")
                }
                context.startService(Intent(context, RecorderService::class.java).apply {
                    this.action = TOGGLE_PAUSE
                })
                OacpResult.success("Recording paused")
            }
            action.endsWith(".oacp.ACTION_RESUME_RECORDING") -> {
                if (!RecorderService.isRunning) {
                    return OacpResult.error("unsupported_state", "No recording in progress")
                }
                context.startService(Intent(context, RecorderService::class.java).apply {
                    this.action = TOGGLE_PAUSE
                })
                OacpResult.success("Recording resumed")
            }
            action.endsWith(".oacp.ACTION_STOP_RECORDING") -> {
                if (!RecorderService.isRunning) {
                    return OacpResult.error("unsupported_state", "No recording in progress")
                }
                context.stopService(Intent(context, RecorderService::class.java))
                OacpResult.success("Recording stopped and saved")
            }
            action.endsWith(".oacp.ACTION_DISCARD_RECORDING") -> {
                if (!RecorderService.isRunning) {
                    return OacpResult.error("unsupported_state", "No recording in progress")
                }
                context.startService(Intent(context, RecorderService::class.java).apply {
                    this.action = CANCEL_RECORDING
                })
                OacpResult.success("Recording discarded")
            }
            else -> null
        }
    }
}
