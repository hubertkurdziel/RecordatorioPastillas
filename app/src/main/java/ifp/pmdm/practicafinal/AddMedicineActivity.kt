package ifp.pmdm.practicafinal

import android.app.DatePickerDialog
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.journeyapps.barcodescanner.ScanContract
import com.journeyapps.barcodescanner.ScanOptions
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
    private var idEditar: Long = -1
    private var codigoBarrasEscaneado: String = ""

    // 1. Lanzador para el escáner de registro inicial
    private val scannerLauncher = registerForActivityResult(ScanContract()) { result ->
        if (result.contents != null) {
            codigoBarrasEscaneado = result.contents
            Toast.makeText(this, "Código registrado: $codigoBarrasEscaneado", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddMedicineBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        database = BaseDatosApp.obtenerBaseDatos(this)

        idEditar = intent.getLongExtra("ID_PARA_EDITAR", -1)

        if (idEditar != -1L) {
            binding.tvTitulo.text = "Editar Medicina"
            binding.btnGuardar.text = "Actualizar"
            cargarDatosParaEditar(idEditar)
        }

        // Configurar Spinner
        val opciones = arrayOf("mg", "ml", "pastillas", "gotas")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, opciones)
        binding.spUnidadDosis.adapter = adapter

        // Configurar Calendarios
        binding.etFechaInicio.setOnClickListener { mostrarCalendario(esFechaInicio = true) }
        binding.etFechaFin.setOnClickListener { mostrarCalendario(esFechaInicio = false) }

        // Botón para escanear el código de barras de la caja (Registro)
        binding.btnEscanearRegistro.setOnClickListener {
            val options = ScanOptions().apply {
                setPrompt("Escanea el código de la caja para registrarlo")
                setBeepEnabled(true)
                setOrientationLocked(false)
            }
            scannerLauncher.launch(options)
        }

        binding.btnBack.setOnClickListener { finish() }

        binding.btnGuardar.setOnClickListener {
            guardarOActualizar()
        }
    }

    private fun cargarDatosParaEditar(id: Long) {
        lifecycleScope.launch {
            val medicina = database.medicinasDao().obtenerPorId(id)
            if (medicina != null) {
                binding.etNombreMedicina.setText(medicina.nombre)
                binding.etFrecuencia.setText(medicina.frecuenciaHoras.toString())
                codigoBarrasEscaneado = medicina.codigoBarras ?: ""

                val partesDosis = medicina.dosis.split(" ")
                if (partesDosis.isNotEmpty()) {
                    binding.etDosisCantidad.setText(partesDosis[0])
                }

                fechaInicioMilis = medicina.fechaInicio
                fechaFinMilis = medicina.fechaFin

                val formato = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                binding.etFechaInicio.setText(formato.format(medicina.fechaInicio))
                binding.etFechaFin.setText(formato.format(medicina.fechaFin))
            }
        }
    }

    private fun guardarOActualizar() {
        val nombre = binding.etNombreMedicina.text.toString()
        val cantidad = binding.etDosisCantidad.text.toString()
        val unidad = binding.spUnidadDosis.selectedItem.toString()
        val frecuenciaStr = binding.etFrecuencia.text.toString()

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

        lifecycleScope.launch {
            val frecuenciaHoras = frecuenciaStr.toInt()

            val medicina = DatosMedicinas(
                id = if (idEditar != -1L) idEditar else 0,
                nombre = nombre,
                dosis = "$cantidad $unidad",
                frecuenciaHoras = frecuenciaHoras,
                proximaDosis = System.currentTimeMillis() + (frecuenciaHoras * 3600000L),
                fechaInicio = fechaInicioMilis,
                fechaFin = fechaFinMilis,
                codigoBarras = codigoBarrasEscaneado
            )

            val helper = AlarmHelper(this@AddMedicineActivity)

            if (idEditar != -1L) {
                database.medicinasDao().actualizar(medicina)
                helper.programarAlarma(medicina)
                Toast.makeText(this@AddMedicineActivity, "¡Actualizado!", Toast.LENGTH_SHORT).show()
            } else {
                val nuevoId = database.medicinasDao().insertar(medicina)
                val medicinaConId = medicina.copy(id = nuevoId)
                helper.programarAlarma(medicinaConId)
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