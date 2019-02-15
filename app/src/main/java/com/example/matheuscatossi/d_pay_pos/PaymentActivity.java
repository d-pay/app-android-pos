package com.example.matheuscatossi.d_pay_pos;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

public class PaymentActivity extends AppCompatActivity {

    private Spinner spinner;
    private static final String[] paths = {"Sapataria Juscelino", "Claudio Modas", "Barbearia José Costela"};
    Button btnSeguinte;
    TextView tvValor;

    String editValor;

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
                        break;
                    case 1:
                        // Whatever you want to happen when the second item gets selected
                        break;
                    case 2:
                        // Whatever you want to happen when the thrid item gets selected
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

                                Intent myIntent = new Intent(PaymentActivity.this, MainActivity.class);
                                PaymentActivity.this.startActivity(myIntent);
                                PaymentActivity.this.finish();
                            }
                        })
                .setNegativeButton("não", null)
                        .show();



            }
        });
    }
}
