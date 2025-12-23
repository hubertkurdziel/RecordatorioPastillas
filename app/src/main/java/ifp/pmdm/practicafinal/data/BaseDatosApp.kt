package ifp.pmdm.practicafinal.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

// Aquí listamos todas las tablas (entities). Si creas más en el futuro, añádelas aquí.
@Database(entities = [DatosMedicinas::class], version = 1, exportSchema = false)
abstract class BaseDatosApp : RoomDatabase() {

    // Conectamos con el DAO
    abstract fun medicinasDao(): MedicinasDao

    companion object {
        // Volatile asegura que todos los hilos vean la versión más reciente de la instancia
        @Volatile
        private var INSTANCE: BaseDatosApp? = null

        fun obtenerBaseDatos(context: Context): BaseDatosApp {
            // Si la instancia no es nula, la devolvemos.
            // Si es nula, creamos la base de datos.
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    BaseDatosApp::class.java,
                    "medicinas_database" // Nombre del archivo físico en el móvil
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}