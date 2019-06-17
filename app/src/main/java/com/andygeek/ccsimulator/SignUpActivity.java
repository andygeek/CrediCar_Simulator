package com.andygeek.ccsimulator;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
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

public class SignUpActivity extends AppCompatActivity {

    private EditText et_Correo_SignUp;
    private EditText et_Password_SignUp_1;
    private EditText et_Password_SignUp_2;
    private Button btn_ContinuarSignUp;
    private Button btn_AceptarErrorPassword;

    private Dialog dialog_PopUpError;

    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;

    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        et_Correo_SignUp = findViewById(R.id.et_Correo_SignUp);
        et_Password_SignUp_1 = findViewById(R.id.et_Password_SignUp_1);
        et_Password_SignUp_2 = findViewById(R.id.et_Password_SignUp_2);
        btn_ContinuarSignUp = findViewById(R.id.btn_ContinuarSignUp);

        dialog_PopUpError = new Dialog(this);

        progressDialog = new ProgressDialog(this);

        Initialize();

        btn_ContinuarSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String password_1 = et_Password_SignUp_1.getText().toString();
                String password_2 = et_Password_SignUp_2.getText().toString();

                if(!password_1.equals(password_2) || password_1.length()<6){
                    show_dialog_error_password();
                    et_Password_SignUp_1.setText("");
                    et_Password_SignUp_2.setText("");
                }
                else{
                    progressDialog.setMessage("Conectando.....");
                    progressDialog.show();
                    CreateAccount(et_Correo_SignUp.getText().toString(), et_Password_SignUp_1.getText().toString());
                    SignIn(et_Correo_SignUp.getText().toString(), et_Password_SignUp_1.getText().toString());
                }
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


    public void show_dialog_error_password(){
        dialog_PopUpError.setContentView(R.layout.popup_error_sigup_password);
        btn_AceptarErrorPassword = (Button)dialog_PopUpError.findViewById(R.id.btn_AceptarErrorPassword);

        btn_AceptarErrorPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog_PopUpError.dismiss();
            }
        });

        dialog_PopUpError.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog_PopUpError.show();

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

    private void CreateAccount(String email, String password){

        firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                }
                else {
                    Toast.makeText(SignUpActivity.this, "No se pudo crear", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }


    private void SignIn(String email, String password){

        firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Intent intent = new Intent(SignUpActivity.this, SignUp2Activity.class);
                    progressDialog.dismiss();
                    startActivity(intent);
                }
                else{
                    Toast.makeText(SignUpActivity.this, "No se pudo ingresar", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }



}


