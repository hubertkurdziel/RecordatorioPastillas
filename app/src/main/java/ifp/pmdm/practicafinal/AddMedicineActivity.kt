package ifp.pmdm.practicafinal

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import ifp.pmdm.practicafinal.data.BaseDatosApp
import ifp.pmdm.practicafinal.data.DatosMedicinas
import ifp.pmdm.practicafinal.databinding.ActivityAddMedicineBinding
import kotlinx.coroutines.launch

class AddMedicineActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddMedicineBinding
    private lateinit var database: BaseDatosApp

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddMedicineBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Ocultar la barra de acción por defecto
        supportActionBar?.hide()

        // Configurar el botón de atrás personalizado
        binding.btnBack.setOnClickListener {
            // Usamos finish() para cerrar la actividad, es más directo y seguro
            finish()
        }

        database = BaseDatosApp.obtenerBaseDatos(this)

        binding.btnGuardar.setOnClickListener {
            guardarMedicina()
        }
    }

    private fun guardarMedicina() {
        val nombre = binding.etNombreMedicina.text.toString()
        val dosis = binding.etDosis.text.toString()

        if (nombre.isNotEmpty() && dosis.isNotEmpty()) {
            lifecycleScope.launch {
                val nuevaMedicina = DatosMedicinas(
                    nombre = nombre,
                    dosis = dosis,
                    frecuenciaHoras = 8,
                    proximaDosis = System.currentTimeMillis()
                )

                database.medicinasDao().insertar(nuevaMedicina)
                
                Toast.makeText(this@AddMedicineActivity, "Guardado", Toast.LENGTH_SHORT).show()
                finish() // Cierra esta actividad y vuelve a la anterior
            }
        } else {
            Toast.makeText(this, "Por favor completa todos los campos", Toast.LENGTH_SHORT).show()
        }
    }
}