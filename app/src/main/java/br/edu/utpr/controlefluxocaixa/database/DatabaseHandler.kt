package br.edu.utpr.controlefluxocaixa.database

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import br.edu.utpr.controlefluxocaixa.entity.Cadastro

class DatabaseHandler (context : Context) : SQLiteOpenHelper ( context, DATABASE_NAME, null, DATABASE_VERSION ) {

    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL( "CREATE TABLE IF NOT EXISTS ${TABLE_NAME} ( _id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                " tipo TEXT, detalhe TEXT, valor FLOAT, data DATE)")
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL( "DROP TABLE IF EXISTS ${TABLE_NAME}" )
        onCreate(db)
    }

    companion object {
        private const val DATABASE_NAME = "dbfile.sqlite"
        private const val DATABASE_VERSION = 3
        private const val TABLE_NAME = "cadastro"
        public const val ID = 0
        public const val TIPO = 1
        public const val DETALHE = 2
        public const val VALOR = 3
        public const val DATA = 4
    }

    fun insert( cadastro: Cadastro) {
        val db = this.writableDatabase

        val registro = ContentValues()
        registro.put("tipo", cadastro.tipo)
        registro.put("detalhe", cadastro.detalhe)
        registro.put("valor", cadastro.valor)
        registro.put("data",  cadastro.data)

        db.insert( TABLE_NAME, null, registro )
    }

    fun update( cadastro : Cadastro ) {
        val db = this.writableDatabase

        val registro = ContentValues()
        registro.put( "tipo", cadastro.tipo )
        registro.put( "detalhe", cadastro.detalhe)
        registro.put("valor", cadastro.valor)
        registro.put("data",  cadastro.data)

        db.update( TABLE_NAME, registro, "_id=${cadastro._id}", null )
    }

    fun delete( id : Int ) {
        val db = this.writableDatabase

        db.delete( TABLE_NAME, "_id=${id}", null )
    }

    fun find(id : Int) : Cadastro? {
        val db = this.writableDatabase

        val registro : Cursor = db.query( TABLE_NAME,
            null,
            "_id=${id}",
            null,
            null,
            null,
            null
        )

        if ( registro.moveToNext() ) {
            val cadastro = Cadastro(
                id,
                registro.getString(TIPO),
                registro.getString(DETALHE),
                registro.getFloat(VALOR),
                registro.getString(DATA)
            )
            return cadastro
        } else {
            return null
        }
    }

    fun list() : MutableList<Cadastro> {
        val db = this.writableDatabase

        val registro = db.query( TABLE_NAME,
            null,
            null,
            null,
            null,
            null,
            null
        )

        var registros = mutableListOf<Cadastro>()

        while ( registro.moveToNext() ) {

            val cadastro = Cadastro(
                registro.getInt( ID ),
                registro.getString( TIPO ),
                registro.getString( DETALHE ),
                registro.getFloat(VALOR),
                registro.getString(DATA)
            )
            registros.add( cadastro )
        }
        return registros
    }

    fun listCursor() : Cursor {
        val db = this.writableDatabase

        val registro = db.query( TABLE_NAME,
            null,
            null,
            null,
            null,
            null,
            null
        )
        return registro
    }

    fun findByType(tipo: String): Float {
        val db = this.writableDatabase
        var sumValores: Float = 0.0F

        val query = "SELECT SUM(c.valor) FROM $TABLE_NAME c WHERE c.tipo = ?"
        val cursor = db.rawQuery(query, arrayOf(tipo))

        if (cursor.moveToFirst()) {
            val sum = cursor.getFloat(0)
            sumValores = sum
        }

        cursor.close()
        return sumValores
    }
}