package mx.salle.appsqlliite

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.annotation.NonNull
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.lista_eventos.view.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun LlenarInformacion(){
        val datasource = EventosDataSource(this)
        val registros = ArrayList<Evento>()
        // SE ESTA LLAMANDO AL METODO PARA TRAERNOS TODA LA INFORMACION DE LA BD
        val cursor = datasource.consultarEventos()

        while (cursor.moveToNext())
        {
            val columnas = Evento(
                cursor.getInt(0),
                cursor.getString(1),
                cursor.getString(2)
            )
            registros.add(columnas)
        }

        val adaptador = AdaptadorEventos(this,registros)

        listaeventos.adapter = adaptador

        listaeventos.setOnItemClickListener{ parent, view, position, id ->

            val item = parent.getItemAtPosition(position) as Evento
            val intent = Intent(this@MainActivity, DetalleEventos::class.java)

            intent.putExtra("id", item.id_EVENTO)
            intent.putExtra("dia", item.dia_EVENTO)
            intent.putExtra("descripcion", item.descripcion_EVENTO)

            startActivity(intent)
        }
    }

    fun AgregarEventos(view: View) {
        val intent = Intent(this@MainActivity, DetalleEventos::class.java)
        startActivity(intent)
    }

    override fun onPause() {
        super.onPause()
        this.LlenarInformacion()
    }

    override fun onResume() {
        super.onResume()
        this.LlenarInformacion()
    }

    internal class AdaptadorEventos(context: Context, datos:List<Evento>):
        ArrayAdapter<Evento>(context, R.layout.lista_eventos, datos) {

        var _datos:List<Evento>

        init {
            _datos = datos
        }

        @NonNull
        override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {

            val inflater = convertView ?: LayoutInflater.from(context).inflate(R.layout.lista_eventos, parent,false)

            val currentEntity = getItem(position)

            if (currentEntity != null) {
                inflater.LblTitulo.text = currentEntity.descripcion_EVENTO
            }

            return  inflater
        }

    }
}