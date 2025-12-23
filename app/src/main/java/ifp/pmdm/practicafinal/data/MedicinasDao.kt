package ifp.pmdm.practicafinal.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface MedicinasDao {

    // 1. Obtener todas las medicinas
    // Usamos Flow para que si la base de datos cambia, la lista en pantalla se actualice sola
    @Query("SELECT * FROM medicines")
    fun obtenerTodas(): Flow<List<DatosMedicinas>>

    // 2. Insertar una medicina (o actualizarla si ya existe con el mismo ID)
    // 'suspend' significa que se hará en segundo plano para no congelar la app
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertar(medicina: DatosMedicinas)

    // 3. Borrar una medicina
    @Delete
    suspend fun borrar(medicina: DatosMedicinas)

    // 4. Buscar por ID (útil para ver detalles)
    @Query("SELECT * FROM medicines WHERE id = :id")
    suspend fun obtenerPorId(id: Long): DatosMedicinas?

    @androidx.room.Update
    suspend fun actualizar(medicina: DatosMedicinas)

    // Para borrar VARIAS de golpe (Selección múltiple)
    @Delete
    suspend fun borrarVarias(medicinas: List<DatosMedicinas>)
}