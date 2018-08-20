package santiagoyandres.proyectoquesorbeto;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;

public class PantallaFacturacion extends AppCompatActivity {

    EditText eTextNumero_PF;
    EditText eTextCantidad_PF;
    EditText eTextFecha_PF;
    Spinner spinCliente_PF;
    Spinner spinProducto_PF;

    SQLite_Class baseDeDatos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pantalla_facturacion);

        baseDeDatos = new SQLite_Class(getApplicationContext());

        inicializaComponentes();
        activaCampos(false);

    }

    private void inicializaComponentes() {
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











}
