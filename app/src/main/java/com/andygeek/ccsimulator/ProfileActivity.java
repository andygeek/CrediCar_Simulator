package com.andygeek.ccsimulator;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.andygeek.ccsimulator.model.Usuario;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ProfileActivity extends AppCompatActivity {

    private static final String USUARIO_NODE = "Usuarios";
    private static final String HISTORIAL_NODE = "Historial";

    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private FirebaseUser firebaseUser;
    private ListView lv_Historial;

    private String nombres;
    private String apellidos;

    private TextView tv_EmailProfile;
    private LinearLayout ll_CerrarSesion;
    private TextView tv_NombreProfile;
    private RelativeLayout rl_Calculadora;

    //PAra base de datos
    private DatabaseReference databaseReference;

    private List<String> historialFecha;
    private ArrayAdapter arrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        tv_EmailProfile = findViewById(R.id.tv_EmailProfile);
        ll_CerrarSesion = findViewById(R.id.ll_CerrarSesion);
        tv_NombreProfile = findViewById(R.id.tv_NombreProfile);
        rl_Calculadora = findViewById(R.id.rl_Calculadora);
        lv_Historial = findViewById(R.id.lv_Historial);

        initialize();


        //Para firestore databse
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        databaseReference = FirebaseDatabase.getInstance().getReference();
        //----------------

        historialFecha = new ArrayList<>();

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                nombres = dataSnapshot.child(USUARIO_NODE).child(firebaseUser.getUid()).child("nombres/").getValue(String.class);
                apellidos = dataSnapshot.child(USUARIO_NODE).child(firebaseUser.getUid()).child("apellidos/").getValue(String.class);
                String nombre_completo = nombres + " " + apellidos;
                tv_NombreProfile.setText(nombre_completo);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                nombres = "Failed database reading";
            }
        });

        //-----

        ll_CerrarSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signOut();
                finishAffinity();
            }
        });

        rl_Calculadora.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfileActivity.this, CalculadoraCreditoActivity.class);
                startActivity(intent);
            }
        });

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, historialFecha);
        databaseReference.child(USUARIO_NODE).child(firebaseUser.getUid()).child(HISTORIAL_NODE).addValueEventListener(valueEventListener);
        lv_Historial.setAdapter(arrayAdapter);
    }




    public void initialize(){

        firebaseAuth = FirebaseAuth.getInstance();

        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                //Creo un objeto User para mostrar sus datos.
                firebaseUser = firebaseAuth.getCurrentUser();

                if(firebaseUser != null){
                    tv_EmailProfile.setText(firebaseUser.getEmail().toString());
                    tv_NombreProfile.setText(nombres);

                }
                else{

                }
            }
        };
    }




    public ValueEventListener valueEventListener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            //Aqui va lo que pasara cuando los datos cambien
            //Primero colocamostodo en blanco
            //Sabemos que los datos viene en la forma de DataSnapshot

            historialFecha.clear();

            if(dataSnapshot.exists()){
                for ( DataSnapshot snapshot:dataSnapshot.getChildren() ) {
                    String fecha = snapshot.getKey();
                    historialFecha.add(fecha);
                }
            }
            //Le indicamos en tiempo de jecucion
            arrayAdapter.notifyDataSetChanged();

        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }


    };




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

    private void signOut(){
        firebaseAuth.signOut();
    }

}
