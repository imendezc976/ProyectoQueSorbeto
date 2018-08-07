package santiagoyandres.proyectoquesorbeto;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class Prueba extends AppCompatActivity {

    ListView lista;
    ArrayList<String> arreglo;
    Button pruebabtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prueba);

        arreglo = new ArrayList<>();
        lista = (ListView) findViewById(R.id.Lista_PRU);
        pruebabtn = (Button) findViewById(R.id.Button_PRU);

        arreglo.add("PruebaOLD");
        arreglo.add("PruebaOLD 2");

        pruebabtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(getApplicationContext(),"Prueba", Toast.LENGTH_LONG).show();
                //ArrayAdapter adaptador = new ArrayAdapter(getApplicationContext(),android.R.layout.simple_list_item_1, arreglo);
                //lista.setAdapter(adaptador);

                crearSubitems();
            }
        });
    }

    private void crearSubitems(){

        HashMap<String, String> nombres = new HashMap<>();
        nombres.put("Nombre", "Cedula");
        nombres.put("Nombre2", "Cedula2");
        nombres.put("Nombre3", "Cedula3");

        List<HashMap<String, String>> listaDeItems = new ArrayList<>();

        SimpleAdapter adaptador = new SimpleAdapter(this, listaDeItems, android.R.layout.simple_expandable_list_item_2, new String[]{"First line", "Second line"}, new int[]{android.R.id.text1, android.R.id.text2});

        Iterator contador = nombres.entrySet().iterator();
        while (contador.hasNext()){
            HashMap<String, String> resultaMap = new HashMap<>();
            Map.Entry pair = (Map.Entry) contador.next();
            resultaMap.put("First line", pair.getKey().toString());
            resultaMap.put("Second line", pair.getValue().toString());
            listaDeItems.add(resultaMap);
        }

        lista.setAdapter(adaptador);
    }
}
