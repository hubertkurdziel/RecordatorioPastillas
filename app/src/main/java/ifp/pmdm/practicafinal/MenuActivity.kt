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
        
        supportActionBar?.hide()

        binding.btnAddPill.setOnClickListener {
            val intent = Intent(this, AddMedicineActivity::class.java)
            startActivity(intent)
        }

        binding.btnListPills.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        binding.btnAbout.setOnClickListener {
            mostrarAcercaDe()
        }

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