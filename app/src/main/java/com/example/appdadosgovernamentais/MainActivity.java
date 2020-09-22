package com.example.appdadosgovernamentais;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

public class MainActivity extends AppCompatActivity {
    TextView textResultado;
    EditText codigoMunicipio;
    EditText ano;
    List<BolsaFamilia> bolsaFamilias = new ArrayList<BolsaFamilia>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textResultado = findViewById(R.id.textResult);
        codigoMunicipio = findViewById(R.id.codigoMunicipio);
        ano = findViewById(R.id.ano);
    }

    public void solicitarDado(View view) {
        RequestQueue queue = Volley.newRequestQueue(this);
        String codigoString = codigoMunicipio.getText().toString();
        String anoString = ano.getText().toString();
        String mesString = "";

        for (int i = 1; i <=12; i++) {
            if (i < 10){
                mesString = "0"+i;
            } else{
                mesString = ""+i;
            }
            String url = "http://www.transparencia.gov.br/api-de-dados/bolsa-familia-por-municipio?mesAno=" + anoString + mesString + "&codigoIbge=" + codigoString + "&pagina=1";

            int finalI = i;
            JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url,null,
                        new Response.Listener<JSONArray>() {
                            @Override
                            public void onResponse(JSONArray response) {
                                try {
                                    JSONObject result = response.getJSONObject(0);
                                    JSONObject municipioDados = result.getJSONObject("municipio");
                                    JSONObject uf = municipioDados.getJSONObject("uf");

                                    BolsaFamilia bolsaFamilia = new BolsaFamilia();
                                    bolsaFamilia.setNomeMunicipio(municipioDados.getString("nomeIBGE"));
                                    bolsaFamilia.setEstado(uf.getString("nome"));
                                    bolsaFamilia.setBeneficiarios(result.getInt("quantidadeBeneficiados"));
                                    bolsaFamilia.setTotalPago(result.getDouble("valor"));
                                    bolsaFamilias.add(bolsaFamilia);

                                    if (finalI == 12){
                                        mostrarDados();
                                    }
                                } catch (JSONException error){
                                    textResultado.setText(error.getMessage());
                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                textResultado.setText(error.getMessage());
                            }
                        }
                ){
                    @Override
                    public Map<String, String> getHeaders() throws AuthFailureError {
                        Map<String, String> params = new HashMap<>();
                        params.put("chave-api-dados", "46694746af12d7fd0851f447c3a3211e");
                        params.put("Accept", "*/*");

                        return params;
                    }
                };

            request.setRetryPolicy(new DefaultRetryPolicy( 5000, 5, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            queue.add(request);
         }
    }

    public void mostrarDados(){
        double mediaValoresPagos = 0;
        double maiorMesPago = 0;
        String resultado = "";

        for (int i = 1; i <= bolsaFamilias.size(); ++i){
            BolsaFamilia objeto = (BolsaFamilia) bolsaFamilias.get(i-1);
            if (i == 1){
                resultado += "Municipio: "+ objeto.getNomeMunicipio() + "\n";
                resultado += "Estado: "+ objeto.getEstado()+"\n";
                resultado += "Número Beneficiados mês "+i+" :" + objeto.getBeneficiarios()+"\n";
                resultado += "Total pago no mês "+i+" :" + objeto.getTotalPago()+"\n\n";
                mediaValoresPagos += objeto.getTotalPago();
                maiorMesPago += objeto.getTotalPago();
            }
            resultado += "Número Beneficiados mês "+i+" :" + objeto.getBeneficiarios()+"\n";
            resultado += "Total pago no mês "+i+" :" + objeto.getTotalPago()+"\n\n";
            mediaValoresPagos += objeto.getTotalPago();

            if (maiorMesPago < objeto.getTotalPago()){
                maiorMesPago = objeto.getTotalPago();
            }

            if (i == 12){
                resultado += "Média dos valores pagos no ano: "+mediaValoresPagos/12;
                resultado += "Maior pago no ano "+maiorMesPago;
                textResultado.setText(resultado);
            }
        }
    }
}