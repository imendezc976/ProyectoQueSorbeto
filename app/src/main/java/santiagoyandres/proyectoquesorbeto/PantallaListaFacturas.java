package santiagoyandres.proyectoquesorbeto;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class PantallaListaFacturas extends AppCompatActivity {

    ListView lViewFactura_PLF;

    SQLite_Class baseDeDatos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pantalla_lista_facturas);

        baseDeDatos = new SQLite_Class(getApplicationContext());

        inicializarComponentes();
        listarFacturas();
    }

    private void inicializarComponentes(){
        lViewFactura_PLF = (ListView) findViewById(R.id.lViewFactura_PLF);
    }

    private void listarFacturas(){

        HashMap<String, String> facturas = new HashMap<>();
        // nombres.put("Nombre", "Cedula");

        ArrayList<String[]> resultadofacturas = new ArrayList<String[]>();
        resultadofacturas = baseDeDatos.ConsultaFactura();

        for(String[] resultado : resultadofacturas){
            facturas.put(resultado[0], resultado[1]);
        }

        List<HashMap<String, String>> listaDeItems = new ArrayList<>();

        SimpleAdapter adaptador = new SimpleAdapter(this, listaDeItems, android.R.layout.simple_expandable_list_item_2, new String[]{"First line", "Second line"}, new int[]{android.R.id.text1, android.R.id.text2});

        Iterator contador = facturas.entrySet().iterator();
        while (contador.hasNext()){
            HashMap<String, String> resultaMap = new HashMap<>();
            Map.Entry pair = (Map.Entry) contador.next();
            resultaMap.put("First line", pair.getKey().toString());
            resultaMap.put("Second line", pair.getValue().toString());
            listaDeItems.add(resultaMap);
        }

        lViewFactura_PLF.setAdapter(adaptador);
    }
}
