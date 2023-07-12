package com.example.asistente_compras;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;

public class VistaPrevia extends AppCompatActivity {
    private AdminSOLite admin = new AdminSOLite(this);
    ArrayList<String> productos=new ArrayList<>();
    ArrayList<String> nom_pro=new ArrayList<>();
    private ListView lv1;
    private TextView titulo;
    String pro;
    int id_lista;
    ArrayAdapter adaptador;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vista_previa);


        lv1=(ListView)findViewById(R.id.lista_vista);
        titulo=(TextView)findViewById(R.id.tituloVista);
        //Obtiene id_Lista de SeleccionarProducto
        id_lista= getIntent().getIntExtra("id_lista",0);
        //Obtiene id_lista enviado por ListaCompras
        if(id_lista==0){
            id_lista= getIntent().getIntExtra("lista_codigo",0);
        }

        //se obtiene el codigo de los productos de la lista, listandolos con el nombre.

        productos = admin.buscar_enlista("pro_det", "Detalle_Listas", "id_lis_per", String.valueOf(id_lista));
        if (!productos.isEmpty()) {
                for (int i = 0; i < productos.size(); i++) {
                    pro = admin.buscar_xid_producto(productos.get(i));
                    nom_pro.add(String.valueOf(pro));
                }
            }

        adaptador = new ArrayAdapter(this, android.R.layout.simple_list_item_1, nom_pro);
        lv1.setAdapter(adaptador);

        //click largo

        lv1.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        lv1.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                //elimina un producto no deseado de la Lista y BD

                String seleccion= (String) lv1.getItemAtPosition(position);
                String cod_pro= admin.buscar("cod_pro","Productos","nom_pro",seleccion);
                AlertDialog.Builder alerta=new AlertDialog.Builder(VistaPrevia.this);
                alerta.setTitle("¿Desea eliminar" + seleccion+" de la lista?");
                alerta.setCancelable(true);
                alerta.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        admin.borrar_x2parametros("Detalle_Listas","id_lis_per",id_lista, "pro_det",Integer.valueOf(cod_pro));
                        nom_pro.remove(seleccion);
                        lv1.setAdapter(adaptador);
                        Toast.makeText(getApplicationContext(),  seleccion+ " eliminado", Toast.LENGTH_LONG).show();
                    }
                });
                alerta.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                               finish();
                           }
                });
                AlertDialog showDialogo= alerta.create();
                showDialogo.show();
                return true;
            }
        });
    }

// menu
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;

    }

  //asignar funciones a los items del menu

    public boolean onOptionsItemSelected(MenuItem item){
        int id=item.getItemId();

        //regresa a seleccionar productos

        if(id==R.id.item_add){
            Intent siguiente = new Intent(this, SeleccionarProducto.class);
            siguiente.putExtra("cod_lista",id_lista);
            startActivity(siguiente);

           // regresar a RecordatorioCompras

        } else if(id==R.id.item_close){
            Intent siguiente = new Intent(VistaPrevia.this, RecordatorioCompras.class);
            startActivity(siguiente);

            // realizar la compra

        }else if(id==R.id.item_list_compra){
            AlertDialog.Builder confirmacion= new AlertDialog.Builder(VistaPrevia.this);
            confirmacion.setTitle("¿Ir a Lista de Compras?");
            confirmacion.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    finish();
                }
            });

            confirmacion.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent siguiente = new Intent(VistaPrevia.this, ListaCompras.class);
                    siguiente.putExtra("cod_lista",id_lista);
                    startActivity(siguiente);
                }
            });
            AlertDialog showDialogo = confirmacion.create();
            showDialogo.show();
        }
        return super.onOptionsItemSelected(item);
    }

}