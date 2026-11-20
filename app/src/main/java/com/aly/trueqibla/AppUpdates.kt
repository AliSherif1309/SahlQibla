package com.aly.trueqibla

import android.app.Activity
import android.app.AlertDialog
import android.widget.Toast
import java.util.concurrent.Executors

/** Settings button + prompts for updates, the Sahl Adhan way (see [Updater]). */
class AppUpdates(private val activity: Activity, private val arabic: Boolean) {
    companion object { private val worker = Executors.newSingleThreadExecutor(); private var checkedThisRun = false }
    private var offer: Updater.Release? = null
    private var busy = false
    private var resumed = false
    @Volatile private var closed = false
    private var dialog: AlertDialog? = null
    var onChanged: (() -> Unit)? = null
    private fun t(en: String, ar: String) = if (arabic) ar else en
    private fun toast(text: String) { Toast.makeText(activity, text, Toast.LENGTH_LONG).show() }

    val label: String get() = when {
        busy -> t("Checking…", "جارٍ التحقق…")
        offer != null -> t("New version available: ${offer!!.name} — Update now", "إصدار جديد متاح: ${offer!!.name} — حدّث الآن")
        else -> t("Check for updates · v${BuildConfig.VERSION_NAME}", "التحقق من التحديثات · v${BuildConfig.VERSION_NAME}")
    }

    fun onResume() {
        resumed = true
        Updater.scheduleDaily(activity)
        if (!checkedThisRun) { checkedThisRun = true; check(false) }
    }

    fun onPause() { resumed = false; dialog?.dismiss(); dialog = null }
    fun close() { closed = true; onPause() }

    fun click() {
        when {
            busy -> Unit
            offer != null -> showOffer()
            else -> check(true)
        }
    }

    private fun check(manual: Boolean) {
        busy = true; onChanged?.invoke()
        worker.execute {
            val r = Updater.fetchNewer()
            activity.runOnUiThread {
                if (closed) return@runOnUiThread
                busy = false; offer = r; onChanged?.invoke()
                if (!resumed) return@runOnUiThread
                if (r != null) { if (manual) toast(t("New version available: ${r.name}", "إصدار جديد متاح: ${r.name}")); showOffer() }
                else if (manual) toast(t("You have the latest version (v${BuildConfig.VERSION_NAME})", "لديك آخر إصدار (v${BuildConfig.VERSION_NAME})"))
            }
        }
    }

    private fun showOffer() {
        val r = offer ?: return
        dialog?.dismiss()
        dialog = AlertDialog.Builder(activity)
            .setTitle(t("New version available: ${r.name}", "إصدار جديد متاح: ${r.name}"))
            .setMessage(r.notes.ifBlank { t("A new version of Sahl Qibla is ready.", "إصدار جديد من سهل للقبلة جاهز.") })
            .setPositiveButton(t("Update now", "حدّث الآن")) { _, _ ->
                Updater.downloadAndInstall(activity, r)
                toast(t("Downloading in your browser — tap the download when it finishes to install",
                    "جارٍ التنزيل في المتصفح — اضغط على الملف بعد انتهاء التنزيل لتثبيته"))
            }
            .setNegativeButton(t("Later", "لاحقًا"), null)
            .show()
    }
}
