package com.aly.trueqibla

import android.app.Activity
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.media.AudioAttributes
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView
import android.widget.Toast

/**
 * Sahl Core (light) — the shared pieces every Sahl app has:
 * crash report screen, the Sahl chime, and Sahl Union (signature permission in the manifest).
 */
object SahlCore {
    const val UNION = "com.sahl.permission.UNION"

    /** Saves any crash and opens a simple report screen (works even when the main screen crashes). */
    fun catchCrashes(c: Context) {
        val app = c.applicationContext
        val prev = Thread.getDefaultUncaughtExceptionHandler()
        if (prev is Handler) return
        Thread.setDefaultUncaughtExceptionHandler(Handler(app, prev))
    }

    private class Handler(val app: Context, val prev: Thread.UncaughtExceptionHandler?) : Thread.UncaughtExceptionHandler {
        override fun uncaughtException(t: Thread, e: Throwable) {
            try {
                val v = try { app.packageManager.getPackageInfo(app.packageName, 0).versionName } catch (x: Exception) { "?" }
                val log = "${app.packageName} v$v\n" + android.util.Log.getStackTraceString(e).take(6000)
                app.startActivity(Intent(app, SahlCrashActivity::class.java).putExtra("log", log)
                    .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK))
                android.os.Process.killProcess(android.os.Process.myPid())
                System.exit(10)
            } catch (x: Exception) { prev?.uncaughtException(t, e) }
        }
    }

    /** The Sahl chime — the shared notification sound of every Sahl app (res/raw/sahl_chime). */
    fun chime(c: Context): Uri = Uri.parse("android.resource://${c.packageName}/raw/sahl_chime")

    fun chimeChannel(c: Context, id: String, name: String, importance: Int = NotificationManager.IMPORTANCE_HIGH) =
        NotificationChannel(id, name, importance).apply {
            setSound(chime(c), AudioAttributes.Builder().setUsage(AudioAttributes.USAGE_NOTIFICATION)
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION).build())
        }
}

/** Plain crash-report screen (no app UI code, so it cannot crash itself). */
class SahlCrashActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val log = intent.getStringExtra("log") ?: "No report"
        val box = LinearLayout(this).apply { orientation = LinearLayout.VERTICAL; setPadding(40, 80, 40, 40) }
        box.addView(TextView(this).apply { text = "Sahl crash report"; textSize = 22f })
        box.addView(Button(this).apply {
            text = "Copy report"
            setOnClickListener {
                getSystemService(ClipboardManager::class.java).setPrimaryClip(ClipData.newPlainText("crash", log))
                Toast.makeText(this@SahlCrashActivity, "Copied", Toast.LENGTH_SHORT).show()
            }
        })
        box.addView(ScrollView(this).apply { addView(TextView(this@SahlCrashActivity).apply { text = log; textSize = 11f; setTextIsSelectable(true) }) },
            LinearLayout.LayoutParams(-1, 0, 1f))
        setContentView(box)
    }
}
