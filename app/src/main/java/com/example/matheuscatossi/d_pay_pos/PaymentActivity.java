package com.example.matheuscatossi.d_pay_pos;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.matheuscatossi.d_pay_pos.model.ResultRequest;
import com.example.matheuscatossi.d_pay_pos.webservice.APIClient;
import com.example.matheuscatossi.d_pay_pos.webservice.APIInterface;

import java.io.IOException;
import java.io.InputStream;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PaymentActivity extends AppCompatActivity {



    private Spinner spinner;
    private static final String[] paths = {"Tuin Sapataria", "Restaurante Paladar", "Minha Oficina", "Doutor Faz tudo", "Plumber encanamentos"};
    Button btnSeguinte;
    TextView tvValor;

    String editValor;


    private APIInterface apiService;

    private Call<ResultRequest> callPackage;

    private ProgressDialog progress;


    String company = "";

    private TrustManager[] getWrappedTrustManagers(TrustManager[] trustManagers) {
        final X509TrustManager originalTrustManager = (X509TrustManager) trustManagers[0];
        return new TrustManager[]{
                new X509TrustManager() {
                    public X509Certificate[] getAcceptedIssuers() {
                        return originalTrustManager.getAcceptedIssuers();
                    }
                    public void checkClientTrusted(X509Certificate[] certs, String authType) {
                        try {
                            originalTrustManager.checkClientTrusted(certs, authType);
                        } catch (CertificateException ignored) {
                        }
                    }
                    public void checkServerTrusted(X509Certificate[] certs, String authType) {
                        try {
                            originalTrustManager.checkServerTrusted(certs, authType);
                        } catch (CertificateException ignored) {
                        }
                    }
                }
        };
    }

    public SSLSocketFactory getSSLSocketFactory()
            throws CertificateException, KeyStoreException, IOException,
            NoSuchAlgorithmException, KeyManagementException {
        CertificateFactory cf = CertificateFactory.getInstance("X.509");
        InputStream caInput = getResources().openRawResource(R.raw.cert); // your certificate file
        Certificate ca = cf.generateCertificate(caInput);
        caInput.close();
        KeyStore keyStore = KeyStore.getInstance("BKS");
        keyStore.load(null, null);
        keyStore.setCertificateEntry("ca", ca);
        String tmfAlgorithm = TrustManagerFactory.getDefaultAlgorithm();
        TrustManagerFactory tmf = TrustManagerFactory.getInstance(tmfAlgorithm);
        tmf.init(keyStore);
        TrustManager[] wrappedTrustManagers = getWrappedTrustManagers(tmf.getTrustManagers());
        SSLContext sslContext = SSLContext.getInstance("TLS");
        sslContext.init(null, wrappedTrustManagers, null);
        return sslContext.getSocketFactory();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        spinner = (Spinner)findViewById(R.id.spinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(PaymentActivity.this,
                android.R.layout.simple_spinner_item,paths);

        btnSeguinte = findViewById(R.id.btn_seguinte);

        editValor = getIntent().getStringExtra("VALUE");


        tvValor = findViewById(R.id.tv_valor);
        tvValor.setText("" + editValor);



        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                switch (i) {
                    case 0:
                        // Whatever you want to happen when the first item gets selected
                        company = "" + paths[0];
                        break;
                    case 1:
                        // Whatever you want to happen when the second item gets selected
                        company = ""+ paths[1];
                        break;
                    case 2:
                        // Whatever you want to happen when the thrid item gets selected
                        company = ""+ paths[2];
                        break;

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });






        btnSeguinte.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                new AlertDialog.Builder(PaymentActivity.this).setTitle("Confirmação de pagameento").
                        setMessage("Tem certeza que deseja pagar o valor de " + editValor + "  ?")
                        .setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                                try {
                                    getSSLSocketFactory();
                                }catch (Exception e){
                                    Log.e("ERRO", "" + e);
                                }


                                apiService = APIClient.getService().create(APIInterface.class);
                                progress = ProgressDialog.show(PaymentActivity.this, "Carregando", "Fazendo transação...", true);

                                String valor = editValor.replace(",", ".").replace("R$", "").replaceAll(" ", "");
                                callPackage = apiService.transaction("" + company, "" + (valor).substring(1, valor.length()));
                                Log.i("Request in API", "" + callPackage.request().url().toString());

                                callPackage.enqueue(new Callback<ResultRequest>() {
                                    public void onResponse(Call<ResultRequest> call, Response<ResultRequest> response) {
                                        if (response.raw().code() == 200) {

                                            Log.i("Return Request", "Success");

                                            progress.dismiss();

                                            Intent myIntent = new Intent(PaymentActivity.this, MainActivity.class);
                                            PaymentActivity.this.startActivity(myIntent);
                                            PaymentActivity.this.finish();
                                        }
                                    }

                                    public void onFailure(Call<ResultRequest> call, Throwable t) {
                                        Log.e("ERROR in GetAllBidding", t.toString());
                                        progress.dismiss();
                                    }
                                });
                            }
                        })
                .setNegativeButton("não", null)
                        .show();



            }
        });
    }


}
