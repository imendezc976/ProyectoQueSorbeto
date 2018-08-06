package santiagoyandres.proyectoquesorbeto;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class menuPrincipal extends AppCompatActivity {

    TextView tView_Username_MP;
    Button btn_Usuarios_MP;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_principal);

        tView_Username_MP = (TextView) findViewById(R.id.tView_Username_MP);
        btn_Usuarios_MP = (Button) findViewById(R.id.btn_Usuarios_MP);

        btn_Usuarios_MP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent pantallaUsuarios = new Intent(getApplicationContext(), PantallaUsuarios.class);
                startActivity(pantallaUsuarios);
            }
        });


        Bundle extras = getIntent().getExtras();

        if (extras != null){
            tView_Username_MP.setText(extras.getString("user"));
        }else{
            tView_Username_MP.setText("No se pudo obtener el nombre de usuario");
        }

    }
}
