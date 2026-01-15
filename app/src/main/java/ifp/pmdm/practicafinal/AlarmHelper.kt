package ifp.pmdm.practicafinal

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import ifp.pmdm.practicafinal.data.AlarmReceiver
import ifp.pmdm.practicafinal.data.DatosMedicinas

class AlarmHelper(private val context: Context) {

    fun programarAlarma(medicina: DatosMedicinas) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, AlarmReceiver::class.java).apply {
            putExtra("MEDICINA_NOMBRE", medicina.nombre)
            putExtra("MEDICINA_CODIGO", medicina.codigoBarras)
            putExtra("MEDICINA_ID", medicina.id)
        }

        val pendingIntent = PendingIntent.getBroadcast(
            context,
            medicina.id.toInt(),
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val triggerTime = System.currentTimeMillis() + 10000


        alarmManager.setExactAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP,
            triggerTime,
            pendingIntent
        )
    }
}