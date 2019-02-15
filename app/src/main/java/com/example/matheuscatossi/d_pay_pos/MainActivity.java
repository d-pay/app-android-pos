package com.example.matheuscatossi.d_pay_pos;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import java.text.NumberFormat;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {


    Button btnSeguinte;
    EditText editValor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnSeguinte = findViewById(R.id.btn_seguinte);
        editValor = findViewById(R.id.edit_valor);

        editValor.requestFocus();

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

        editValor.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            private String current = "";
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!s.toString().equals(current)) {
                    Locale myLocale = new Locale("pt", "BR");
                    //Nesse bloco ele monta a maskara para money
                    editValor.removeTextChangedListener(this);
                    String cleanString = s.toString().replaceAll("[R$,.]", "").replaceAll("[ ]", "").trim();
                    double parsed = 0;
                    if(cleanString.length() > 1) {
                        parsed = Double.parseDouble("" + cleanString.trim().toString().trim().substring(1, cleanString.length()));
                    } else {
                        parsed = Double.parseDouble("" + cleanString.trim().toString().trim());
                    }
                    String formatted = NumberFormat.getCurrencyInstance(myLocale).format((parsed / 100));
                    current = formatted;
                    editValor.setText(formatted);
                    editValor.setSelection(formatted.length());


                    editValor.addTextChangedListener(this);
                    editValor.requestFocus();
                }
            }
            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        btnSeguinte.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(MainActivity.this, PaymentActivity.class);
                myIntent.putExtra("VALUE", "" + editValor.getText());
                MainActivity.this.startActivity(myIntent);
                MainActivity.this.finish();

            }
        });

    }
}
