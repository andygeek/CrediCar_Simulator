package com.andygeek.ccsimulator;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    private Button btn_CrearCuenta;
    private Button btn_Ingresar;
    private EditText et_CorreoLogin;
    private EditText et_PasswordLogin;

    private ProgressDialog progressDialog;

    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        btn_CrearCuenta = findViewById(R.id.btn_CrearCuenta);
        btn_Ingresar = findViewById(R.id.btn_Ingresar);
        et_CorreoLogin = findViewById(R.id.et_CorreoLogin);
        et_PasswordLogin = findViewById(R.id.et_PasswordLogin);
        progressDialog = new ProgressDialog(this);

        Initialize();

        btn_Ingresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SignIn(et_CorreoLogin.getText().toString(), et_PasswordLogin.getText().toString());
                progressDialog.setMessage("Iniciando sesion.....");
                progressDialog.show();
            }
        });

        btn_CrearCuenta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
                startActivity(intent);
            }
        });

    }


    public void Initialize(){

        firebaseAuth = FirebaseAuth.getInstance();

        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                //Obtengo los datos del usuario que inicio sesión.
                FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();

                if(firebaseUser != null){
                    //Alguien ya inicio sesión.
                }
                else{
                    //No hay ningun inicio de sesión
                }
            }
        };
    }


    private void SignIn(String email, String password){

        firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Intent intent = new Intent(LoginActivity.this, ProfileActivity.class);
                    et_CorreoLogin.setText("");
                    et_PasswordLogin.setText("");
                    progressDialog.dismiss();
                    startActivity(intent);

                }
                else{
                    Toast.makeText(LoginActivity.this, "No se pudo ingresar", Toast.LENGTH_SHORT).show();
                }
            }
        });

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
