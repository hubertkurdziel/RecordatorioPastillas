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
            putExtra("MEDICINA_ID", medicina.id)
        }

        // El ID de la medicina nos sirve para que cada alarma sea única
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            medicina.id.toInt(),
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        // Calculamos cuándo debe sonar (frecuenciaHoras * milisegundos en una hora)
        // Para la PRIMERA toma, usaremos System.currentTimeMillis() + frecuencia
        val tiempoEspera = medicina.frecuenciaHoras * 3600000L
        val triggerTime = System.currentTimeMillis() + tiempoEspera


        // Programar alarma exacta
        alarmManager.setExactAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP,
            triggerTime,
            pendingIntent
        )
    }
}