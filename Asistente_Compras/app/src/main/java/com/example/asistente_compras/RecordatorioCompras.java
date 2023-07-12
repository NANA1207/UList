package com.example.asistente_compras;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.util.ArrayList;

public class RecordatorioCompras extends AppCompatActivity {

    //Lista de los recosrdatorios Creados

    private AdminSOLite admin = new AdminSOLite(this);
    private ArrayList <String> listas=new ArrayList<>();
    private ListView lv1;
    int cod_usu;
    int cod_lis;
    ArrayAdapter ad;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recordatorio_compras);

        lv1=(ListView)findViewById(R.id.recordatorio);
        cod_usu=admin.buscar_estado_activo();


         //Lista las Listas con su estdao

        listas=admin.listar_2campos(1,2,"Listas","id_usu_per",cod_usu);
        if(listas!=null) {

        //envia las pendientes al inicio de la Lista
            for(int t=0;t<listas.size();t++){
                String estado= listas.get(t).split(": ")[0].trim();
               if(estado.equals("Pendiente")){
                   listas.add(0,listas.get(t));
                   listas.remove(t+1);
               }

            }
          ad = new ArrayAdapter(this, android.R.layout.simple_list_item_1, listas);
           lv1.setAdapter(ad);
        }

        lv1.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

        lv1.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                String items = (String) lv1.getItemAtPosition(position);
                String item = items.split(": ")[1].trim();
                cod_lis=admin.buscar_id_lis(item,cod_usu);
                AlertDialog.Builder alerta = new AlertDialog.Builder(RecordatorioCompras.this);
                alerta.setCancelable(true);
                alerta.setTitle("¿Desea eliminar "+item+ "?");

                // ELIMINAR LISTA
                alerta.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        //se elimina la lisat y sus detalles de la BD

                        admin.borrar_x2parametros("Listas","id_lis", cod_lis,"id_usu_per",cod_usu);
                        admin.borrar("Detalle_Listas","id_lis_per",String.valueOf(cod_lis));
                        listas.remove( items );
                        lv1.setAdapter(ad);

                    }
                });
                alerta.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();

                    }
                });
                AlertDialog showDialogo = alerta.create();
                showDialogo.show();
                return true;
            }
        });
        //seguir a Lista Compras
        lv1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                String items = (String) lv1.getItemAtPosition(position);
                String item = items.split(": ")[1].trim();                                    //toma el nombre de la lista
                int cod_lis=admin.buscar_id_lis(item,cod_usu);
                Intent siguiente = new Intent(RecordatorioCompras.this, ListaCompras.class);
                siguiente.putExtra("id",cod_lis);
                startActivity(siguiente);

            }
        });
    }


    //seleccionar crear
    public void aparecer(View view){
        //llama a mensajeEmerjente
        Intent i= new Intent(this, mensajeEmergente.class);
        startActivity(i);
    }

    //Cambia de usuario, colocando al actual el estado "D" (Desactivo)
    public void cambiarUsuario(View view){
        int cod_usu=admin.buscar_estado_activo();
        admin.modificar_esatado_usu("D",cod_usu);
        Intent i= new Intent(this, MainActivity.class);
        startActivity(i);
    }

}