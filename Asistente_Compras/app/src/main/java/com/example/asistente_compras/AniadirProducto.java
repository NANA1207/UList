package com.example.asistente_compras;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class AniadirProducto extends AppCompatActivity {
    private final AdminSOLite admin = new AdminSOLite(this);
    private EditText nombre;
    private EditText marca;
    private EditText precio;
    private Spinner spinner1;
    private String categoria_cod;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aniadir_producto);

        DisplayMetrics ventana = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(ventana);
        int ancho = ventana.widthPixels;
        int alto = ventana.heightPixels;
        getWindow().setLayout((int) (ancho * 0.9), (int) (alto * 0.7));

        nombre= (EditText) findViewById(R.id.nombre);
        marca = (EditText) findViewById(R.id.marca);
        precio = (EditText) findViewById(R.id.precio);
        spinner1 = (Spinner) findViewById(R.id.spinner);

        //Adaptador para Spinner de categorías

        ArrayAdapter adapter = ArrayAdapter.createFromResource(this, R.array.spinner, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner1.setAdapter(adapter);
        spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

               String categoria = (String) spinner1.getItemAtPosition(position);
                categoria_cod = admin.buscar("Id_cat", "Categorias", "des_cat", categoria.toUpperCase());

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    //botón guardar
    public void guardar_pro(View view) {

        String nombre1 = nombre.getText().toString().toUpperCase();
        String marca1 = marca.getText().toString();
        String precio1 = precio.getText().toString();
        ArrayList productos = admin.buscar_lista_sinCondicion("nom_pro", "Productos");

        if (!productos.contains(nombre1)) {
            if (!nombre1.isEmpty() && !precio1.isEmpty()) {
                if (marca1.isEmpty()) {

                    marca1 = null;

                }
                if (categoria_cod.isEmpty() || categoria_cod == null) {
                    Toast.makeText(this, "Seleccionar Categoría", Toast.LENGTH_SHORT).show();
                } else {

                    //se añade el producto

                    admin.agregarProductos(nombre1, marca1, Float.parseFloat(precio1), categoria_cod);
                    Toast.makeText(this, "Producto Añadido", Toast.LENGTH_SHORT).show();
                    finish();

                }
            } else {
                Toast.makeText(this, "Llenar campos obligatorios (*)", Toast.LENGTH_SHORT).show();
            }
        }else{
            Toast.makeText(this, "Nombre del Producto ya existe", Toast.LENGTH_SHORT).show();
        }
    }

    //Botón cancelar
    public void cancel(View view){
         finish();
        }


    }

