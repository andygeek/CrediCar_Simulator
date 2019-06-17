package com.andygeek.ccsimulator;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.andygeek.ccsimulator.model.Usuario;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ProfileActivity extends AppCompatActivity {

    private static final String USUARIO_NODE = "Usuarios";

    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private FirebaseUser firebaseUser;

    private String nombres;
    private String apellidos;

    private TextView tv_EmailProfile;
    private LinearLayout ll_CerrarSesion;
    private TextView tv_NombreProfile;

    //PAra base de datos
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        tv_EmailProfile = findViewById(R.id.tv_EmailProfile);
        ll_CerrarSesion = findViewById(R.id.ll_CerrarSesion);
        tv_NombreProfile = findViewById(R.id.tv_NombreProfile);



        initialize();


        //Para firestore databse
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        databaseReference = FirebaseDatabase.getInstance().getReference();
        //----------------

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                nombres = dataSnapshot.child(USUARIO_NODE).child(firebaseUser.getUid()).child("nombres/").getValue(String.class);
                apellidos = dataSnapshot.child(USUARIO_NODE).child(firebaseUser.getUid()).child("apellidos/").getValue(String.class);
                tv_NombreProfile.setText(nombres + " " + apellidos);
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
                startActivity(new Intent(getBaseContext(), LoginActivity.class)
                        .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP));
            }
        });




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
