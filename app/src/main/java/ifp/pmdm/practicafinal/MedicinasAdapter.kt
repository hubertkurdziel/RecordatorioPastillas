package ifp.pmdm.practicafinal

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import ifp.pmdm.practicafinal.data.DatosMedicinas

class MedicinasAdapter(
    private var lista: List<DatosMedicinas>,
    private val onClick: (DatosMedicinas) -> Unit,       // Click normal
    private val onSelectionChanged: (Int) -> Unit        // Avisar al Main cuántos hay seleccionados
) : RecyclerView.Adapter<MedicinasAdapter.MedicinaViewHolder>() {

    // Lista de IDs seleccionados
    val seleccionados = mutableSetOf<Long>()
    var modoSeleccion = false

    class MedicinaViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val card: CardView = view as CardView
        val tvNombre: TextView = view.findViewById(R.id.tvNombre)
        val tvDosis: TextView = view.findViewById(R.id.tvDosis)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MedicinaViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_medicina, parent, false)
        return MedicinaViewHolder(view)
    }

    override fun onBindViewHolder(holder: MedicinaViewHolder, position: Int) {
        val medicina = lista[position]
        holder.tvNombre.text = medicina.nombre
        holder.tvDosis.text = medicina.dosis

        // CAMBIO DE COLOR: Si está seleccionado, ponerlo morado claro, si no, blanco
        if (seleccionados.contains(medicina.id)) {
            holder.card.setCardBackgroundColor(Color.parseColor("#E8DEF8"))
        } else {
            holder.card.setCardBackgroundColor(Color.WHITE)
        }

        // LOGICA DE CLICKS
        holder.itemView.setOnClickListener {
            if (modoSeleccion) {
                toggleSeleccion(medicina.id)
            } else {
                onClick(medicina)
            }
        }

        holder.itemView.setOnLongClickListener {
            if (!modoSeleccion) {
                modoSeleccion = true
                toggleSeleccion(medicina.id)
            }
            true // Indica que consumimos el evento long click
        }
    }

    private fun toggleSeleccion(id: Long) {
        if (seleccionados.contains(id)) {
            seleccionados.remove(id)
        } else {
            seleccionados.add(id)
        }

        // Si quitamos el último, salimos del modo selección
        if (seleccionados.isEmpty()) {
            modoSeleccion = false
        }

        notifyDataSetChanged() // Refrescar colores
        onSelectionChanged(seleccionados.size) // Avisar al Main
    }

    // Función para obtener los objetos completos seleccionados (para borrarlos)
    fun obtenerItemsSeleccionados(): List<DatosMedicinas> {
        return lista.filter { seleccionados.contains(it.id) }
    }

    // Función para obtener EL ÚNICO ID seleccionado (para editar)
    fun obtenerUnicoIdSeleccionado(): Long? {
        return seleccionados.firstOrNull()
    }

    fun limpiarSeleccion() {
        seleccionados.clear()
        modoSeleccion = false
        notifyDataSetChanged()
        onSelectionChanged(0)
    }

    override fun getItemCount() = lista.size

    fun actualizarLista(nuevaLista: List<DatosMedicinas>) {
        lista = nuevaLista
        notifyDataSetChanged()
    }
}