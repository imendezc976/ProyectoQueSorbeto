package santiagoyandres.proyectoquesorbeto;

import android.content.Context;
import android.content.SharedPreferences;

public class Util_Preferences {

    Context myContext;
    private SharedPreferences configPreferences;
    private SharedPreferences.Editor confPrefEditor;
    String sharedPrefName = "ConfigPref";

    public Util_Preferences(Context myContext) {
        this.myContext = myContext;
        configPreferences = myContext.getSharedPreferences(sharedPrefName, myContext.MODE_PRIVATE);
        confPrefEditor = configPreferences.edit();
    }

    public void saveUserAdmin(String username, String password){
        confPrefEditor.putString("adminUser", username);
        confPrefEditor.putString("adminPass", password);
        confPrefEditor.commit();
    }

    public String[] getUserAdminPreferences(){
        String[] result = {"", ""};

        SharedPreferences pref = myContext.getSharedPreferences(sharedPrefName, myContext.MODE_PRIVATE);
        result[0] = pref.getString("adminUser", "No set");
        result[1] = pref.getString("adminPass","No set");

        return result;
    }
}
