package ifp.pmdm.practicafinal

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ifp.pmdm.practicafinal.data.DatosMedicinas

class MedicinasAdapter(private var lista: List<DatosMedicinas>) :
    RecyclerView.Adapter<MedicinasAdapter.MedicinaViewHolder>() {

    class MedicinaViewHolder(view: View) : RecyclerView.ViewHolder(view) {
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
    }

    override fun getItemCount() = lista.size

    // Función para actualizar la lista cuando guardamos una nueva
    fun actualizarLista(nuevaLista: List<DatosMedicinas>) {
        lista = nuevaLista
        notifyDataSetChanged()
    }
}