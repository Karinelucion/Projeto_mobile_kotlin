package br.edu.utpr.controlefluxocaixa

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import br.edu.utpr.controlefluxocaixa.adapter.ElementoListaAdapter
import br.edu.utpr.controlefluxocaixa.database.DatabaseHandler
import com.google.android.material.floatingactionbutton.FloatingActionButton

class ListarActivity : AppCompatActivity() {

    private lateinit var lvRegistro : ListView
    private lateinit var toolbarLista: Toolbar
    private lateinit var banco : DatabaseHandler

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_listagem)

        lvRegistro = findViewById( R.id.lvRegistros )
        toolbarLista = findViewById(R.id.toolbarLista)

        setSupportActionBar(toolbarLista)

        supportActionBar?.title = "Listagem de lançamentos"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        banco = DatabaseHandler( this )
    }

    override fun onStart() {
        super.onStart()
        val registros = banco.listCursor()
        val adapter = ElementoListaAdapter( this, registros )
        lvRegistro.adapter = adapter
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}

