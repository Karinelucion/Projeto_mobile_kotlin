package br.edu.utpr.controlefluxocaixa.adapter

import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.health.connect.datatypes.units.Length
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import br.edu.utpr.controlefluxocaixa.R
import br.edu.utpr.controlefluxocaixa.database.DatabaseHandler
import br.edu.utpr.controlefluxocaixa.database.DatabaseHandler.Companion.DATA
import br.edu.utpr.controlefluxocaixa.database.DatabaseHandler.Companion.DETALHE
import br.edu.utpr.controlefluxocaixa.database.DatabaseHandler.Companion.ID
import br.edu.utpr.controlefluxocaixa.database.DatabaseHandler.Companion.TIPO
import br.edu.utpr.controlefluxocaixa.database.DatabaseHandler.Companion.VALOR
import br.edu.utpr.controlefluxocaixa.entity.Cadastro
import java.text.NumberFormat
import java.util.Locale

class ElementoListaAdapter(val context: Context, var cursor: Cursor) : BaseAdapter() {
    private lateinit var banco : DatabaseHandler

    override fun getCount(): Int {
        return cursor.count
    }

    override fun getItem(position: Int): Any {
        cursor.moveToPosition(position)
        return Cadastro(
            cursor.getInt(ID),
            cursor.getString(TIPO),
            cursor.getString(DETALHE),
            cursor.getFloat(VALOR),
            cursor.getString(DATA)
        )
    }

    override fun getItemId(position: Int): Long {
        cursor.moveToPosition(position)
        return cursor.getInt(ID).toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val v = convertView ?: inflater.inflate(R.layout.actvity_elemento_lista, parent, false)

        val imageView: ImageView = v.findViewById(R.id.imageView)
        val tvTipoElementoLista = v.findViewById<TextView>(R.id.tvTipoElementoLista)
        val tvDetalheElementoLista = v.findViewById<TextView>(R.id.tvDetalheElementoLista)
        val tvValorElementoLista = v.findViewById<TextView>(R.id.tvValorElementoLista)
        val tvDataElementoLista = v.findViewById<TextView>(R.id.tvDataElementoLista)
        val btExcluirElementoLista = v.findViewById<ImageButton>(R.id.btExcluirElementoLista)

        cursor.moveToPosition(position)

        val valor = cursor.getFloat(VALOR)

        val formatoMonetarioBR = NumberFormat.getCurrencyInstance(Locale("pt", "BR"))

        tvValorElementoLista.text = formatoMonetarioBR.format(valor)
        tvTipoElementoLista.text = cursor.getString(TIPO)
        tvDetalheElementoLista.text = cursor.getString(DETALHE)
        tvDataElementoLista.text = cursor.getString(DATA)

        val tipo = cursor.getString(TIPO)

        val iconResId = if (tipo == "Crédito") {
            R.drawable.up
        } else {
            R.drawable.down
        }
        imageView.setImageDrawable(ContextCompat.getDrawable(context, iconResId))

        banco = DatabaseHandler( context )

        btExcluirElementoLista.setOnClickListener{
            cursor.moveToPosition(position)
            val idToDelete = cursor.getInt(ID)
            banco.delete(idToDelete)
            updateCursor()

            Toast.makeText(context,"Movimentação excluída com sucesso!", Toast.LENGTH_SHORT).show()
        }
        return v
    }

    private fun updateCursor() {
        val newCursor = banco.listCursor()
        cursor.close()
        cursor = newCursor
        notifyDataSetChanged()
    }
}
