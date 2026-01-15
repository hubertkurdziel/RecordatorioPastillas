package ifp.pmdm.practicafinal.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface MedicinasDao {

    @Query("SELECT * FROM medicines")
    fun obtenerTodas(): Flow<List<DatosMedicinas>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertar(medicina: DatosMedicinas): Long

    @Delete
    suspend fun borrar(medicina: DatosMedicinas)

    @Query("SELECT * FROM medicines WHERE id = :id")
    suspend fun obtenerPorId(id: Long): DatosMedicinas?

    @androidx.room.Update
    suspend fun actualizar(medicina: DatosMedicinas)

    @Delete
    suspend fun borrarVarias(medicinas: List<DatosMedicinas>)
}