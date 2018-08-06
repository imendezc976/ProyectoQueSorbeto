package santiagoyandres.proyectoquesorbeto;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.text.method.PasswordTransformationMethod;
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
                    //Toast.makeText(getApplicationContext(), "Login", Toast.LENGTH_LONG).show();
                    Intent menuPrincipal = new Intent(getApplicationContext(), menuPrincipal.class);
                    menuPrincipal.putExtra("user", eTxt_Email_PL.getText().toString());
                    menuPrincipal.putExtra("password", eTxt_Password_PL.getText().toString());
                    startActivity(menuPrincipal);
                } else{
                    Toast.makeText(getApplicationContext(), "Invalid login.", Toast.LENGTH_LONG).show();
                }

            }
        });

        eTxt_Email_PL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                eTxt_Email_PL.setText("");
            }
        });

        eTxt_Password_PL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                eTxt_Password_PL.setText("");
                eTxt_Password_PL.setTransformationMethod(PasswordTransformationMethod.getInstance());
            }
        });
    }
}
