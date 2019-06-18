package com.andygeek.ccsimulator;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;

import java.util.Date;

public class CalculadoraCreditoActivity extends AppCompatActivity {

    private EditText et_ValorVehiculo;
    private EditText et_Desembolso;
    private EditText et_NumPeriodos;
    private EditText et_Tea;
    private EditText et_TasaAnualSeguro;
    private EditText et_CuotaInicial;

    //Fecha desembolso
    private int dia;
    private int mes;
    private int anio;

    private boolean individual;
    private double envio;
    private Date fecha_desembolso;

    private double tasa_efectiva_anual;             //No se calcula
    private double tasa_degravamen_mensual;         //No se calcula
    private double tasa_seguro_vehicular_anual;     //No se calcula

    private double tasa_nominal_anual;              //Se calcula facilmente
    private double tasa_degravamen_anual;           //Se calcula facilmente

    private Datos_credito dc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculadora_credito);


        dc = new Datos_credito();



    }


    public void calcular(){
        tasa_nominal_anual = ((12*365)/360)*((Math.pow((1+tasa_efectiva_anual), (1/12)))-1);
        tasa_degravamen_anual = tasa_degravamen_mensual * 12;
    }
}
