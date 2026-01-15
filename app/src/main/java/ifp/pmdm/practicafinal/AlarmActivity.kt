package ifp.pmdm.practicafinal

import android.media.MediaPlayer
import android.media.RingtoneManager
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.journeyapps.barcodescanner.ScanContract
import com.journeyapps.barcodescanner.ScanOptions
import ifp.pmdm.practicafinal.databinding.ActivityAlarmBinding

class AlarmActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAlarmBinding
    private var mediaPlayer: MediaPlayer? = null
    private var codigoEsperado: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O_MR1) {
            setShowWhenLocked(true)
            setTurnScreenOn(true)
            val keyguardManager = getSystemService(android.content.Context.KEYGUARD_SERVICE) as android.app.KeyguardManager
            keyguardManager.requestDismissKeyguard(this, null)
        } else {
            window.addFlags(
                android.view.WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD or
                        android.view.WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED or
                        android.view.WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON or
                        android.view.WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
            )
        }

        binding = ActivityAlarmBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val nombreMedicina = intent.getStringExtra("MEDICINA_NOMBRE")
        codigoEsperado = intent.getStringExtra("MEDICINA_CODIGO")
        binding.tvNombreMedicinaAlarma.text = nombreMedicina

        iniciarSonido()

        binding.btnEscanearApagar.setOnClickListener {
            lanzarEscaner()
        }
    }

    private fun iniciarSonido() {
        val alerta = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM)
        mediaPlayer = MediaPlayer.create(this, alerta)
        mediaPlayer?.isLooping = true
        mediaPlayer?.start()
    }

    private val barcodeLauncher = registerForActivityResult(ScanContract()) { result ->
        if (result.contents == null) {
            Toast.makeText(this, getString(R.string.alarm_scan_cancelled), Toast.LENGTH_SHORT).show()
        } else {
            if (result.contents == codigoEsperado || codigoEsperado.isNullOrEmpty()) {
                detenerAlarma()
            } else {
                Toast.makeText(this, getString(R.string.alarm_incorrect_code), Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun lanzarEscaner() {
        val options = ScanOptions().apply {
            setDesiredBarcodeFormats(ScanOptions.ALL_CODE_TYPES)
            setPrompt(getString(R.string.alarm_scan_prompt))
            setBeepEnabled(true)
            setOrientationLocked(false)
        }
        barcodeLauncher.launch(options)
    }

    private fun detenerAlarma() {
        mediaPlayer?.stop()
        mediaPlayer?.release()
        mediaPlayer = null
        Toast.makeText(this, getString(R.string.alarm_medicine_validated), Toast.LENGTH_LONG).show()
        finish()
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer?.release()
    }
}