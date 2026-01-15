package ifp.pmdm.practicafinal.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "medicines")
data class DatosMedicinas(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    val nombre: String,
    val dosis: String,
    val frecuenciaHoras: Int,
    val proximaDosis: Long,

    val fechaInicio: Long,
    val fechaFin: Long,

    val codigoBarras: String? = null
)