package com.example.asistente_compras;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class CambiarContrasena extends AppCompatActivity {

    private EditText te_usuario, te_contrasena, te_repetir, te_pre1, te_pre2;
    AdminSOLite admin = new AdminSOLite(this);
    String opcion="";
    String id_usu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cambiar_contrasena);

        te_usuario = (EditText)findViewById(R.id.verificarUsuario);
        te_contrasena = (EditText)findViewById(R.id.newContrasena);
        te_repetir= (EditText)findViewById(R.id.repContrasenia);
        te_pre1= (EditText)findViewById(R.id.resp1);
        te_pre2=(EditText)findViewById(R.id.resp2);

    }

    public void cambiarContrasenia(View view) {

        String usuario = te_usuario.getText().toString();
        String contrasena = te_contrasena.getText().toString();
        String contrasena2 = te_repetir.getText().toString();
        String pregunta1 = te_pre1.getText().toString();
        String pregunta2 = te_pre2.getText().toString();

        if (!usuario.isEmpty() && !contrasena.isEmpty() && !pregunta1.isEmpty() && !pregunta2.isEmpty()) {

            //comprobar si existe el usuario

            id_usu=admin.buscar("id_usu", "Usuarios","nom_usu",usuario);
            if (id_usu!= null) {

                //comprueba que las dos contraseñas sean iguales

                if (contrasena.equals(contrasena2)) {
                    if (!opcion.equals("")) {

                        //busca al usuario activo y cambia su estado a desactivo

                        String u= admin.buscar("id_usu","Usuarios","estado","A");
                        if(u!=null){
                            admin.modificar_esatado_usu("D",Integer.valueOf(u));
                        }

                        //Ingresa al usuario
                       if(comprobar_campos(pregunta1,pregunta2,opcion)) {

                           //admin.agregarUsuarios(usuario,contrasena,"A",pregunta1,pregunta2,opcion);
                           admin.modificar_esatado_usu("A", Integer.valueOf(id_usu));
                           admin.modificar("Usuarios", "con_usu", contrasena, "id_usu", Integer.valueOf(id_usu));

                           te_usuario.setText("");
                           te_contrasena.setText("");
                           te_repetir.setText("");
                           te_pre1.setText("");
                           te_pre2.setText("");
                           opcion = "";
                           Toast.makeText(this, "Modificación exitosa", Toast.LENGTH_SHORT).show();
                           Intent siguiente = new Intent(this, RecordatorioCompras.class);
                           startActivity(siguiente);
                       }else{
                           Toast.makeText(this, "Respuestas no concuerdan", Toast.LENGTH_SHORT).show();
                       }
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

     //comprueba las respuestas con las guardadas en la BD
    public boolean comprobar_campos(String resp1, String resp2, String resp3){
        String pregunta1= te_pre1.getText().toString();
        String pregunta2= te_pre2.getText().toString();
        String com_pre1=admin.buscar("res1","Usuarios","id_usu",id_usu);
        String com_pre2=admin.buscar("res2","Usuarios","id_usu",id_usu);
        String com_pre3=admin.buscar("res3","Usuarios","id_usu",id_usu);
        if(pregunta1.equals(com_pre1)&&pregunta2.equals(com_pre2)&&resp3.equals(com_pre3)){
            return true;
        }
        return false;
    }

    //registra la figura seleccionada

    public void figuraSeleccionada(View view){

        switch (view.getId()){
            case R.id.im_cua:
                Toast.makeText(this, "Cuadrado", Toast.LENGTH_SHORT).show();
                opcion= "cuadrado";

                break;
            case R.id.ima_est:
                Toast.makeText(this, "Estrella", Toast.LENGTH_SHORT).show();
                opcion= "estrella";

                break;
            case R.id.ima_tri:
                Toast.makeText(this, "Triangulo", Toast.LENGTH_SHORT).show();
                opcion= "triangulo";
                break;

        }
    }
    public void cancelar(View view) {
        Intent siguiente = new Intent(this, MainActivity.class);
        startActivity(siguiente);

    }
}