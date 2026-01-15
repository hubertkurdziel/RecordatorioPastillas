package ifp.pmdm.practicafinal

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import ifp.pmdm.practicafinal.data.BaseDatosApp
import ifp.pmdm.practicafinal.databinding.DetallesMedicinaBinding
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale

class DetailMedicineActivity : AppCompatActivity() {

    private lateinit var binding: DetallesMedicinaBinding
    private lateinit var database: BaseDatosApp

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DetallesMedicinaBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        database = BaseDatosApp.obtenerBaseDatos(this)

        val idMedicina = intent.getLongExtra("ID_MEDICINA", -1)

        if (idMedicina != -1L) {
            cargarDatos(idMedicina)
        } else {
            Toast.makeText(this, "Error al cargar", Toast.LENGTH_SHORT).show()
            finish()
        }

        binding.btnBack.setOnClickListener { finish() }
    }

    private fun cargarDatos(id: Long) {
        lifecycleScope.launch {
            val medicina = database.medicinasDao().obtenerPorId(id)

            if (medicina != null) {
                val formato = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                val inicio = formato.format(medicina.fechaInicio)
                val fin = formato.format(medicina.fechaFin)

                binding.tvNombreMedicina.text = medicina.nombre
                binding.tvDosisDetalle.text = medicina.dosis
                binding.tvFrecuenciaDetalle.text = "Cada ${medicina.frecuenciaHoras} horas"
                binding.tvFechasDetalle.text = "Desde: $inicio\nHasta: $fin"
            }
        }
    }
}