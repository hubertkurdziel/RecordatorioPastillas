package ifp.pmdm.practicafinal

import android.content.Intent
import android.os.Bundle
import android.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import ifp.pmdm.practicafinal.databinding.ActivityMenuBinding

class MenuActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMenuBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMenuBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        // Ocultar la ActionBar para que se vea como en el diseño
        supportActionBar?.hide()

        // Botón: Añadir Pastilla -> Abre AddMedicineActivity
        binding.btnAddPill.setOnClickListener {
            val intent = Intent(this, AddMedicineActivity::class.java)
            startActivity(intent)
        }

        // Botón: Lista Pastillas -> Abre MainActivity (Lista)
        binding.btnListPills.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        // Botón: Acerca de -> Muestra un diálogo informativo
        binding.btnAbout.setOnClickListener {
            mostrarAcercaDe()
        }

        // Los botones de Configuración y Ayuda no hacen nada (como solicitaste)
        // binding.btnSettings.setOnClickListener { }
        // binding.btnHelp.setOnClickListener { }
    }

    private fun mostrarAcercaDe() {
        AlertDialog.Builder(this)
            .setTitle(getString(R.string.about_title))
            .setMessage(getString(R.string.about_text))
            .setPositiveButton("OK") { dialog, _ ->
                dialog.dismiss()
            }
            .create()
            .show()
    }
}