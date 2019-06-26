package com.andygeek.ccsimulator;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.andygeek.ccsimulator.model.Datos_credito;
import com.andygeek.ccsimulator.model.Usuario;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class CalculadoraCreditoActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    private static final String USUARIO_NODE = "Usuarios";
    private static final String HISTORIAL_NODE = "Historial";

    private Button btn_Calcular;

    private TextView tv_FechaPrestamo; //Para mostrar, el vendadero o pasare como dato
    private EditText et_ValorVehiculo;
    private EditText et_CuotaInicial;
    private EditText et_NumPeriodos;
    private EditText et_Tea;
    private EditText et_Sva;

    private RadioButton rb_Endosado;
    private RadioButton rb_Individual;

    private RadioButton rb_EnvioVirtual;
    private RadioButton rb_EnvioFisico;

    private ImageButton imb_Calendario;

    private Datos_credito dc;

    //PAra base de datos
    private DatabaseReference databaseReference;
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private FirebaseUser firebaseUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculadora_credito);

        dc = new Datos_credito();

        tv_FechaPrestamo = findViewById(R.id.tv_FechaPrestamo);
        et_ValorVehiculo = findViewById(R.id.et_ValorVehiculo);
        et_CuotaInicial = findViewById(R.id.et_CuotaInicial);
        et_NumPeriodos = findViewById(R.id.et_NumPeriodos);
        et_Tea = findViewById(R.id.et_Tea);
        et_Sva = findViewById(R.id.et_Sva);
        rb_Endosado = findViewById(R.id.rb_Endosado);
        rb_Individual = findViewById(R.id.rb_Individual);
        rb_EnvioVirtual = findViewById(R.id.rb_EnvioVirtual);
        rb_EnvioFisico = findViewById(R.id.rb_EnvioFisico);
        imb_Calendario = findViewById(R.id.imb_Calendario);
        btn_Calcular = findViewById(R.id.btn_Calcular);

        initialize();

        //Para firestore databse
        //FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        databaseReference = FirebaseDatabase.getInstance().getReference();
        //----------------



        btn_Calcular.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                dc.setPrecio_vehiculo(Double.parseDouble(et_ValorVehiculo.getText().toString()));
                int nump = Integer.parseInt(et_NumPeriodos.getText().toString());
                dc.setPeriodos(nump);
                dc.setInicial(Double.parseDouble(et_CuotaInicial.getText().toString()));
                double tea_escrita = Double.parseDouble(et_Tea.getText().toString());
                double tea = tea_escrita/100f;
                dc.setTea(tea);

                if(rb_Individual.isChecked() == true && rb_Endosado.isChecked() == false){
                    dc.setEndosado_individual(false);
                    double indiv = 0.0005f;
                    dc.setTdm(indiv);

                }
                else{
                    dc.setEndosado_individual(true);
                    dc.setTdm(0);
                }

                if(rb_EnvioFisico.isChecked() == true && rb_EnvioVirtual.isChecked() == false){
                    dc.setFisico_virtual(true);
                }
                else{
                    dc.setFisico_virtual(false);
                }

                double sva_escrito = Double.parseDouble(et_Sva.getText().toString());
                double sva = sva_escrito/100;
                dc.setSva(sva);
                dc.calcular();

                Intent intent = new Intent(CalculadoraCreditoActivity.this, PeriodosActivity.class);
                Guardar_Historial(dc);
                intent.putExtra("objeto", dc);
                startActivity(intent);

            }
        });



        imb_Calendario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment datePicker = new DatePicker_Fragment();
                datePicker.show(getSupportFragmentManager(), "date picker");
            }
        });


    }


    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, month);
        c.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        String currentDateString = DateFormat.getDateInstance(DateFormat.DATE_FIELD).format(c.getTime());

        dc.setDia(dayOfMonth);
        dc.setMes(month);
        dc.setAnio(year);
        tv_FechaPrestamo.setText(currentDateString);

    }


    public void Guardar_Historial(Datos_credito dc){
        Date date = new Date();
        DateFormat hourdateFormat = new SimpleDateFormat("HH:mm:ss dd-MM-yyyy");
        String id = hourdateFormat.format(date);
        databaseReference.child(USUARIO_NODE).child(firebaseUser.getUid()).child(HISTORIAL_NODE).child(id).setValue(dc);

    }

    public void initialize(){

        firebaseAuth = FirebaseAuth.getInstance();

        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                //Creo un objeto User para mostrar sus datos.
                firebaseUser = firebaseAuth.getCurrentUser();

                if(firebaseUser != null){
                    Log.v(null, "------- Estamos dentro --------");
                }
                else{
                    Log.v(null, "------- Nos salimos --------");
                }
            }
        };
    }

    @Override
    protected void onStart() {
        super.onStart();
        firebaseAuth.addAuthStateListener(authStateListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        firebaseAuth.removeAuthStateListener(authStateListener);
    }

}
