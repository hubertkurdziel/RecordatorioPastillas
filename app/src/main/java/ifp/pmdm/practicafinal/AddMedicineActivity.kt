package ifp.pmdm.practicafinal

import android.app.DatePickerDialog
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import ifp.pmdm.practicafinal.data.BaseDatosApp
import ifp.pmdm.practicafinal.data.DatosMedicinas
import ifp.pmdm.practicafinal.databinding.ActivityAddMedicineBinding
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class AddMedicineActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddMedicineBinding
    private lateinit var database: BaseDatosApp

    private var fechaInicioMilis: Long = 0
    private var fechaFinMilis: Long = 0
    private var idEditar: Long = -1 // Si es -1, es nuevo. Si tiene número, es editar.

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddMedicineBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        database = BaseDatosApp.obtenerBaseDatos(this)

        // 1. Comprobamos si venimos a EDITAR
        idEditar = intent.getLongExtra("ID_PARA_EDITAR", -1)

        if (idEditar != -1L) {
            binding.tvTitulo.text = "Editar Medicina"
            binding.btnGuardar.text = "Actualizar"
            cargarDatosParaEditar(idEditar)
        }

        // 2. Configurar Spinner
        val opciones = arrayOf("mg", "ml", "pastillas", "gotas")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, opciones)
        binding.spUnidadDosis.adapter = adapter

        // 3. Configurar Calendarios
        binding.etFechaInicio.setOnClickListener { mostrarCalendario(esFechaInicio = true) }
        binding.etFechaFin.setOnClickListener { mostrarCalendario(esFechaInicio = false) }

        // 4. Botones
        binding.btnBack.setOnClickListener { finish() }

        // ¡OJO! Aquí tenías dos listener. Solo necesitamos uno que llame a la función maestra
        binding.btnGuardar.setOnClickListener {
            guardarOActualizar()
        }
    }

    private fun cargarDatosParaEditar(id: Long) {
        lifecycleScope.launch {
            // Asegúrate de que tu DAO tenga: obtenerPorId(id: Long)
            val medicina = database.medicinasDao().obtenerPorId(id)
            if (medicina != null) {
                binding.etNombreMedicina.setText(medicina.nombre)
                binding.etFrecuencia.setText(medicina.frecuenciaHoras.toString())

                // Separamos "600 mg" para rellenar la caja y el spinner
                val partesDosis = medicina.dosis.split(" ")
                if (partesDosis.isNotEmpty()) {
                    binding.etDosisCantidad.setText(partesDosis[0])
                    // (Opcional: aquí podrías buscar el índice en el spinner para seleccionarlo también)
                }

                // Guardamos las fechas en las variables para que no fallen las validaciones
                fechaInicioMilis = medicina.fechaInicio
                fechaFinMilis = medicina.fechaFin

                // Mostrar texto en las cajas de fecha
                val formato = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                binding.etFechaInicio.setText(formato.format(medicina.fechaInicio))
                binding.etFechaFin.setText(formato.format(medicina.fechaFin))
            }
        }
    }

    // Esta es la función que te daba error. Faltaba LEER los datos de las cajas.
    private fun guardarOActualizar() {
        // 1. LEER LOS DATOS (Esto es lo que te faltaba)
        val nombre = binding.etNombreMedicina.text.toString()
        val cantidad = binding.etDosisCantidad.text.toString()
        val unidad = binding.spUnidadDosis.selectedItem.toString()
        val frecuenciaStr = binding.etFrecuencia.text.toString()

        // 2. VALIDACIONES
        if (nombre.isEmpty() || cantidad.isEmpty() || frecuenciaStr.isEmpty()) {
            Toast.makeText(this, "Rellena todos los campos", Toast.LENGTH_SHORT).show()
            return
        }
        if (fechaInicioMilis == 0L || fechaFinMilis == 0L) {
            Toast.makeText(this, "Selecciona las fechas", Toast.LENGTH_SHORT).show()
            return
        }
        if (fechaFinMilis < fechaInicioMilis) {
            Toast.makeText(this, "La fecha fin no puede ser antes", Toast.LENGTH_LONG).show()
            return
        }

        // 3. GUARDAR O ACTUALIZAR
        lifecycleScope.launch {
            val medicina = DatosMedicinas(
                // Si editamos, usamos el ID viejo. Si es nuevo, 0 (para que sea autoincrement)
                id = if (idEditar != -1L) idEditar else 0,
                nombre = nombre,
                dosis = "$cantidad $unidad",
                frecuenciaHoras = frecuenciaStr.toInt(),
                proximaDosis = System.currentTimeMillis(), // Nota: Esto reinicia el contador de la próxima toma
                fechaInicio = fechaInicioMilis,
                fechaFin = fechaFinMilis
            )

            if (idEditar != -1L) {
                // Asegúrate de tener @Update en tu DAO
                database.medicinasDao().actualizar(medicina)
                Toast.makeText(this@AddMedicineActivity, "¡Actualizado!", Toast.LENGTH_SHORT).show()
            } else {
                database.medicinasDao().insertar(medicina)
                Toast.makeText(this@AddMedicineActivity, "¡Guardado!", Toast.LENGTH_SHORT).show()
            }
            finish()
        }
    }

    private fun mostrarCalendario(esFechaInicio: Boolean) {
        val calendario = Calendar.getInstance()
        val datePicker = DatePickerDialog(this, { _, year, month, day ->
            val calSeleccionada = Calendar.getInstance()
            calSeleccionada.set(year, month, day)

            val formato = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            val fechaTexto = formato.format(calSeleccionada.time)

            if (esFechaInicio) {
                fechaInicioMilis = calSeleccionada.timeInMillis
                binding.etFechaInicio.setText(fechaTexto)
            } else {
                fechaFinMilis = calSeleccionada.timeInMillis
                binding.etFechaFin.setText(fechaTexto)
            }
        }, calendario.get(Calendar.YEAR), calendario.get(Calendar.MONTH), calendario.get(Calendar.DAY_OF_MONTH))

        datePicker.show()
    }
}