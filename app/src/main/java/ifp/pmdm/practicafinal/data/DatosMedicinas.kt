package ifp.pmdm.practicafinal.data


import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "medicines")
data class DatosMedicinas(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    val nombre: String,           // Ej: "Paracetamol"
    val dosis: String,         // Ej: "1 pastilla"
    val frecuenciaHoras: Int,    // Ej: 8 (cada 8 horas)
    val proximaDosis: Long,     // Fecha en milisegundos de la próxima toma

    // REQUISITO: El código de barras que desbloquea la alarma
    val codigoBarras: String? = null
)