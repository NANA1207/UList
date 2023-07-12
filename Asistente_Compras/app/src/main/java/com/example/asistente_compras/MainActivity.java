package com.example.asistente_compras;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private EditText Txt_Usuario;
    private EditText Txt_Contrasena;
    AdminSOLite bd = new AdminSOLite(this);
    private  String nombre_usu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Txt_Usuario = (EditText)findViewById(R.id.txt_Usuario);
        Txt_Contrasena = (EditText)findViewById(R.id.txt_Contraseña);
        nombre_usu=Txt_Usuario.getText().toString();
        //comprobar y cargar datos a la base de datos
        String validador=bd.buscar("des_cat","Categorias","Id_cat","CE01");
        if(validador==null) {
            bd.categorias();
            bd.productos();
        }
        //busca si un usuario está activo

        int es=0;
        es=bd.buscar_estado_activo();
        if(es!=0){
            Intent siguiente = new Intent(this, RecordatorioCompras.class);
            startActivity(siguiente);
        }
    }

    //Boton crear
    public void crearUsuario(View view){
        Intent siguiente = new Intent(this, CrearUsuario.class);
        startActivity(siguiente);
    }


    //
    public void ingresar(View view){

        String usuario = Txt_Usuario.getText().toString();
        String contrasena=Txt_Contrasena.getText().toString();
        String vcontrasena=bd.buscar("con_usu", "Usuarios","nom_usu",usuario);
        if (!usuario.isEmpty() && !contrasena.isEmpty()){
            if(vcontrasena != null){
                if(contrasena.equals(vcontrasena)){    //valida que la contraseña sea correcta

                    //pasar a al RecordatorioCompras

                    int u= bd.buscar_estado_activo();
                    if(u==0){
                        String n= bd.buscar("id_usu","Usuarios","nom_usu",usuario);
                        bd.modificar_esatado_usu("A",Integer.valueOf(n));
                    }
                    Intent siguiente = new Intent(this, RecordatorioCompras.class);
                    startActivity(siguiente);
                    Txt_Contrasena.setText("");
                }else{
                    Toast.makeText(this, "Contraseña incorrecta", Toast.LENGTH_SHORT).show();
                    Txt_Contrasena.setText("");
                }

            }else{
                Toast.makeText(this, "El usuario no existe", Toast.LENGTH_SHORT).show();
                Txt_Usuario.setText("");
                Txt_Contrasena.setText("");
            }
        } else{
            Toast.makeText(this, "Debe llenarse todos los campos", Toast.LENGTH_SHORT).show();
        }
    }
    public void cambiarContrasena(View view){
        Intent siguiente = new Intent(this, CambiarContrasena.class);
        startActivity(siguiente);
    }
}