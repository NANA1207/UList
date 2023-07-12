package com.example.asistente_compras;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class ListaCompras extends AppCompatActivity {
    AdminSOLite admin = new AdminSOLite(this);
    private ListView lv1;
    private ArrayList<String> productos=new ArrayList<>();
    ArrayList<String> nom_pro=new ArrayList<>();
    String pro;
    private int id_lista;
    private  ArrayAdapter adaptador;
    private ArrayList <String> seleccionados=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_compras);
        lv1=(ListView)findViewById(R.id.lista_compras);
        //asigna id enviado por RecordatorioCompras

        id_lista= getIntent().getIntExtra("id",0);

        //asigna el id enviado por VistaPrevia

        if(id_lista==0){
            id_lista= getIntent().getIntExtra("cod_lista",0);
        }

        //asigna los nombres de los productos dependiendo su codigo a los productos no comprados

        productos= admin.buscar_enlista("pro_det","Detalle_Listas","id_lis_per",String.valueOf(id_lista));
        ArrayList <String> estado= admin.buscar_enlista("com_nocom","Detalle_Listas","id_lis_per",String.valueOf(id_lista));
        if (!productos.isEmpty()) {                                           //***
            for (int i = 0; i < productos.size(); i++) {
                if(estado.get(i).equals("N")) {
                    pro = admin.buscar_xid_producto(productos.get(i));
                    nom_pro.add(String.valueOf(pro));
                }
            }
        }

        // crea lista de productos

        adaptador = new ArrayAdapter(this, android.R.layout.simple_list_item_multiple_choice, nom_pro);
        lv1.setAdapter(adaptador);

        // seleccionar items guardando sus codigos

        lv1.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        lv1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String items = (String) lv1.getItemAtPosition(position);
                String cod_pro = admin.buscar("cod_pro", "Productos", "nom_pro", items);

                if (lv1.isItemChecked(position)) {
                    if (!seleccionados.contains(cod_pro)) {
                        seleccionados.add(cod_pro);
                    }

                }
            }

        });

    }

    // regresar Recordatorio Compras
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.compras, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item){
        int id=item.getItemId();

        //validar compra de los productos

        if(id==R.id.item_compras){
            AlertDialog.Builder confirmacion= new AlertDialog.Builder(ListaCompras.this);
            confirmacion.setTitle("¿Comprar?");
            confirmacion.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    finish();
                }
            });

            confirmacion.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    //cambia el estado del detalle a "C" (Comprado)

                    if(seleccionados.isEmpty()) {
                        Toast.makeText(ListaCompras.this, "Seleccionar Productos", Toast.LENGTH_SHORT).show();
                    }else {

                        for(int t=0; t<seleccionados.size();t++) {
                            String busqueda= admin.buscar_xid_producto(seleccionados.get(t).toString());

                            if(nom_pro.contains(busqueda)){  //elimina los productos ya comprados del ListView
                                nom_pro.remove(busqueda);

                            }

                            admin.modificar_estado_compra("C",id_lista,Integer.valueOf(seleccionados.get(t).toString()));
                            lv1.setAdapter(adaptador);
                        }

                               if(nom_pro.isEmpty()){

                                   //Opcion de guardar o eliminar la Lista

                                   AlertDialog.Builder guardar= new AlertDialog.Builder(ListaCompras.this);
                                   guardar.setTitle(String.format("%-21s\n%-30s","Compra Finalizada :)","¿Desea Guardarla?"));

                                   //no guardar lista (Se elimina la Lista de la BD)
                                   guardar.setNegativeButton("NO", new DialogInterface.OnClickListener(){

                                       @Override
                                       public void onClick(DialogInterface dialog, int which) {
                                           admin.borrar("Listas","id_lis",String.valueOf(id_lista));
                                           admin.borrar("Detalle_Listas","id_lis_per",String.valueOf(id_lista));
                                           Toast.makeText(ListaCompras.this, "Lista Eliminada Correctamente", Toast.LENGTH_SHORT).show();
                                           Intent atras = new Intent(ListaCompras.this, RecordatorioCompras.class);
                                           startActivity(atras);
                                       }
                                   });

                                   //Se gusarda la Lista con un estado "Guardado"
                                   guardar.setPositiveButton("SI", new DialogInterface.OnClickListener(){

                                       @Override
                                       public void onClick(DialogInterface dialog, int which) {
                                           admin.modificar("Listas", "est_lis","Guardado","id_lis",id_lista);
                                           admin.modificar("Detalle_Listas","com_nocom","N","id_lis_per",id_lista);
                                           Toast.makeText(ListaCompras.this, "Lista Guardada Correctamente", Toast.LENGTH_SHORT).show();
                                           Intent atras = new Intent(ListaCompras.this, RecordatorioCompras.class);
                                           startActivity(atras);
                                       }
                                   });
                                   AlertDialog dialogo=guardar.create();
                                   dialogo.show();
                               }
                    }

                }
            });
            AlertDialog compra=confirmacion.create();
            compra.show();

            // regresar Recordatorio Compras

        }else if(id==R.id.item_re) {
            Intent siguiente = new Intent(this, RecordatorioCompras.class);
            startActivity(siguiente);

            //añade productos a la lista.

        }else if(id==R.id.item_aniadir) {
            Intent i = new Intent(ListaCompras.this, VistaPrevia.class);
            i.putExtra("lista_codigo", id_lista);
            startActivity(i);

            //cambiar estado de una lista

        }else if(id==R.id.item_estado) {
            String cambiar = admin.buscar("est_lis", "Listas", "id_lis", String.valueOf(id_lista));
            if (cambiar != null) {
                if (cambiar.equals("Pendiente")) {
                    admin.modificar("Listas", "est_lis", "Guardado", "id_lis", id_lista);
                    Toast.makeText(ListaCompras.this, "Lista Guardada", Toast.LENGTH_SHORT).show();
                }else{
                    admin.modificar("Listas", "est_lis", "Pendiente", "id_lis", id_lista);
                    Toast.makeText(ListaCompras.this, "Lista Pendiente", Toast.LENGTH_SHORT).show();
                }

            }
        }  else if(id==R.id.item_lista_completa) {
            Intent i = new Intent(ListaCompras.this, VistaPrevia.class);
            i.putExtra("lista_codigo", id_lista);
            startActivity(i);
    }
        return super.onOptionsItemSelected(item);
    }

}