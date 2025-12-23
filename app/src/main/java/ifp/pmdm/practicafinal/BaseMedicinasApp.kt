package ifp.pmdm.practicafinal

import android.app.Application
import ifp.pmdm.practicafinal.data.BaseDatosApp

// Esta clase debe heredar de 'Application'
class BaseMedicinasApp : Application() {
    // Aquí creamos la base de datos
    val database: BaseDatosApp by lazy { BaseDatosApp.obtenerBaseDatos(this) }
}