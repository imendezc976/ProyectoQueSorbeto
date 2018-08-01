package santiagoyandres.proyectoquesorbeto;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
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

public class PantallaUsuarios extends AppCompatActivity {

    ArrayList<String> paises = new ArrayList<>();
    Button boton;
    Spinner spinner123;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usuarios);

        miTarea hagale = new miTarea();
        hagale.execute();
        inicializaContenido();
    }

    private void inicializaContenido(){
        boton = (Button) findViewById(R.id.button);
        spinner123 = (Spinner) findViewById(R.id.spinner);

        boton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                miTarea hagale = new miTarea();
                hagale.execute();


            }
        });

        // Spinner click listener
        spinner123.setOnItemSelectedListener(new Spinner.OnItemSelectedListener(){
            public void onItemSelected(AdapterView<?>
                                               arg0,View arg1,int arg2,long arg3){
            }
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, paises);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner123.setAdapter(dataAdapter);
    }

    private class miTarea extends AsyncTask<Void, Void, Void> {
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
