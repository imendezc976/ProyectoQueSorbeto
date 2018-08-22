package santiagoyandres.proyectoquesorbeto;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class PantallaProducto extends AppCompatActivity {

    Button btnInsertar_PP;
    Button btnConsulta_PP;
    Button btnModificar_PP;
    Button btnCancelar_PP;
    EditText eTextId_PP;
    EditText eTextNombre_PP;
    EditText eTextPrecio_PP;
    ListView lViewProductos_PP;

    SQLite_Class baseDeDatos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pantalla_producto);

        baseDeDatos = new SQLite_Class(getApplicationContext());

        inicializaComponentes();
        activaCampos(false);
        listarProductos();
    }

    private void inicializaComponentes() {
        // Relacionar variables con la parte grafica
        btnInsertar_PP = (Button) findViewById(R.id.btnInsertar_PP);
        btnConsulta_PP = (Button) findViewById(R.id.btnConsulta_PP);
        btnModificar_PP = (Button) findViewById(R.id.btnModificar_PP);
        btnCancelar_PP = (Button) findViewById(R.id.btnCancelar_PP);
        eTextId_PP = (EditText) findViewById(R.id.eTextId_PP);
        eTextNombre_PP = (EditText) findViewById(R.id.eTextNombre_PP);
        eTextPrecio_PP = (EditText) findViewById(R.id.eTextPrecio_PP);
        lViewProductos_PP = (ListView) findViewById(R.id.lViewProductos_PP);

        // Funcion para el boton insertar
        btnInsertar_PP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                accionBotonInsertaProducto();
            }
        });

        //Funcion para boton consulta
        btnConsulta_PP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                accionBotonConsultaProducto();
            }
        });

        // Funcion para boton modificar
        btnModificar_PP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                accionBotonModificaProducto();
            }
        });

        // Funcion para el boton cancelar
        btnCancelar_PP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                accionBotonCancelarProducto();
            }
        });
    }

    private void LimpiaCampos() {
        eTextId_PP.setText("");
        eTextNombre_PP.setText("");
        eTextPrecio_PP.setText("");
    }

    private void activaCampos(boolean estado) {
        eTextId_PP.setEnabled(!estado);
        eTextNombre_PP.setEnabled(estado);
        eTextPrecio_PP.setEnabled(estado);
    }

    private void accionBotonInsertaProducto() {

        objProducto ProductoNuevo = new objProducto(0,
                eTextNombre_PP.getText().toString(),
                Double.parseDouble(eTextPrecio_PP.getText().toString()));
        int resultado = baseDeDatos.InsertaProducto(ProductoNuevo);
        Toast.makeText(getApplicationContext(), "Producto insertado correctamente. El id de Producto es: " + String.valueOf(resultado), Toast.LENGTH_LONG).show();
        LimpiaCampos();
        activaCampos(false);
        btnInsertar_PP.setVisibility(View.INVISIBLE);
        lViewProductos_PP.setVisibility(View.VISIBLE);
        btnCancelar_PP.setVisibility(View.INVISIBLE);
        listarProductos();
    }

    private void accionBotonConsultaProducto() {
        objProducto ProductoConsultado = new objProducto();
        ProductoConsultado = baseDeDatos.BuscaProducto_x_ID(Integer.parseInt(eTextId_PP.getText().toString()));

        if (ProductoConsultado != null) {
            activaCampos(true);
            eTextNombre_PP.setText(ProductoConsultado.nombre);
            eTextPrecio_PP.setText(String.valueOf(ProductoConsultado.precio));
            btnModificar_PP.setVisibility(View.VISIBLE);
            btnCancelar_PP.setVisibility(View.VISIBLE);
            btnConsulta_PP.setVisibility(View.INVISIBLE);
        } else {
            Toast.makeText(getApplicationContext(), "El Producto no fue encontrado. ¿Desea ingresarlo?", Toast.LENGTH_LONG).show();
            activaCampos(true);
            eTextId_PP.setText("");
            eTextId_PP.setEnabled(false);
            btnInsertar_PP.setVisibility(View.VISIBLE);
            btnCancelar_PP.setVisibility(View.VISIBLE);
            lViewProductos_PP.setVisibility(View.INVISIBLE);
        }
    }

    private void accionBotonModificaProducto() {
        objProducto ProductoAModificar = new objProducto(Integer.parseInt(eTextId_PP.getText().toString()),
                eTextNombre_PP.getText().toString(),
                Double.parseDouble(eTextPrecio_PP.getText().toString()));
        baseDeDatos.ModificaProducto(ProductoAModificar);
        Toast.makeText(getApplicationContext(), "Producto modificado correctamente.", Toast.LENGTH_LONG).show();
        btnModificar_PP.setVisibility(View.INVISIBLE);
        btnCancelar_PP.setVisibility(View.INVISIBLE);
        btnConsulta_PP.setVisibility(View.VISIBLE);
        LimpiaCampos();
        activaCampos(false);
        listarProductos();
    }

    private void accionBotonCancelarProducto() {
        LimpiaCampos();
        activaCampos(false);
        btnModificar_PP.setVisibility(View.INVISIBLE);
        btnCancelar_PP.setVisibility(View.INVISIBLE);
        btnConsulta_PP.setVisibility(View.VISIBLE);
        btnInsertar_PP.setVisibility(View.INVISIBLE);
        lViewProductos_PP.setVisibility(View.VISIBLE);
    }

    private void listarProductos() {

        HashMap<String, String> nombres = new HashMap<>();
        // nombres.put("Nombre", "Cedula");

        ArrayList<String[]> resultadoNombres = new ArrayList<String[]>();
        resultadoNombres = baseDeDatos.ConsultaProductos();

        for (String[] resultado : resultadoNombres) {
            nombres.put(resultado[0], resultado[1]);
        }

        List<HashMap<String, String>> listaDeItems = new ArrayList<>();

        SimpleAdapter adaptador = new SimpleAdapter(this, listaDeItems, android.R.layout.simple_expandable_list_item_2, new String[]{"First line", "Second line"}, new int[]{android.R.id.text1, android.R.id.text2});

        Iterator contador = nombres.entrySet().iterator();
        while (contador.hasNext()) {
            HashMap<String, String> resultaMap = new HashMap<>();
            Map.Entry pair = (Map.Entry) contador.next();
            resultaMap.put("First line", pair.getKey().toString());
            resultaMap.put("Second line", pair.getValue().toString());
            listaDeItems.add(resultaMap);
        }

        lViewProductos_PP.setAdapter(adaptador);
    }
}
