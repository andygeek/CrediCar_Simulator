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
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SignUpActivity extends AppCompatActivity {

    private EditText et_Correo_SignUp;
    private EditText et_Password_SignUp_1;
    private EditText et_Password_SignUp_2;
    private Button btn_ContinuarSignUp;

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

                String correo = et_Correo_SignUp.getText().toString();
                String password_1 = et_Password_SignUp_1.getText().toString();
                String password_2 = et_Password_SignUp_2.getText().toString();

                if(correo.length() == 0){
                    show_dialog_error_password("Es necesario escribir un correo electronico.");
                }
                else if(!validar_correo(correo)){
                    show_dialog_error_password("Escriba una dirección de correo electrónica valida.");
                }
                else if(!password_1.equals(password_2)){
                    show_dialog_error_password("Las contraseñas no coinciden. Vuelva a escribirlas.");
                    et_Password_SignUp_1.setText("");
                    et_Password_SignUp_2.setText("");
                }
                else if(password_1.length()<6){
                    show_dialog_error_password("La contraseña es demaciado corta. Intente con una nueva contraseña.");
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


    public void show_dialog_error_password(String message){
        dialog_PopUpError.setContentView(R.layout.pop_up_error);
        Button btn_AceptarErrorPassword = (Button)dialog_PopUpError.findViewById(R.id.btn_AceptarErrorPassword);
        TextView tv_MessageError = (TextView)dialog_PopUpError.findViewById(R.id.tv_MessageError);
        tv_MessageError.setText(message);

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

    public boolean validar_correo(String email){
        // Patrón para validar el email
        Pattern pattern = Pattern
                .compile("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                        + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$");

        // El email a validar

        Matcher mather = pattern.matcher(email);

        if (mather.find() == true) {
            //Si el correo el valido
            return true;
        } else {
            //Si no es valido
            return false;
        }
    }

}


