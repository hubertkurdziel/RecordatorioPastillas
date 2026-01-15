package ifp.pmdm.practicafinal.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [DatosMedicinas::class], version = 1, exportSchema = false)
abstract class BaseDatosApp : RoomDatabase() {

    abstract fun medicinasDao(): MedicinasDao

    companion object {
        @Volatile
        private var INSTANCE: BaseDatosApp? = null

        fun obtenerBaseDatos(context: Context): BaseDatosApp {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    BaseDatosApp::class.java,
                    "medicinas_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}