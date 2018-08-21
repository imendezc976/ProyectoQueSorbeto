package santiagoyandres.proyectoquesorbeto;

import android.app.DatePickerDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class PantallaFacturacion extends AppCompatActivity {

    ArrayList<String> listaDeClientes = new ArrayList<>();
    ArrayList<String> listaDeProductos = new ArrayList<>();

    DatePickerDialog.OnDateSetListener mDateSetListener;

    EditText eTextCantidad_PF;
    EditText eTextFecha_PF;
    Spinner spinCliente_PF;
    Spinner spinProducto_PF;
    TextView tViewFechaMod_PF;
    TextView tViewVTipoDeCambio_PF;
    TextView tViewVMontoTotal_PF;
    Button btnCalcular_PF;

    SQLite_Class baseDeDatos;

    Double cambio;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pantalla_facturacion);

        baseDeDatos = new SQLite_Class(getApplicationContext());

        inicializaComponentes();
        //activaCampos(false);

    }

    private void inicializaComponentes() {
        // Ejectura el api para el cambio del dolar
        cambioDelDolar cambioDelDolar = new cambioDelDolar();
        cambioDelDolar.execute();

        // Relacionar variables con la parte grafica
        eTextCantidad_PF = (EditText) findViewById(R.id.eTextCantidad_PF);
        spinCliente_PF = (Spinner) findViewById(R.id.spinCliente_PF);
        spinProducto_PF = (Spinner) findViewById(R.id.spinProducto_PF);
        tViewFechaMod_PF = (TextView) findViewById(R.id.tViewFechaMod_PF);
        tViewVTipoDeCambio_PF = (TextView) findViewById(R.id.tViewVTipoDeCambio_PF);
        tViewVMontoTotal_PF = (TextView) findViewById(R.id.tViewVMontoTotal_PF);
        btnCalcular_PF = (Button) findViewById(R.id.btnCalcular_PF);

        spinCliente_PF.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinProducto_PF.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        tViewFechaMod_PF.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(PantallaFacturacion.this,android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        mDateSetListener,year, month, day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });

        mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month = month + 1;
                Log.d("PantallaFacturacion", "onDateSet: mm/dd/yyyy: " + month + "/" + dayOfMonth + "/" + year);

                String date = dayOfMonth + "/" + month + "/" + year;
                tViewFechaMod_PF.setText(date);
            }
        };

        btnCalcular_PF.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                objProducto producto = baseDeDatos.BuscaProducto_x_Nombre(spinProducto_PF.getSelectedItem().toString());
                int Cantidad = Integer.valueOf(eTextCantidad_PF.getText().toString());
                double precioTotal = Cantidad * producto.precio;
                double precioTotalDolares = precioTotal / cambio;
                DecimalFormat df = new DecimalFormat("#.00");
                String resultado = "₡"+String.valueOf(precioTotal) + " / $" + String.valueOf(df.format(precioTotalDolares));
                tViewVMontoTotal_PF.setText(resultado);
                tViewVTipoDeCambio_PF.setText(String.valueOf(cambio));
            }
        });

        rellenaClientes();
        rellenaProductos();
    }

    private void activaCampos(boolean estado) {
        eTextCantidad_PF.setEnabled(estado);
        eTextFecha_PF.setEnabled(estado);
    }

    private void rellenaClientes(){
        ArrayList<String[]> clientes = new ArrayList<String[]>();
        clientes = baseDeDatos.ConsultaClientes();
        listaDeClientes.add("Seleccione...");
        for (String [] cliente: clientes) {
            listaDeClientes.add(cliente[1]);
        }

        // Inserta los datos obtenidos por por la base de datos en el spinner
        ArrayAdapter<String> dataAdapterClientes = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, listaDeClientes);
        spinCliente_PF.setAdapter(dataAdapterClientes);
    }

    private void rellenaProductos(){
        ArrayList<String[]> productos = new ArrayList<String[]>();
        productos = baseDeDatos.ConsultaProductos();
        listaDeProductos.add("Seleccione...");
        for (String [] producto: productos) {
            listaDeProductos.add(producto[1]);
        }

        // Inserta los datos obtenidos por por la base de datos en el spinner
        ArrayAdapter<String> dataAdapterClientes = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, listaDeProductos);
        spinProducto_PF.setAdapter(dataAdapterClientes);
    }

    private class cambioDelDolar extends AsyncTask<Void, Void, Void> {
        //String UrlTxt = "https://jsonip.com/";
        String UrlTxt = "http://www.apilayer.net/api/live?access_key=515aeb957c11b529d5e998e249d2e0ef&format=1";
        String elTextoBuffer;
        String elTextoFinal = "";

        @Override
        protected Void doInBackground(Void... params) {
            URL elUrl;
            try {
                //Nos conectamos y leemos del Servicio Web
                elUrl = new URL(UrlTxt);
                BufferedReader elBufferReader = new BufferedReader(new InputStreamReader(elUrl.openStream()));

                //Leemos linea por linea el contenido de lo leido
                while ((elTextoBuffer = elBufferReader.readLine()) != null) {
                    elTextoFinal += elTextoBuffer;
                }

                elBufferReader.close();//Cerramos el buffer

            } catch (MalformedURLException e) {
                Toast.makeText(getApplicationContext(), "Error al abrir el URL", Toast.LENGTH_LONG).show();
                //UtilesUI.MensajeToast(getApplicationContext(), "Error al abrir el URL!");
                e.printStackTrace();
                Log.d("==>>Error: ", e.toString());
            } catch (IOException e) {
                Toast.makeText(getApplicationContext(), "Error al cargar los datos!", Toast.LENGTH_LONG).show();
                //UtilesUI.MensajeToast(getApplicationContext(), "Error al cargar los datos!");
                e.printStackTrace();
                Log.d("==>>Error: ", e.toString());
            }
            return null;
        }//Fin doInBackground

        @Override
        protected void onPostExecute(Void result) {
            try {
                //Guardamos los datos en un objeto JSON
                JSONObject clienteJSON = new JSONObject(new String(elTextoFinal));
                JSONObject divisas = clienteJSON.getJSONObject("quotes");
                cambio = divisas.getDouble("USDCRC");

            } catch (JSONException e) {
                //UtilesUI.MensajeToast(getApplicationContext(), "Error al mostrar los datos!");
                e.printStackTrace();
                Log.d("==>>Error: ", e.toString());
            }
            super.onPostExecute(result);

        }//Fin onPostExecute
    }

}
