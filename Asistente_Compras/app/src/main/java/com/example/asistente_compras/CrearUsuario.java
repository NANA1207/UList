package com.example.asistente_compras;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

public class CrearUsuario extends AppCompatActivity {

    private EditText te_usuario, te_contrasena, te_repetir, te_pre1, te_pre2;
    AdminSOLite admin = new AdminSOLite(this);
    String opcion="";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_usuario);

        te_usuario = (EditText)findViewById(R.id.txt_nom_usu);
        te_contrasena = (EditText)findViewById(R.id.txt_contra_usu);
        te_repetir= (EditText)findViewById(R.id.txt_contra_usu2);
        te_pre1= (EditText)findViewById(R.id.txt_resp1);
        te_pre2=(EditText)findViewById(R.id.txt_resp2);

    }



    //metodo para crear el usuario
    public void registrarUsuario(View view) {

        String usuario = te_usuario.getText().toString();
        String contrasena = te_contrasena.getText().toString();
        String contrasena2 = te_repetir.getText().toString();
        String pregunta1 = te_pre1.getText().toString();
        String pregunta2 = te_pre2.getText().toString();

            if (!usuario.isEmpty() && !contrasena.isEmpty() && !pregunta1.isEmpty() && !pregunta2.isEmpty()) {

                //comprobar si existe el usuario

                String vusuario=admin.buscar("con_usu", "Usuarios","nom_usu",usuario);
                if (vusuario== null) {

                    //comprueba que las dos conttaseñas sean iguales

                if (contrasena.equals(contrasena2)) {
                    if (!opcion.equals("")) {

                        //busca al usuario activo y cambia su estado a desactivo

                        AdminSOLite admin = new AdminSOLite(this);
                        String u= admin.buscar("id_usu","Usuarios","estado","A");
                        if(u!=null){
                            admin.modificar_esatado_usu("D",1);
                        }

                        //Ingresa al usuario

                        admin.agregarUsuarios(usuario,contrasena,"A",pregunta1,pregunta2,opcion);

                        te_usuario.setText("");
                        te_contrasena.setText("");
                        te_repetir.setText("");
                        te_pre1.setText("");
                        te_pre2.setText("");
                        opcion ="";
                        Toast.makeText(this, "Registro Exitoso", Toast.LENGTH_SHORT).show();
                        Intent siguiente = new Intent(this, RecordatorioCompras.class);
                        startActivity(siguiente);
                    } else {
                        Toast.makeText(this, "Debes escoger una figura", Toast.LENGTH_SHORT).show();
                    }

                } else {

                    Toast.makeText(this, "La contraseña no es igual", Toast.LENGTH_SHORT).show();
                    te_contrasena.setText("");
                    te_repetir.setText("");

                }
                } else {

                    Toast.makeText(this, "Nombre de Usuario ya existe", Toast.LENGTH_SHORT).show();
                    te_usuario.setText("");
                    te_contrasena.setText("");
                    te_repetir.setText("");

                }
            } else {

                Toast.makeText(this, "Debes llenar todos los campos", Toast.LENGTH_SHORT).show();

            }

    }



    //registra la figura seleccionada

    public void figuraSeleccionada(View view){

        switch (view.getId()){
                case R.id.btn_cuadrado:
                Toast.makeText(this, "Cuadrado", Toast.LENGTH_SHORT).show();
                opcion= "cuadrado";
                break;

                case R.id.btn_estrella:
                Toast.makeText(this, "Estrella", Toast.LENGTH_SHORT).show();
                opcion= "estrella";
                break;

                case R.id.btn_triangulo:
                Toast.makeText(this, "Triangulo", Toast.LENGTH_SHORT).show();
                opcion= "triangulo";
                break;

        }
    }
}