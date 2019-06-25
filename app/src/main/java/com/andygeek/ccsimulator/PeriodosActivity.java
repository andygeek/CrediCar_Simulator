package com.andygeek.ccsimulator;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.TextView;

import com.andygeek.ccsimulator.model.Datos_credito;
import com.andygeek.ccsimulator.model.Datos_periodo;

import java.util.ArrayList;

public class PeriodosActivity extends AppCompatActivity {

    //private ExpandableListView lv_PlanPago;
    private TextView tv_dTna;
    private Datos_credito dc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_periodos);
        tv_dTna = findViewById(R.id.tv_dTna);

        dc = (Datos_credito)getIntent().getExtras().getParcelable("objeto");
        tv_dTna.setText(String.valueOf(dc.getTna()));

    }
}
