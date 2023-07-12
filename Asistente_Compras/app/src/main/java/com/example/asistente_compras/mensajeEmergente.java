package com.example.asistente_compras;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class mensajeEmergente extends AppCompatActivity {
    private AdminSOLite admin = new AdminSOLite(this);

//Da un nombre y guarda la nueva lista

    private TextView nombre_list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mensaje_emergente);

        DisplayMetrics ventana = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(ventana);
        int ancho = ventana.widthPixels;
        int alto = ventana.heightPixels;
        getWindow().setLayout((int) (ancho * 0.85), (int) (alto * 0.5));

        nombre_list = (EditText) findViewById(R.id.nameLista);
    }

    public void cancelar(View view) {
        finish();

    }

    public void crearLista(View view) {
        int cod_usu = admin.buscar_estado_activo();
        int buscar = admin.buscar_id_lis(nombre_list.getText().toString(), cod_usu);                //comprueba que no exista una lisat con el mismo nombre
        if (buscar == 0) {

            admin.agregarListas(nombre_list.getText().toString(), "Pendiente", 0, cod_usu);

            Toast.makeText(this, "Lista Creada", Toast.LENGTH_SHORT).show();
            Intent i = new Intent(this, SeleccionarProducto.class);
            i.putExtra("nombre", nombre_list.getText().toString());                           //envia nomobre de lista a la siguiente Activity
            startActivity(i);

        } else {
            Toast.makeText(this, "Ya existe una tabla con ese nombre", Toast.LENGTH_SHORT).show();
        }
    }
}