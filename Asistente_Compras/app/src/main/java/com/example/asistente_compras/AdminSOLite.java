package com.example.asistente_compras;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import androidx.annotation.Nullable;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class AdminSOLite extends SQLiteOpenHelper {
    private static final String NOMBRE_BD= "AsistenteCompras";
    private static final int VERSION_BD= 1;

    private static final String TABLA_USUARIOS= "Create table Usuarios(id_usu INTEGER primary key autoincrement, nom_usu text , con_usu text,  estado text, res1 text, res2 text, res3 text)";
    //(Esatado de un usuario)estado= "A" (Activo) o "D" (Desactivo)

    private static final String TABLA_CATEGORIAS= "Create table Categorias(Id_cat text primary key, des_cat text)";


    private static final String TABLA_PRODUCTOS= "Create table Productos" +
            "(cod_pro INTEGER primary key autoincrement, nom_pro text, mar_pro text, pre_uni_pro float, cat_pro text REFERENCES Categorias(Id_cat) )";


    private static final String TABLA_LISTAS= "Create table Listas " +
            "(id_lis INTEGER primary key autoincrement, nom_lis text , est_lis text, tot_lis float, id_usu_per INTEGER REFERENCES Usuarios(id_usu) )";

    //est_lis= "Pendiente" o "Guardado"


    private static final String TABLA_DETALLE_LISTAS= "Create table Detalle_Listas" +
            "(id_lis_per INTEGER REFERENCES Listas(id_lis),pro_det INTEGER REFERENCES Productos(cod_pro), com_nocom text)";

    //(Estado del detalle) com_nocom= "C"(comprado) o "N"(no comprado)

    public AdminSOLite(@Nullable Context context) {
        super(context, NOMBRE_BD, null, VERSION_BD);


    }


    @Override
    public void onCreate(SQLiteDatabase Base_Datos) {
             Base_Datos.execSQL(TABLA_USUARIOS);
             Base_Datos.execSQL(TABLA_CATEGORIAS);
             Base_Datos.execSQL(TABLA_PRODUCTOS);
        Base_Datos.execSQL(TABLA_LISTAS);
        Base_Datos.execSQL(TABLA_DETALLE_LISTAS);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
       db.execSQL("DROP TABLE IF EXISTS Usuarios");
       db.execSQL(TABLA_USUARIOS);
        db.execSQL("DROP TABLE IF EXISTS Categorias");
        db.execSQL(TABLA_CATEGORIAS);
        db.execSQL("DROP TABLE IF EXISTS Productos");
        db.execSQL(TABLA_PRODUCTOS);
        db.execSQL("DROP TABLE IF EXISTS Listas");
        db.execSQL(TABLA_LISTAS);
        db.execSQL("DROP TABLE IF EXISTS Detalle_Listas");
        db.execSQL(TABLA_DETALLE_LISTAS);
    }

    //insertar categorias predeterminadas
    public void categorias() {
        SQLiteDatabase Base_Datos = getWritableDatabase();
        Base_Datos.execSQL("INSERT INTO Categorias VALUES('CE01','CARNES Y EMBUTIDOS')");
        Base_Datos.execSQL("INSERT INTO Categorias VALUES('VL01','VERDURAS Y LEGUMBRES')");
        Base_Datos.execSQL("INSERT INTO Categorias VALUES('FT01','FRUTAS Y TUBERCULOS')");
        Base_Datos.execSQL("INSERT INTO Categorias VALUES('CD01','CEREALES Y DERIVADOS')");
        Base_Datos.execSQL("INSERT INTO Categorias VALUES('LA01','LACTEOS Y DERIVADOS')");
        Base_Datos.execSQL("INSERT INTO Categorias VALUES('SN01','SNACKS')");
        Base_Datos.execSQL("INSERT INTO Categorias VALUES('BE01','BEBIDAS')");
        Base_Datos.execSQL("INSERT INTO Categorias VALUES('OT01','OTROS')");
        Base_Datos.close();
    }

    //insertar productos predeterminados

    public void productos(){

        //CARNES Y EMBUTIDOS

        agregarProductos("CARNE DE RES",null,(float)2.50,"CE01");
        agregarProductos("CARNE DE POLLO",null,(float)1.50,"CE01");
        agregarProductos("MORTADELA","PLUMROSE",(float)0.65,"CE01");
        agregarProductos("MORTADELA FAMILIAR","PLUMROSE",(float)1.00,"CE01");
        agregarProductos("SALCHICHAS","PLUMROSE",(float)0.6,"CE01");

        //VERDURAS Y LEGUMBRES

        agregarProductos("ARBEJA",null,(float)1.00,"VL01");
        agregarProductos("HABAS",null,(float)1.00,"VL01");
        agregarProductos("LECHUGA",null,(float)0.4,"VL01");
        agregarProductos("ESPINACA",null,(float)1.00,"VL01");
        agregarProductos("COL",null,(float)1.00,"VL01");

        //FRUTAS Y TUBERCULOS

        agregarProductos("FRESA",null,(float)1.00,"FT01");
        agregarProductos("NARANJA",null,(float)0.5,"FT01");
        agregarProductos("PAPAS",null,(float)0.8,"FT01");
        agregarProductos("ZANAHORIA",null,(float)0.5,"FT01");
        agregarProductos("MANZANA",null,(float)1.00,"FT01");

        //CEREALES Y DERIBADOS

        agregarProductos("ARROZ",null,(float)0.5,"CD01");
        agregarProductos("MAIZ",null,(float)0.4,"CD01");
        agregarProductos("CONFLEX","ZUCARITAS",(float)3.50,"CD01");
        agregarProductos("GALLETAS AMOR MED.","NESTLE",(float)1.00,"CD01");
        agregarProductos("GALLETA OREO PAQ.","NABISCO",(float)2.60,"CD01");
        agregarProductos("PASTA","BARILLA",(float)2.50,"CD01");

        //SNACKS

        agregarProductos("DORITOS",null,(float)0.50,"SN01");
        agregarProductos("RUFFLES",null,(float)0.50,"SN01");
        agregarProductos("DORITOS",null,(float)0.50,"SN01");

        //BEBIDAS

        agregarProductos("COCA-COLA 3LT","COCA COLA",(float)3.00,"BE01");
        agregarProductos("SUNY",null,(float)0.7,"BE01");
        agregarProductos("VINO",null,(float)10.00,"BE01");
        agregarProductos("BEBIDA ENERGÉTICA 220V",null,(float)1.00,"BE01");

        //LACTEOS Y DERIVADOS

        agregarProductos("YOGURT PEQ.","TONY",(float)0.5,"LA01");
        agregarProductos("LECHE DESLACTOSADA","TONY",(float)1.00,"LA01");
        agregarProductos("QUESO","REY QUESO",(float)2.50,"LA01");
        agregarProductos("MANTEQUILLA","GIRASOL",(float)1.00,"LA01");

        //OTROS

        agregarProductos("CHOCOLATES","NESTLE",(float)1.00,"OT01");
        agregarProductos("CUVET. HUEVOS",null,(float)3.00,"OT01");
        agregarProductos("RICACAO","NESTLE",(float)1.00,"OT01");


    }
// agrgar Productos
     public void agregarProductos (String nom_pro, String mar_pro, float pre_uni_pro, String cat_pro){
         SQLiteDatabase bd = getWritableDatabase();

        if(bd!=null){
            if(bd!=null){
                ContentValues c= new ContentValues();
                c.put("nom_pro", nom_pro);
                c.put("mar_pro", mar_pro);
                c.put("pre_uni_pro", pre_uni_pro);
                c.put("cat_pro", cat_pro);
                bd.insert("Productos",null,c);
                //   bd.execSQL("INSERT INTO Detalle_Listas VALUES('"+id_list+"','"+cod_pro+"','"+can_pro+"')");
                bd.close();
             }
           bd.close();
        }
    }

 //**********************************Agregar****************************************
    // agrega usuarios

    public void agregarUsuarios(String usuario, String contrasena, String estado, String respuesta1, String respuesta2, String respuesta3){
        SQLiteDatabase bd = getWritableDatabase();

          if(bd!=null){
            ContentValues c= new ContentValues();
            c.put("nom_usu", usuario);
            c.put("con_usu", contrasena);
            c.put("estado", estado);
            c.put("res1", respuesta1);
            c.put("res2", respuesta2);
            c.put("res3", respuesta3);
            bd.insert("Usuarios",null,c);

            bd.close();
        }
    }


    //Agregar listas
    public void agregarListas(String nom_list, String est_list, float tot_list, int usuario){
        SQLiteDatabase bd = getWritableDatabase();

        if(bd!=null){
            ContentValues c= new ContentValues();
            c.put("nom_lis", nom_list);
            c.put("est_lis", est_list);
            c.put("tot_lis", tot_list);
            c.put("id_usu_per", usuario);
            bd.insert("Listas",null,c);
            // bd.execSQL("INSERT INTO Listas VALUES('"+nom_list+"','"+est_list+"','"+tot_list+"','"+usuario+"')");
            bd.close();
        }
    }

    //agregar detalle
    public void agregarDetalle(int id_list, int cod_pro, String estado){
        SQLiteDatabase bd = getWritableDatabase();

        if(bd!=null){
            ContentValues c= new ContentValues();
            c.put("id_lis_per", id_list);
            c.put("pro_det", cod_pro);
            c.put("com_nocom", estado);
            bd.insert("Detalle_Listas",null,c);
            //   bd.execSQL("INSERT INTO Detalle_Listas VALUES('"+id_list+"','"+cod_pro+"','"+can_pro+"')");
            bd.close();
        }
    }

    //***********************************busquedas******************************************
    //buscar

    public String buscar(String campo, String tabla, String llave, String atributo ){
        String dato;

        SQLiteDatabase BaseDeDatos = getReadableDatabase();
        Cursor fila = BaseDeDatos.rawQuery("Select "+campo+" from "+tabla+" where "+llave+" ='"+atributo+"'", null);
        if (fila.moveToFirst()) {
             dato = (String) fila.getString(0).toString();

            BaseDeDatos.close();
        } else {
            BaseDeDatos.close();
            return dato= null;
        }
        return dato;

}
    //busca lista de un atributo
    public ArrayList buscar_enlista (String campo, String tabla, String id, String atributo ){
        ArrayList  datos = new ArrayList();
        SQLiteDatabase BaseDeDatos = getReadableDatabase();
        Cursor fila = BaseDeDatos.rawQuery("Select "+campo+" from "+tabla +" where "+id+" ='" + atributo+"'", null);
        if (fila.moveToFirst()) {
            do {
                datos.add(fila.getString(0));
            } while (fila.moveToNext());
        }
        BaseDeDatos.close();
        return datos;
    }

    //Busca el nombre del producto por cod_pro

    public String buscar_xid_producto( String id_pro ){
        String dato;
        SQLiteDatabase BaseDeDatos = getReadableDatabase();
        Cursor fila = BaseDeDatos.rawQuery("Select * from Productos where cod_pro ="+id_pro, null);
        if (fila.moveToFirst()) {
            dato = (String) fila.getString(1).toString();

            BaseDeDatos.close();
        } else {
            BaseDeDatos.close();
            return dato= null;
        }
        return dato;

    }

    // busca una tista de datos sin una condición

    public ArrayList buscar_lista_sinCondicion (String campo, String tabla ){
        ArrayList  datos = new ArrayList();
        SQLiteDatabase BaseDeDatos = getReadableDatabase();
        Cursor fila = BaseDeDatos.rawQuery("Select "+campo+" from "+tabla , null);
        if (fila.moveToFirst()) {
            do {
                datos.add(fila.getString(0));
            } while (fila.moveToNext());
        }

        BaseDeDatos.close();
        return datos;

    }

     //listar dos datos de una tabla

    public ArrayList listar_2campos(int indicador1, int indicador2, String tabla, String id, int atributo ){
        ArrayList <String> datos = new ArrayList();
        SQLiteDatabase BaseDeDatos = getReadableDatabase();
        Cursor fila = BaseDeDatos.rawQuery("Select * from "+tabla +" where "+id+" ='" + atributo+"'", null);
        if (fila.moveToFirst()) {
            do {
                String uno=fila.getString(indicador1);
                String dos= fila.getString(indicador2);
                datos.add(String.format("%-11s%-1s", dos+": ", uno));
            } while (fila.moveToNext());
        }else{
            datos=null;
        }
        BaseDeDatos.close();
        return datos;
    }

   // *******************busquedas específicas*************************

    //busca el id de una lista

    public int buscar_id_lis(String nom_lis, int cod_usu ){
     int dato=0;
        SQLiteDatabase BaseDeDatos = getReadableDatabase();
        Cursor fila = BaseDeDatos.rawQuery("Select id_lis from Listas where nom_lis ='" +nom_lis+"'"+" and id_usu_per ='" + cod_usu+"'", null);
        if (fila.moveToFirst()) {
            dato = Integer.valueOf(fila.getString(0));

            BaseDeDatos.close();
        } else {
            BaseDeDatos.close();
            return dato= 0;
        }
        return dato;
    }

   //busca el usuario que se encuentra usando la aplicacion

    public int buscar_estado_activo (  ){
        ArrayList  datos = new ArrayList();
        int cod = 0;
        SQLiteDatabase BaseDeDatos = getReadableDatabase();
        Cursor fila = BaseDeDatos.rawQuery("Select estado from Usuarios"  , null);
        if (fila.moveToFirst()) {
            do {
                datos.add(fila.getString(0));
            } while (fila.moveToNext());
        }
        if(datos.contains("A")){
            cod = Integer.valueOf(buscar("id_usu", "Usuarios", "estado", "A"));

        }
        BaseDeDatos.close();
        return cod;

    }
    //***
public void ingresar_variosDetalles(ArrayList lista, int idList){
    for(int i=0; i<lista.size();i++ ){

        agregarDetalle(idList, (Integer) (lista.get(i)),"C");

    }
}

//*******************************modificar datos*****************************

   // modifica un dato de una tabla

    public void modificar(String tabla, String atributo, String dato, String atributo2, int dato2 ) {
        SQLiteDatabase bd = getWritableDatabase();
        if (bd != null) {
            bd.execSQL("Update "+tabla+" SET "+atributo+"='" +dato+ "' WHERE " +atributo2+ "=" +dato2);
            bd.close();
        }
    }

    // modifica el estado de un usuario de "A" a "D" o viceversa

     public void modificar_esatado_usu(String estado, int cod){
         SQLiteDatabase bd = getWritableDatabase();
         if(bd!=null){
             bd.execSQL("Update Usuarios SET estado='"+estado+"' WHERE id_usu="+cod);
             bd.close();
         }
    }

    // modifica el estado de un Detalle_Listas de "N" a "C" o viceversa

    public void modificar_estado_compra(String estado, int cod_list, int cod_pro){
        SQLiteDatabase bd = getWritableDatabase();
        if(bd!=null){
            bd.execSQL("Update Detalle_Listas SET com_nocom='"+estado+"' WHERE id_lis_per="+cod_list+" AND pro_det ="+cod_pro);
            bd.close();
        }
    }

    //****************************************Borrar*************************************

    // borra un dato de una tabla

    public void borrar(String tabla, String atributo, String dato){
        SQLiteDatabase BaseDeDatos = getWritableDatabase();
        if(BaseDeDatos!=null) {
            BaseDeDatos.execSQL("Delete FROM "+tabla+" where "+atributo+ "='"+dato+"'");
            BaseDeDatos.close();
        }
    }

    // borra un dato que cumpla con dos condiciones

    public void borrar_x2parametros(String tabla, String atributo1,int dato1, String atributo2, int dato2){
        SQLiteDatabase BaseDeDatos = getWritableDatabase();
        if(BaseDeDatos!=null) {

            BaseDeDatos.execSQL("DELETE FROM "+tabla+" WHERE "+atributo1+"="+dato1+" AND "+atributo2+"="+dato2);
            BaseDeDatos.close();
        }
    }


}
