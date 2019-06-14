package com.andygeek.ccsimulator;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class PeriodosActivity extends AppCompatActivity {

    private ExpandableListView lv_PlanPago;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_periodos);

        lv_PlanPago = findViewById(R.id.lv_PlanPago);

        Datos_credito dc = (Datos_credito) getIntent().getExtras().getParcelable("datos_credito");

        ArrayList<Periodo> arrayPeriodo = new ArrayList<Periodo>();

        for (int i = 0; i < dc.getPeriodos(); i++){
            Periodo p = new Periodo();
            p.setNumero(i);
            p.setTes(dc.getTes());
            if(i==0){
                p.setSaldo_inicial(dc.getPrestamo());
            }
            else{
                p.setSaldo_inicial(arrayPeriodo.get(i-1).getSaldo_final());
            }
            p.calcular(dc.getPeriodos());
            arrayPeriodo.add(p);
        }

        PeriodoAdapter adapter = new PeriodoAdapter(arrayPeriodo, this);

        lv_PlanPago.setAdapter(adapter);

    }
}
