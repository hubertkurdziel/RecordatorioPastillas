package ifp.pmdm.practicafinal.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "medicines")
data class DatosMedicinas(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    val nombre: String,
    val dosis: String,          // Guardaremos "600 mg" o "20 ml"
    val frecuenciaHoras: Int,   // Cada cuántas horas
    val proximaDosis: Long,

    // NUEVOS CAMPOS:
    val fechaInicio: Long,      // Fecha en milisegundos
    val fechaFin: Long,         // Fecha en milisegundos

    val codigoBarras: String? = null
)