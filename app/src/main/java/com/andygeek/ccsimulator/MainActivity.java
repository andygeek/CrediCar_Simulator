package com.andygeek.ccsimulator;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private EditText et_Prestamo;
    private EditText et_Tes;
    private EditText et_Periodos;
    private Button btn_Calcular;

    //private Datos_credito datos_credito;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        et_Prestamo = findViewById(R.id.et_Prestamo);
        et_Tes = findViewById(R.id.et_Tes);
        et_Periodos = findViewById(R.id.et_Periodos);
        btn_Calcular = findViewById(R.id.btn_Calcular);

        btn_Calcular.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }
}
