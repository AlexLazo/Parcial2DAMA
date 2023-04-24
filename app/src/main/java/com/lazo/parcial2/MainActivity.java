package com.lazo.parcial2;

import static android.provider.ContactsContract.CommonDataKinds.Website.URL;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.lazo.parcial2.Clases.ConfiguracionesGlobales;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    Button botonNuevo;
    Button btnBuscar;
    EditText txtCriterio;
    ListView actividades;

    ConfiguracionesGlobales objConfiguraciones = new ConfiguracionesGlobales();
    String URL = objConfiguraciones.urlTareas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Referencia
        botonNuevo = findViewById(R.id.btnNuevaActividad);
        botonNuevo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent ventana = new Intent(MainActivity.this, NuevaActividad.class);
                startActivity(ventana);
            }
        });
        txtCriterio = findViewById(R.id.txtCriterio);
        actividades = findViewById(R.id.lvActividades);
        btnBuscar = findViewById(R.id.btnBuscar);
        btnBuscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                llenarLista();
            }
        });

    }
    @Override
    protected void onResume() {
        super.onResume();
        try {
            llenarLista();

        }catch (Exception error){
            Toast.makeText(MainActivity.this, "Error"+error.getMessage(), Toast.LENGTH_SHORT).show();
        }

    }
    private void llenarLista() {
        final String criterio = txtCriterio.getText().toString();
        RequestQueue objetoPeticion = Volley.newRequestQueue(MainActivity.this);
        StringRequest peticion = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>(){
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject objJSONResultado = new JSONObject(response.toString());
                    JSONArray aDatosResultado = objJSONResultado.getJSONArray("resultado");

                    AdaptadorListaActividades miAdaptador = new AdaptadorListaActividades();
                    miAdaptador.arregloDatos = aDatosResultado;
                    actividades.setAdapter(miAdaptador);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        },new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this,"Error: "+error.getMessage(),Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String,String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<String,String>();
                params.put("accion","listar_actividades");
                params.put("filtro",criterio);
                return params;
            }
        };

        objetoPeticion.add(peticion);

    }
    class AdaptadorListaActividades extends BaseAdapter {
        public JSONArray arregloDatos;

        @Override
        public int getCount() {
            return arregloDatos.length();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View v, ViewGroup parent) {
            v = getLayoutInflater().inflate(R.layout.fila_actividades,null);
            TextView txtDescripcion = v.findViewById(R.id.tvDescripcion);
            TextView txtTitulo =v.findViewById(R.id.tvNombre);
            TextView txtFecha =v.findViewById(R.id.tvFecha);
            TextView txtHora =v.findViewById(R.id.tvHora);
            TextView txtEstado =v.findViewById(R.id.tvEstado);

            Button btnver = v.findViewById(R.id.btnVerCobtacto);

            JSONObject objJSON = null;
            try {
                objJSON = arregloDatos.getJSONObject(position);
                final String id, nombre,descripcion, fecha, hora, estado;
                id = objJSON.getString("id_tarea");
                nombre = objJSON.getString("nombre");
                descripcion = objJSON.getString("descripcion");
                fecha = objJSON.getString("fecha");
                hora = objJSON.getString("hora");
                estado = objJSON.getString("estado");

                txtTitulo.setText(nombre);
                txtDescripcion.setText(descripcion);
                txtFecha.setText(fecha);
                txtHora.setText(hora);
                txtEstado.setText(estado);

                btnver.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent ventanaModificar = new Intent(MainActivity.this, ModificarActividades.class);
                        ventanaModificar.putExtra("id_tarea",id);
                        ventanaModificar.putExtra("nombre",nombre);
                        ventanaModificar.putExtra("descripcion",descripcion);
                        ventanaModificar.putExtra("fecha",fecha);
                        ventanaModificar.putExtra("hora",hora);
                        ventanaModificar.putExtra("estado",estado);

                        startActivity(ventanaModificar);
                    }
                });
            }catch (JSONException e){
                e.printStackTrace();
            }
            return v;
        }
    }
}