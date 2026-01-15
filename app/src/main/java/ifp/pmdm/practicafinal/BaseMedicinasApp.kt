package ifp.pmdm.practicafinal

import android.app.Application
import ifp.pmdm.practicafinal.data.BaseDatosApp

class BaseMedicinasApp : Application() {
    val database: BaseDatosApp by lazy { BaseDatosApp.obtenerBaseDatos(this) }
}