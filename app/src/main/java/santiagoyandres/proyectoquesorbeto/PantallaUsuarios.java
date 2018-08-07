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
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class PantallaUsuarios extends AppCompatActivity {

    ArrayList<String> paises = new ArrayList<>();

    Spinner spinPaises_PU;
    Button btnInsertar_PU;
    Button btnConsulta_PU;
    Button btnModificar_PU;
    Button btnCancelar_PU;
    EditText eTextId_PU;
    EditText eTextNombre_PU;
    EditText eTextTelefono_PU;
    ListView lViewClientes_PU;

    SQLite_Class baseDeDatos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pantalla_usuarios);

        baseDeDatos = new SQLite_Class(getApplicationContext());

        paises.add("Seleccione...");
        paisesApi rellenaPaises = new paisesApi();
        rellenaPaises.execute();

        inicializaComponentes();
        activaCampos(false);
        listarClientes();
    }

    private void inicializaComponentes(){
        // Relacionar variables con la parte grafica
        spinPaises_PU = (Spinner) findViewById(R.id.spinPaises_PU);
        btnInsertar_PU = (Button) findViewById(R.id.btnInsertar_PU);
        btnConsulta_PU = (Button) findViewById(R.id.btnConsulta_PU);
        btnModificar_PU = (Button) findViewById(R.id.btnModificar_PU);
        btnCancelar_PU = (Button) findViewById(R.id.btnCancelar_PU);
        eTextId_PU = (EditText) findViewById(R.id.eTextId_PU);
        eTextNombre_PU = (EditText) findViewById(R.id.eTextNombre_PU);
        eTextTelefono_PU = (EditText) findViewById(R.id.eTextTelefono_PU);
        lViewClientes_PU = (ListView) findViewById(R.id.lViewClientes_PU);


        // Inserta los datos obtenidos por el API de los paises, en el spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, paises);
        //dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinPaises_PU.setAdapter(dataAdapter);

        // Funcion del spinnner
        spinPaises_PU.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // textViewPrueba.setText("Seleccion: ");
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        // Funcion para el boton insertar
        btnInsertar_PU.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                accionBotonInserta();
            }
        });

        //Funcion para boton consulta
        btnConsulta_PU.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                accionBotonConsulta();
            }
        });

        // Funcion para boton modificar
        btnModificar_PU.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                accionBotonModifica();
            }
        });

        // Funcion para el boton cancelar
        btnCancelar_PU.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                accionBotonCancelar();
            }
        });
    }

    private void LimpiaCampos(){
        eTextId_PU.setText("");
        eTextNombre_PU.setText("");
        eTextTelefono_PU.setText("");
        spinPaises_PU.setSelection(0);
    }

    private void activaCampos(boolean estado){
        eTextId_PU.setEnabled(!estado);
        eTextNombre_PU.setEnabled(estado);
        eTextTelefono_PU.setEnabled(estado);
        spinPaises_PU.setEnabled(estado);
    }

    private void accionBotonInserta(){
        objCliente clienteNuevo = new objCliente(Integer.parseInt(eTextId_PU.getText().toString()),
                                                eTextNombre_PU.getText().toString(),
                                                eTextTelefono_PU.getText().toString(),
                                                spinPaises_PU.getSelectedItem().toString());
        int resultado = baseDeDatos.InsertaCliente(clienteNuevo);
        Toast.makeText(getApplicationContext(), "Cliente insertado correctamente. El id de cliente es: " + String.valueOf(resultado), Toast.LENGTH_LONG).show();
        LimpiaCampos();
        activaCampos(false);
        btnInsertar_PU.setVisibility(View.INVISIBLE);
        lViewClientes_PU.setVisibility(View.VISIBLE);
    }

    private void accionBotonConsulta(){
        objCliente clienteConsultado = new objCliente();
        clienteConsultado = baseDeDatos.BuscaPersona_x_ID(Integer.parseInt(eTextId_PU.getText().toString()));

        if(clienteConsultado != null){
            activaCampos(true);
            eTextNombre_PU.setText(clienteConsultado.nombre);
            eTextTelefono_PU.setText(clienteConsultado.telefono);
            ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, paises);
            spinPaises_PU.setAdapter(dataAdapter);
            int opcionSpinner = dataAdapter.getPosition(clienteConsultado.nacionalidad);
            spinPaises_PU.setSelection(opcionSpinner);
            btnModificar_PU.setVisibility(View.VISIBLE);
            btnCancelar_PU.setVisibility(View.VISIBLE);
            btnConsulta_PU.setVisibility(View.INVISIBLE);
        } else{
            Toast.makeText(getApplicationContext(), "El cliente no fue encontrado. ¿Desea ingresarlo?", Toast.LENGTH_LONG).show();
            activaCampos(true);
            eTextId_PU.setEnabled(true);
            btnInsertar_PU.setVisibility(View.VISIBLE);
            btnCancelar_PU.setVisibility(View.VISIBLE);
            lViewClientes_PU.setVisibility(View.INVISIBLE);
        }
    }

    private void accionBotonModifica(){
        objCliente clienteAModificar = new objCliente(Integer.parseInt(eTextId_PU.getText().toString()),
                                                        eTextNombre_PU.getText().toString(),
                                                        eTextTelefono_PU.getText().toString(),
                                                        spinPaises_PU.getSelectedItem().toString());
        baseDeDatos.ModificaCliente(clienteAModificar);
        Toast.makeText(getApplicationContext(), "Cliente modificado correctamente.", Toast.LENGTH_LONG).show();
        btnModificar_PU.setVisibility(View.INVISIBLE);
        btnCancelar_PU.setVisibility(View.INVISIBLE);
        btnConsulta_PU.setVisibility(View.VISIBLE);
        LimpiaCampos();
        activaCampos(false);
    }
    private void accionBotonCancelar(){
        LimpiaCampos();
        activaCampos(false);
        btnModificar_PU.setVisibility(View.INVISIBLE);
        btnCancelar_PU.setVisibility(View.INVISIBLE);
        btnConsulta_PU.setVisibility(View.VISIBLE);
        btnInsertar_PU.setVisibility(View.INVISIBLE);
        lViewClientes_PU.setVisibility(View.VISIBLE);
    }

    private void listarClientes(){

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

        lViewClientes_PU.setAdapter(adaptador);
    }

    private class paisesApi extends AsyncTask<Void, Void, Void> {
        //String UrlTxt = "https://jsonip.com/";
        String UrlTxt = "https://restcountries.eu/rest/v2/region/Americas";
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
                //JSONObject clienteJSON = new JSONObject(new String(elTextoFinal));

                //Mostramos un valor del JSON
                //UtilesUI.MensajeToast(getApplicationContext(),"La ip es: "+ clienteJSON.getString("ip"));
                //Toast.makeText(getApplicationContext(), "La ip es: "+ clienteJSON.getString("name"), Toast.LENGTH_LONG).show();

                //-----------------------------------------------------------------
                //En el caso de que sean muchos datos

                JSONArray elJSONArray = new JSONArray(new String(elTextoFinal));

                for(int i=0; i<elJSONArray.length() ;i++) {
                    JSONObject elJSON = elJSONArray.getJSONObject(i);
                    paises.add(elJSON.getString("name"));
                    //UtilesUI.MensajeToast(getApplicationContext(), "La ip es: " + elJSON.getString("ip"));
                }//End For

                //Toast.makeText(getApplicationContext(), "La ip es: "+ elJSON.getString("name"), Toast.LENGTH_LONG).show();
                //-----------------------------------------------------------------

            } catch (JSONException e) {
                //UtilesUI.MensajeToast(getApplicationContext(), "Error al mostrar los datos!");
                e.printStackTrace();
                Log.d("==>>Error: ", e.toString());
            }
            super.onPostExecute(result);

        }//Fin onPostExecute
    }
}
