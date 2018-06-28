package santiagoyandres.proyectoquesorbeto;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class PantallaLogin extends AppCompatActivity {

    EditText eTxt_Email_PL;
    EditText eTxt_Password_PL;
    Button   btn_Login_PL;

    Util_Preferences UtilPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pantalla_login);

        UtilPref = new Util_Preferences(getApplicationContext());
        UtilPref.saveUserAdmin("123", "456");

        eTxt_Email_PL = (EditText) findViewById(R.id.eTxt_Email_PL);
        eTxt_Password_PL = (EditText) findViewById(R.id.eTxt_Password_PL);
        btn_Login_PL = (Button) findViewById(R.id.btn_Login_PL);

        btn_Login_PL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String result[] = UtilPref.getUserAdminPreferences();
                if(eTxt_Email_PL.getText().toString().equals(result[0]) && eTxt_Password_PL.getText().toString().equals(result[1])){
                    Toast.makeText(getApplicationContext(), "Login", Toast.LENGTH_LONG).show();
                } else{
                    Toast.makeText(getApplicationContext(), "Invalid login.", Toast.LENGTH_LONG).show();
                }

            }
        });
    }
}
