package app.grapheneos.gmscompat

import android.app.Notification
import android.app.PendingIntent
import android.content.pm.GosPackageState
import android.content.pm.GosPackageStateFlag
import android.ext.SettingsIntents
import android.ext.settings.app.AswBlockPlayIntegrityApi
import android.os.Binder
import android.os.BinderDef
import android.util.Log
import com.android.internal.gmscompat.IClientOfGmsCore2Gca
import com.android.internal.gmscompat.dynamite.server.IFileProxyService

object BinderClientOfGmsCore2Gca : IClientOfGmsCore2Gca.Stub() {

    override fun maybeGetBinderDef(callerPkg: String, processState: Int, ifaceName: String): BinderDef? {
        return BinderDefs.maybeGetBinderDef(callerPkg, processState, ifaceName, false)
    }

    override fun getDynamiteFileProxyService(): IFileProxyService = BinderGms2Gca.dynamiteFileProxyService!!

    override fun showMissingAppNotification(pkgName: String) {
        val prompt = when (pkgName) {
            "com.google.android.tts" -> R.string.missing_speech_services
            else -> throw IllegalArgumentException(pkgName)
        }

        Notifications.handleMissingApp(Notifications.CH_MISSING_APP, App.ctx().getText(prompt), pkgName)
    }

    override fun showPlayIntegrityNotification(pkgName: String, isBlocked: Boolean) {
        val ctx = App.ctx()
        if (ctx.packageManager.getPackageUid(pkgName, 0) != Binder.getCallingUid()) {
            // this method should be called by app itself
            throw SecurityException()
        }

        val TAG = "showPlayIntegrityNotification"
        Log.d(TAG, "caller: $pkgName")

        val setting = AswBlockPlayIntegrityApi.I
        val gosPs = GosPackageState.get(pkgName, ctx.userId)
        if (!setting.isNotificationEnabled(gosPs)) {
            // there's a client-side isNotificationEnabled() check before the call
            Log.e(TAG, "notification is disabled")
            return
        }

        Notifications.builder(Notifications.CH_MANAGE_PLAY_INTEGRITY_API).run {
            setSmallIcon(R.drawable.ic_info)
            setContentTitle(R.string.notif_app_used_play_integrity_api_title)
            val text = if (isBlocked) R.string.notif_app_play_integrity_api_blocked
                else R.string.notif_app_used_play_integrity_api
            setContentText(ctx.getString(text, getApplicationLabel(ctx, pkgName)))
            run {
                val intent = SettingsIntents.getAppIntent(ctx, SettingsIntents.APP_MANAGE_PLAY_INTEGRITY_API, pkgName)
                val pendingIntent = PendingIntent.getActivity(ctx, 0, intent, PendingIntent.FLAG_IMMUTABLE)
                addAction(Notification.Action.Builder(null,
                    ctx.getText(R.string.notif_app_used_play_integrity_api_action_more_info),
                    pendingIntent).build())
            }
            run {
                val pendingIntent = PendingActionReceiver.makeWriteGosPackageStateAndCancelNotif(
                    ctx, pkgName,
                    GosPackageStateFlag.SUPPRESS_PLAY_INTEGRITY_API_NOTIF, true,
                    Notifications.ID_MANAGE_PLAY_INTEGRITY_API)
                addAction(Notification.Action.Builder(null,
                    ctx.getText(R.string.dont_show_again), pendingIntent).build())
            }
            setShowWhen(true)
            show(Notifications.ID_MANAGE_PLAY_INTEGRITY_API)
        }
    }
}

