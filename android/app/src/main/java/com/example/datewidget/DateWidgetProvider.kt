package com.example.datewidget

import android.app.AlarmManager
import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.widget.RemoteViews
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale

class DateWidgetProvider : AppWidgetProvider() {

    override fun onUpdate(context: Context, manager: AppWidgetManager, ids: IntArray) {
        ids.forEach { manager.updateAppWidget(it, buildViews(context)) }
        scheduleMidnightRefresh(context)
    }

    override fun onReceive(context: Context, intent: Intent) {
        super.onReceive(context, intent)
        if (intent.action == ACTION_REFRESH) {
            val manager = AppWidgetManager.getInstance(context)
            val ids = manager.getAppWidgetIds(ComponentName(context, DateWidgetProvider::class.java))
            onUpdate(context, manager, ids)
        }
    }

    private fun buildViews(context: Context): RemoteViews {
        val today = LocalDate.now()
        val fmt = DateTimeFormatter.ofPattern("EEE, MMM d, yyyy", Locale.getDefault())
        val views = RemoteViews(context.packageName, R.layout.widget)
        OFFSETS.forEachIndexed { i, days ->
            views.setTextViewText(LABELS[i], "$days days ago")
            views.setTextViewText(DATES[i], today.minusDays(days.toLong()).format(fmt))
        }
        return views
    }

    private fun scheduleMidnightRefresh(context: Context) {
        val alarms = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val next = LocalDate.now().plusDays(1).atStartOfDay(ZoneId.systemDefault())
            .plusMinutes(1).toInstant().toEpochMilli()
        val intent = Intent(context, DateWidgetProvider::class.java).setAction(ACTION_REFRESH)
        val pending = PendingIntent.getBroadcast(
            context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        // Inexact alarm: no special permission needed; updatePeriodMillis is the fallback.
        alarms.set(AlarmManager.RTC, next, pending)
    }

    private companion object {
        const val ACTION_REFRESH = "com.example.datewidget.REFRESH"
        val OFFSETS = listOf(29, 22, 14, 7)
        val LABELS = listOf(R.id.label1, R.id.label2, R.id.label3, R.id.label4)
        val DATES = listOf(R.id.date1, R.id.date2, R.id.date3, R.id.date4)
    }
}
