package com.example.asistente_compras;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Layout;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import static android.content.DialogInterface.*;

public class SeleccionarProducto extends AppCompatActivity {

    private final AdminSOLite admin = new AdminSOLite(this);
    private Spinner spinner1;
    private ListView lv1;
    private ArrayList<String> listas;
    private String indicador;
    ArrayList<String> Productos = null;
    private TextView titulo;
    private static String titulop;
    int cod_lis;
    int id_usu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seleccionar_producto);

        //colocar el titulo enviado del mensajeEmergente

        titulo = (TextView) findViewById(R.id.tit_lista);
        String tit = getIntent().getStringExtra("nombre");
        if(tit!=null) {
            titulop = tit;
            titulo.setText(titulop);
        }

        id_usu=admin.buscar_estado_activo();
        cod_lis=admin.buscar_id_lis(titulop,id_usu);

        //coloca el id de la lista enviado por VistaPrevia
        if(cod_lis==0){
            cod_lis = getIntent().getIntExtra("cod_lista",0);
        }

        //conexion con spinner
        spinner1 = (Spinner) findViewById(R.id.Spinner);
        lv1 = (ListView) findViewById(R.id.lista);
        ArrayAdapter adapter = ArrayAdapter.createFromResource(this, R.array.spinner, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner1.setAdapter(adapter);

        spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String seleccion = (String) spinner1.getItemAtPosition(position);
                indicador = admin.buscar("Id_cat", "Categorias", "des_cat", seleccion.toUpperCase());
                if (indicador != null) {

                    listas = admin.buscar_enlista("nom_pro", "Productos", "cat_pro", indicador);
                    ArrayAdapter adaptador = new ArrayAdapter(SeleccionarProducto.this, android.R.layout.simple_list_item_multiple_choice, listas);//*****
                    lv1.setAdapter(adaptador);

                    // poner check a los productos anteriormente seleccionados

                    ArrayList listas2 = admin.buscar_enlista("cod_pro", "Productos", "cat_pro", indicador);
                    Productos= admin.buscar_enlista("pro_det","Detalle_Listas","id_lis_per",String.valueOf(cod_lis));
                    ArrayList posi= obtener_posicion(Productos,listas2);
                    if(posi!=null) {
                        for (int t = 0; t < posi.size(); t++) {
                            lv1.setItemChecked((Integer) posi.get(t), true);
                        }
                    }

                    lv1.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
                    lv1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                            String items = (String) lv1.getItemAtPosition(position);
                            String cod_pro = admin.buscar("cod_pro", "Productos", "nom_pro", items);

                            if (lv1.isClickable()) {
                                //Guarda Detalles
                                if (!Productos.contains(cod_pro)) {
                                    Productos.add(cod_pro);
                                    admin.agregarDetalle(cod_lis, Integer.valueOf(cod_pro), "N");
                                }else{
                                       //elimina detalles
                                    admin.borrar_x2parametros("Detalle_Listas", "id_lis_per", cod_lis, "pro_det", Integer.valueOf(cod_pro));
                                    Productos.remove(cod_pro);
                                }



                            }
                        }

                    });
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

    }

    //menu items
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.vista_guardar_salir_, menu);
        return true;

    }

    //asignar funciones a los items del menu vista/guardar/salir
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.vista) {

                Intent i = new Intent(SeleccionarProducto.this, VistaPrevia.class);
                i.putExtra("tit", titulo.getText().toString());
                i.putExtra("id_lista", cod_lis);
                startActivity(i);

        } else  if (id == R.id.item_salir) {

                //Regresar a Recordatorio de compras

            Intent siguiente = new Intent(SeleccionarProducto.this, RecordatorioCompras.class);
            startActivity(siguiente);

            }
        return super.onOptionsItemSelected(item);
    }

    //obtiene la posicion en la que se encuentran los productos en la lista

    public ArrayList obtener_posicion(ArrayList productos, ArrayList pro_xcategorias){
        ArrayList<Integer>posi= new ArrayList<>();
        if(!productos.isEmpty()){
            for (int i=0;i<productos.size();i++){
                for (int t=0;t<pro_xcategorias.size();t++){
                    if( productos.get(i).toString().equals(pro_xcategorias.get(t).toString())){
                        posi.add(t);
                    }
                }
            }
            return posi;
        }
        return null;
    }

    //añade productos que no se encuentran en las listas
    public void aniadir_pro(View view){
        Intent siguiente = new Intent(this, AniadirProducto.class);
        startActivity(siguiente);

    }

}







