package ifp.pmdm.practicafinal

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import ifp.pmdm.practicafinal.data.BaseDatosApp
import ifp.pmdm.practicafinal.databinding.ActivityMainBinding
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: MedicinasAdapter
    private lateinit var database: BaseDatosApp

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        database = BaseDatosApp.obtenerBaseDatos(this)

        // CONFIGURAR ADAPTER CON LA LÓGICA DE SELECCIÓN
        adapter = MedicinasAdapter(
            lista = emptyList(),
            onClick = { medicina ->
                // Click normal: Ir a detalle
                val intent = Intent(this, DetailMedicineActivity::class.java)
                intent.putExtra("ID_MEDICINA", medicina.id)
                startActivity(intent)
            },
            onSelectionChanged = { cantidad ->
                actualizarBarraSeleccion(cantidad)
            }
        )

        binding.rvMedicinas.layoutManager = LinearLayoutManager(this)
        binding.rvMedicinas.adapter = adapter

        // Cargar datos
        lifecycleScope.launch {
            database.medicinasDao().obtenerTodas().collect { lista ->
                adapter.actualizarLista(lista)
            }
        }

        // --- BOTONES DE LA BARRA DE SELECCIÓN ---

        // 1. Cerrar selección (flecha atrás)

        // 2. Borrar (Papelera)
        binding.btnBorrar.setOnClickListener {
            mostrarDialogoConfirmacion()
        }

        // 3. Editar (Lápiz)
        binding.btnEditar.setOnClickListener {
            val idParaEditar = adapter.obtenerUnicoIdSeleccionado()
            if (idParaEditar != null) {
                // REUTILIZAMOS LA PANTALLA DE AÑADIR PERO EN MODO EDICIÓN
                val intent = Intent(this, AddMedicineActivity::class.java)
                intent.putExtra("ID_PARA_EDITAR", idParaEditar)
                startActivity(intent)
                adapter.limpiarSeleccion() // Salir del modo selección
            }
        }

        // Botón flotante normal
        binding.fabAgregar.setOnClickListener {
            startActivity(Intent(this, AddMedicineActivity::class.java))
        }
    }

    private fun actualizarBarraSeleccion(cantidad: Int) {
        if (cantidad > 0) {
            // HAY SELECCIÓN:
            // 1. Mostrar el panel inferior
            binding.bottomActionPanel.visibility = View.VISIBLE
            // 2. Ocultar el botón flotante (+) para que no moleste
            binding.fabAgregar.visibility = View.GONE

            // Lógica: Si hay 1 -> Mostrar Editar. Si hay > 1 -> Ocultar Editar
            if (cantidad == 1) {
                binding.btnEditar.visibility = View.VISIBLE
            } else {
                binding.btnEditar.visibility = View.GONE
            }
        } else {
            // NO HAY SELECCIÓN:
            // 1. Ocultar el panel inferior
            binding.bottomActionPanel.visibility = View.GONE
            // 2. Volver a mostrar el botón flotante (+)
            binding.fabAgregar.visibility = View.VISIBLE
        }
    }

    private fun mostrarDialogoConfirmacion() {
        AlertDialog.Builder(this)
            .setTitle("¿Borrar medicinas?")
            .setMessage("Se eliminarán los elementos seleccionados permanentemente.")
            .setPositiveButton("Borrar") { _, _ ->
                lifecycleScope.launch {
                    // Borramos la lista seleccionada
                    val listaABorrar = adapter.obtenerItemsSeleccionados()
                    database.medicinasDao().borrarVarias(listaABorrar)
                    adapter.limpiarSeleccion()
                    Toast.makeText(applicationContext, "Eliminados", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }

    // Si pulsan el botón "Atrás" del móvil y hay selección, quitamos la selección primero
    override fun onBackPressed() {
        if (adapter.seleccionados.isNotEmpty()) {
            adapter.limpiarSeleccion()
        } else {
            super.onBackPressed()
        }
    }
}