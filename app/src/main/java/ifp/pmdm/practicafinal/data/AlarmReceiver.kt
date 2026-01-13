package ifp.pmdm.practicafinal.data

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import ifp.pmdm.practicafinal.AlarmActivity

class AlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        val nombreMedicina = intent?.getStringExtra("MEDICINA_NOMBRE") ?: "Medicina"
        val codigoMedicina = intent?.getStringExtra("MEDICINA_CODIGO") ?: ""

        context?.let {
            // 1. Mostrar notificación
            mostrarNotificacion(it, nombreMedicina)

            // 2. Lanzar la pantalla de alarma (la que suena y pide el escaneo)
            val alarmIntent = Intent(it, AlarmActivity::class.java).apply {
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                putExtra("MEDICINA_NOMBRE", nombreMedicina)
                putExtra("MEDICINA_CODIGO", codigoMedicina)
            }
            it.startActivity(alarmIntent)
        }
    }

    private fun mostrarNotificacion(context: Context, nombre: String) {
        val channelId = "alarmas_medicina"
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(channelId, "Recordatorios", NotificationManager.IMPORTANCE_HIGH)
            notificationManager.createNotificationChannel(channel)
        }

        val builder = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(android.R.drawable.ic_lock_idle_alarm)
            .setContentTitle("Recordatorio de Medicina")
            .setContentText("Es hora de tomar: $nombre")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)

        notificationManager.notify(System.currentTimeMillis().toInt(), builder.build())
    }
}