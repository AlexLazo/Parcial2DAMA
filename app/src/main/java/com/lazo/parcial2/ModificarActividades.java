package com.lazo.parcial2;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.lazo.parcial2.Clases.ConfiguracionesGlobales;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ModificarActividades extends AppCompatActivity {

    EditText nombre, descripcion, fecha, hora, status;
    Button botonModificar,botonRegresar,botonEliminar;
    String id_tarea,nombre_act,descripcion_act, fecha_act, hora_act, status_act;
    ConfiguracionesGlobales objConfiguraciones = new ConfiguracionesGlobales();
    String URL = objConfiguraciones.urlTareas;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modificar_actividades);

        nombre = findViewById(R.id.txtNombreNuevo);
        descripcion = findViewById(R.id.txtDescripcionNueva);
        fecha = findViewById(R.id.txtFecha);
        hora = findViewById(R.id.txtHora);
        status = findViewById(R.id.txtEstado);

        botonModificar = findViewById(R.id.btnGuardar);
        botonEliminar = findViewById(R.id.btnEliminar);
        botonRegresar = findViewById(R.id.btnRegresarContactoNuevo);

        botonModificar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                modificar();
            }
        });

        botonRegresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                regresar();
            }
        });

        botonEliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                eliminar();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        Bundle valoresAdicionales = getIntent().getExtras();
        if(valoresAdicionales == null){
            Toast.makeText(ModificarActividades.this, "Debe enviar un ID de contacto", Toast.LENGTH_SHORT).show();
            id_tarea = "";
            regresar();
        }else{
            id_tarea= valoresAdicionales.getString("id_tarea");
            nombre_act= valoresAdicionales.getString("nombre");
            descripcion_act = valoresAdicionales.getString("descripcion");
            fecha_act= valoresAdicionales.getString("fecha");
            hora_act = valoresAdicionales.getString("hora");
            status_act = valoresAdicionales.getString("estado");
            verContacto();
        }
    }

    private void modificar() {
        RequestQueue objetoPeticion = Volley.newRequestQueue(ModificarActividades.this);
        try {

            StringRequest peticion = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>(){
                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject objJSONResultado = new JSONObject(response.toString());
                        String estado = objJSONResultado.getString("estado");
                        if(estado.equals("true")){
                            Toast.makeText(ModificarActividades.this, "Actividad ha sido modificado con exito", Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(ModificarActividades.this, "Error:"+estado, Toast.LENGTH_SHORT).show();
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            },new Response.ErrorListener(){
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(ModificarActividades.this,"Error: "+ error.getMessage(),Toast.LENGTH_SHORT).show();
                }
            }){
                @Override
                protected Map<String,String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("accion","modificar");
                    params.put("id_tarea",String.valueOf(id_tarea));
                    params.put("nombre",nombre.getText().toString());
                    params.put("descripcion",descripcion.getText().toString());
                    params.put("fecha",fecha.getText().toString());
                    params.put("hora",hora.getText().toString());
                    params.put("estado",status.getText().toString());
                    return params;
                }
            };

            objetoPeticion.add(peticion);

        }catch (Exception error){
            Toast.makeText(ModificarActividades.this, "Error en tiempo de ejecucion:"+error.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void verContacto() {
        nombre.setText(nombre_act);
        descripcion.setText(descripcion_act);
        fecha.setText(fecha_act);
        hora.setText(hora_act);
        status.setText(status_act);
    }

    private void eliminar() {
        try {
            RequestQueue objetoPeticion = Volley.newRequestQueue(ModificarActividades.this);
            StringRequest peticion = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>(){
                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject objJSONResultado = new JSONObject(response.toString());
                        String estado = objJSONResultado.getString("estado");
                        if(estado.equals("true")){
                            Toast.makeText(ModificarActividades.this, "Actividad eliminada con exito", Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(ModificarActividades.this, "Error:"+estado, Toast.LENGTH_SHORT).show();
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            },new Response.ErrorListener(){
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(ModificarActividades.this,"Error: "+ error.getMessage(),Toast.LENGTH_SHORT).show();
                }
            }){
                @Override
                protected Map<String,String> getParams() throws AuthFailureError {
                    Map<String,String> params = new HashMap<String,String>();
                    params.put("accion","eliminar");
                    params.put("id_tarea",id_tarea)00
                    return params;
                }
            };

            objetoPeticion.add(peticion);

        }catch (Exception error){
            Toast.makeText(ModificarActividades.this, "Error en tiempo de ejecucion:"+error.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void regresar() {
        Intent actividad = new Intent(ModificarActividades.this, MainActivity.class);
        startActivity(actividad);
        ModificarActividades.this.finish();
    }

}