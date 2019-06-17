package com.andygeek.ccsimulator;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
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
import android.widget.TextView;

import com.andygeek.ccsimulator.model.Usuario;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DateFormat;
import java.util.Calendar;

public class SignUp2Activity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    private Usuario usuario;
    private EditText et_NombreSignUp;
    private EditText et_ApellidoSignUp;
    private EditText et_DniSignUp;
    private TextView tv_FechaNacSignUp;
    private Button btn_FinalizarSignUp;

    private static final String USUARIO_NODE = "Usuarios";

    //PAra base de datos
    private DatabaseReference databaseReference;

    //Para authentication
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private FirebaseUser firebaseUser;

    //Progrsss
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up2);

        tv_FechaNacSignUp = findViewById(R.id.tv_FechaNacSignUp);
        et_NombreSignUp = findViewById(R.id.et_NombreSignUp);
        et_ApellidoSignUp = findViewById(R.id.et_ApellidoSignUp);
        et_DniSignUp = findViewById(R.id.et_DniSignUp);
        btn_FinalizarSignUp = findViewById(R.id.btn_FinalizarSignUp);
        progressDialog = new ProgressDialog(this);


        initialize();

        //Para firestore databse
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        databaseReference = FirebaseDatabase.getInstance().getReference();
        //----------------

        tv_FechaNacSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment datePicker = new DatePicker_Fragment();
                datePicker.show(getSupportFragmentManager(), "date picker");
            }
        });


        btn_FinalizarSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.setMessage("Guardando datos.....");
                progressDialog.show();
                crear_Usuario();
                Intent intent  = new Intent(SignUp2Activity.this, SignUp3Activity.class);
                startActivity(intent);
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

        tv_FechaNacSignUp.setText(currentDateString);
    }

    public void crear_Usuario(){
        Usuario usuario = new Usuario();
        usuario.setId(firebaseUser.getUid().toString());
        usuario.setCorreo(firebaseUser.getEmail().toString());
        usuario.setNombres(et_NombreSignUp.getText().toString());
        usuario.setApellidos(et_ApellidoSignUp.getText().toString());

        databaseReference.child(USUARIO_NODE).child(usuario.getId()).setValue(usuario);
        progressDialog.dismiss();
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
