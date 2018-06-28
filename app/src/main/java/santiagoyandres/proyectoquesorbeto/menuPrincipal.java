package santiagoyandres.proyectoquesorbeto;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

public class menuPrincipal extends AppCompatActivity {

    TextView txt01;
    TextView txt02;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_principal);

        txt01 = (TextView) findViewById(R.id.txt01);
        txt02 = (TextView) findViewById(R.id.txt02);

        Bundle extras = getIntent().getExtras();

        if (extras != null){
            txt01.setText(extras.getString("user"));
            txt02.setText(extras.getString("password"));
        }else{
            txt01.setText("Error");
            txt02.setText("Error");
        }

    }
}
