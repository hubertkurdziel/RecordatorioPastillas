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

        binding.btnBack.setOnClickListener {
            finish()
        }

        database = BaseDatosApp.obtenerBaseDatos(this)

        adapter = MedicinasAdapter(
            lista = emptyList(),
            onClick = { medicina ->
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

        lifecycleScope.launch {
            database.medicinasDao().obtenerTodas().collect { lista ->
                adapter.actualizarLista(lista)
            }
        }


        binding.btnBorrar.setOnClickListener {
            mostrarDialogoConfirmacion()
        }

        binding.btnEditar.setOnClickListener {
            val idParaEditar = adapter.obtenerUnicoIdSeleccionado()
            if (idParaEditar != null) {
                val intent = Intent(this, AddMedicineActivity::class.java)
                intent.putExtra("ID_PARA_EDITAR", idParaEditar)
                startActivity(intent)
                adapter.limpiarSeleccion()
            }
        }

        binding.fabAgregar.setOnClickListener {
            startActivity(Intent(this, AddMedicineActivity::class.java))
        }
    }

    private fun actualizarBarraSeleccion(cantidad: Int) {
        if (cantidad > 0) {
            binding.bottomActionPanel.visibility = View.VISIBLE
            binding.fabAgregar.visibility = View.GONE

            if (cantidad == 1) {
                binding.btnEditar.visibility = View.VISIBLE
            } else {
                binding.btnEditar.visibility = View.GONE
            }
        } else {
            binding.bottomActionPanel.visibility = View.GONE
            binding.fabAgregar.visibility = View.VISIBLE
        }
    }

    private fun mostrarDialogoConfirmacion() {
        AlertDialog.Builder(this)
            .setTitle(getString(R.string.main_delete_dialog_title))
            .setMessage(getString(R.string.main_delete_dialog_message))
            .setPositiveButton(getString(R.string.main_delete_dialog_positive_button)) { _, _ ->
                lifecycleScope.launch {
                    val listaABorrar = adapter.obtenerItemsSeleccionados()
                    database.medicinasDao().borrarVarias(listaABorrar)
                    adapter.limpiarSeleccion()
                    Toast.makeText(applicationContext, getString(R.string.main_delete_toast_deleted), Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton(getString(R.string.main_delete_dialog_negative_button), null)
            .show()
    }

    override fun onBackPressed() {
        if (adapter.seleccionados.isNotEmpty()) {
            adapter.limpiarSeleccion()
        } else {
            super.onBackPressed()
        }
    }
}