package santiagoyandres.proyectoquesorbeto;

import android.app.DatePickerDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class Prueba extends AppCompatActivity {

    ListView lista;
    ArrayList<String> arreglo;
    Button pruebabtn;
    SQLite_Class baseDeDatos;
    CalendarView calendarView;
    TextView textView123;
    DatePickerDialog.OnDateSetListener mDateSetListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prueba);

        baseDeDatos = new SQLite_Class(getApplicationContext());

        arreglo = new ArrayList<>();
        lista = (ListView) findViewById(R.id.Lista_PRU);
        pruebabtn = (Button) findViewById(R.id.Button_PRU);
        calendarView = (CalendarView) findViewById(R.id.calendarView);
        textView123 = (TextView) findViewById(R.id.textView123);



        arreglo.add("PruebaOLD");
        arreglo.add("PruebaOLD 2");

        textView123.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(Prueba.this,android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                                                               mDateSetListener,year, month, day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });

        mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month = month + 1;
                Log.d("Prueba", "onDateSet: mm/dd/yyyy: " + month + "/" + dayOfMonth + "/" + year);

                String date = month + "/" + dayOfMonth + "/" + year;
                textView123.setText(date);

                SimpleDateFormat sdf = new SimpleDateFormat("dd-M-yyy");
                String dateString = "15-08-2018";
                try {
                    Date date123 = sdf.parse(dateString);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

            }
        };

        pruebabtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(getApplicationContext(),"Prueba", Toast.LENGTH_LONG).show();
                //ArrayAdapter adaptador = new ArrayAdapter(getApplicationContext(),android.R.layout.simple_list_item_1, arreglo);
                //lista.setAdapter(adaptador);

                //crearSubitems();
                /*baseDeDatos.BorraTodosLosCliente();
                baseDeDatos.BorraCliente(3);
                baseDeDatos.BorraCliente(4);*/


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
