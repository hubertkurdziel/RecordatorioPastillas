package ifp.pmdm.practicafinal

import android.content.Intent
import android.os.Bundle
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
        
        // Ocultar la barra superior en la pantalla principal para que se vea más limpia
        supportActionBar?.hide()

        // 1. Iniciar Base de Datos
        database = BaseDatosApp.obtenerBaseDatos(this)

        // 2. Configurar el RecyclerView
        adapter = MedicinasAdapter(emptyList())
        binding.rvMedicinas.layoutManager = LinearLayoutManager(this)
        binding.rvMedicinas.adapter = adapter

        // 3. Cargar datos
        lifecycleScope.launch {
            database.medicinasDao().obtenerTodas().collect { listaPastillas ->
                adapter.actualizarLista(listaPastillas)
            }
        }

        // 4. Configurar botón Flotante (FAB) para ir a la nueva pantalla
        binding.fabAgregar.setOnClickListener {
            val intent = Intent(this, AddMedicineActivity::class.java)
            startActivity(intent)
        }
    }
}