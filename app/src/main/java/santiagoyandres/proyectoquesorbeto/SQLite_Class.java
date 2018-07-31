package santiagoyandres.proyectoquesorbeto;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class SQLite_Class extends SQLiteOpenHelper {

    private static final int DB_VERSION = 1;
    private static final String DB_NAME = "QueSorbeto.db";

    // Tabla cliente
    private static final String Cliente_ID = "id";
    private static final String Cliente_Nombre = "nombre";
    private static final String Cliente_Telefono = "telefono";
    private static final String Cliente_Nacionalidad = "nacionalidad";

    // Tabla Producto
    private static final String Producto_ID_Prod = "id";
    private static final String Producto_Nom_Prod = "nombre";
    private static final String Producto_Precio = "precio";

    // Tabla Factura
    private static final String Factura_Numero = "numero";
    private static final String Factura_cliente = "cliente";
    private static final String Factura_producto = "producto";
    private static final String Factura_cantidad = "cantidad";
    private static final String Factura_fecha = "fecha";

    private static final String CREA_TABLA_PERSONA =
            "CREATE TABLE IF NOT EXISTS Clientes (" + Cliente_ID  + " INTEGER PRIMARY KEY AUTOINCREMENT ," +
                    Cliente_Nombre + " TEXT, " +  Cliente_Nacionalidad + " TEXT, " + Cliente_Telefono + " TEXT )";

    private static final String CREA_TABLA_PRODUCTO =
            "CREATE TABLE IF NOT EXISTS Producto (" + Producto_ID_Prod  + " INTEGER PRIMARY KEY AUTOINCREMENT ," +
                    Producto_Nom_Prod + " TEXT, " + Producto_Precio + " FLOAT )";

    private static final String CREA_TABLA_FACTURA =
            "CREATE TABLE IF NOT EXISTS Factura (" + Factura_Numero  + " INTEGER PRIMARY KEY AUTOINCREMENT ," +
                    Factura_cliente + " TEXT, " + Factura_producto + " TEXT, " + Factura_cantidad + " TEXT, "
                    + Factura_fecha + " TEXT )";

    public SQLite_Class(Context context, String name,
                        SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DB_NAME, factory, DB_VERSION);
    }//Fin Constructor  =======================

    public SQLite_Class(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }//Fin Constructor  =======================

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREA_TABLA_PERSONA);
        db.execSQL(CREA_TABLA_PRODUCTO);
        db.execSQL(CREA_TABLA_FACTURA);
    }//Fin onCreate  =======================

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Borra las tablas si existen y todos los datos se borran.
        db.execSQL("DROP TABLE IF EXISTS Clientes");
        db.execSQL("DROP TABLE IF EXISTS Producto");
        db.execSQL("DROP TABLE IF EXISTS Factura");
        onCreate(db);// Crea la tabla otra vez
    }//Fin onUpgrade =======================

    // Funciones para el cliente
    public int InsertaCliente(objCliente persona) {
        ContentValues values = new ContentValues();
        values.put(Cliente_Nombre, persona.nombre);
        values.put(Cliente_Telefono, persona.telefono);
        values.put(Cliente_Nacionalidad, persona.nacionalidad);

        SQLiteDatabase db = this.getWritableDatabase();
        long cliente_Id = db.insert("Clientes", null, values);
        db.close();// Cierra la conexión con la BD
        return (int) cliente_Id;
    }// Fin InsertaCliente =======================

    public void BorraCliente(int cliente_Id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("Clientes", Cliente_ID + "= ?", new String[] { String.valueOf(cliente_Id) });
        db.close();// Cierra la conexión con la BD
    }// Fin BorraPersona =======================

    public void BorraTodosLosCliente(){
        for (int i =1; i < CuentaClientes(); i++ ) {
            BorraCliente(i);
        }//Fin For
    }//Fin BorraTodoCliente =======================

    public int CuentaClientes(){
        int CantElementos = 0;
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery =  "SELECT Count( " + Cliente_ID + ") as CantClientes FROM Clientes";
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                CantElementos = cursor.getInt(cursor.getColumnIndex("CantClientes"));
            } while (cursor.moveToNext());
        }//if
        cursor.close();
        db.close();
        return CantElementos;
    }//Fin CuentaClientes =======================

    public ArrayList <String> ConsultaClientes() {
        String Nombre = "";
        String Telefono = "";
        ArrayList<String> ListaPersonas = new ArrayList<String>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM Clientes", null);

        while(cursor.moveToNext())
        {
            Telefono = cursor.getString(cursor.getColumnIndex(Cliente_Telefono));
            Nombre = cursor.getString(cursor.getColumnIndex(Cliente_Nombre));
            ListaPersonas.add(Telefono + ", " + Nombre);
            Nombre = "";
            Telefono = "";
        }//Fin while

        cursor.close();
        db.close();
        return ListaPersonas;
    } //Fin ConsultaClientes =======================

    public objCliente BuscaPersona_x_ID(int Id){
        int iCount =0;
        objCliente persona = new objCliente();
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery =  "SELECT  " + Cliente_ID + "," +
                Cliente_Nombre + "," + Cliente_Telefono +
                " FROM Clientes WHERE " + Cliente_ID + "=?";

        Cursor cursor = db.rawQuery(selectQuery, new String[] { String.valueOf(Id) } );

        if (cursor.moveToFirst()) {
            do {
                //persona.clienteId =cursor.getInt(cursor.getColumnIndex(COL_ID));
                persona.nombre =cursor.getString(cursor.getColumnIndex(Cliente_Nombre));
                persona.telefono =cursor.getString(cursor.getColumnIndex(Cliente_Telefono));
                persona.nacionalidad = cursor.getString(cursor.getColumnIndex(Cliente_Nacionalidad));

            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return persona;
    }// Fin BuscaPersona_x_ID =======================

    public int InsertaProducto(objProducto Producto) {
        ContentValues values = new ContentValues();
        values.put(Producto_Nom_Prod, Producto.nombre);
        values.put(Producto_Precio, Producto.precio);

        SQLiteDatabase db = this.getWritableDatabase();
        long Producto_Id = db.insert("Producto", null, values);
        db.close();// Cierra la conexión con la BD
        return (int) Producto_Id;
    }// Fin InsertaCliente =======================

    public ArrayList <String> ConsultaProductos() {
        String Nombre = "";
        String Precio = "";
        ArrayList<String> ListaProductos = new ArrayList<String>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM Producto", null);

        while(cursor.moveToNext())
        {
            Precio = cursor.getString(cursor.getColumnIndex(Producto_Precio));
            Nombre = cursor.getString(cursor.getColumnIndex(Producto_Nom_Prod));
            ListaProductos.add(Nombre +", "+Precio);
            Nombre = "";
            Precio = "";
        }//Fin while

        cursor.close();
        db.close();
        return ListaProductos;
    } //Fin ConsultaClientes =======================


    public void EliminaTablas(String laTabla){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DROP TABLE IF EXISTS " + laTabla);
        onCreate(db);// Crea la tabla otra vez
    }//Fin BorraTodoCliente =======================









}// Fin LibSQLIte
