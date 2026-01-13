package ifp.pmdm.practicafinal.data

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.widget.Toast
import androidx.core.app.NotificationCompat

class AlarmReceiver : BroadcastReceiver() {
    // El error estaba aquí: debe ser (context: Context?, intent: Intent?)
    override fun onReceive(context: Context?, intent: Intent?) {
        val nombreMedicina = intent?.getStringExtra("MEDICINA_NOMBRE") ?: "Medicina"

        if (context != null) {
            mostrarNotificacion(context, nombreMedicina)
            Toast.makeText(context, "¡Toma tu medicina: $nombreMedicina!", Toast.LENGTH_LONG).show()
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