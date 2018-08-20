package santiagoyandres.proyectoquesorbeto;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class PantallaFacturacion extends AppCompatActivity {

    ArrayList<String> listaDeClientes = new ArrayList<>();
    ArrayList<String> listaDeProductos = new ArrayList<>();

    EditText eTextNumero_PF;
    EditText eTextCantidad_PF;
    EditText eTextFecha_PF;
    Spinner spinCliente_PF;
    Spinner spinProducto_PF;

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

        rellenaClientes();

        // Relacionar variables con la parte grafica
        eTextNumero_PF = (EditText) findViewById(R.id.eTextNumero_PF);
        eTextCantidad_PF = (EditText) findViewById(R.id.eTextCantidad_PF);
        eTextFecha_PF = (EditText) findViewById(R.id.eTextFecha_PF);
        spinCliente_PF = (Spinner) findViewById(R.id.spinCliente_PF);
        spinProducto_PF = (Spinner) findViewById(R.id.spinProducto_PF);
    }

    private void activaCampos(boolean estado) {
        eTextNumero_PF.setEnabled(!estado);
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

        spinCliente_PF.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // textViewPrueba.setText("Seleccion: ");
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
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
