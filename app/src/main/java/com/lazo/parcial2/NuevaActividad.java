package com.lazo.parcial2;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
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

public class NuevaActividad extends AppCompatActivity {
    EditText nombre, descripcion, fecha, hora, status;
    Button btnregistrar,btnregresar;
    ConfiguracionesGlobales objConfiguraciones = new ConfiguracionesGlobales();

    String URL = objConfiguraciones.urlTareas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nueva_tarea);
        nombre = findViewById(R.id.txtNombreNuevo);
        descripcion = findViewById(R.id.txtDescripcionNueva);
        fecha = findViewById(R.id.txtFecha);
        hora = findViewById(R.id.txtHora);
        status = findViewById(R.id.txtEstado);
        btnregistrar = findViewById(R.id.btnGuardar);
        btnregresar = findViewById(R.id.btnRegresarContactoNuevo);

        btnregistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registrar();
            }
        });
        btnregresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                regresar();
            }
        });
    }

    private void regresar() {
        Intent actividad = new Intent(NuevaActividad.this,MainActivity.class);
        startActivity(actividad);
        NuevaActividad.this.finish();
    }

    private void registrar() {
        try {
            RequestQueue objetoPeticion = Volley.newRequestQueue(NuevaActividad.this);
            StringRequest peticion = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>(){
                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject objJSONResultado = new JSONObject(response.toString());
                        String estado = objJSONResultado.getString("estado");
                        if(estado.equals("true")){
                            Toast.makeText(NuevaActividad.this, "Actividad registrada con exito", Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(NuevaActividad.this, "Error:"+estado, Toast.LENGTH_SHORT).show();
                        }


                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            },new Response.ErrorListener(){
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(NuevaActividad.this,"Error: "+ error.getMessage(),Toast.LENGTH_SHORT).show();
                }
            }){
                @Override
                protected Map<String,String> getParams() throws AuthFailureError {
                    Map<String,String> params = new HashMap<String,String>();
                    params.put("accion","insertar");
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
            Toast.makeText(NuevaActividad.this, "Error en tiempo de ejecucion:"+error.getMessage(), Toast.LENGTH_SHORT).show();
        }

    }
}