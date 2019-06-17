package com.andygeek.ccsimulator;

import android.app.DatePickerDialog;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import com.andygeek.ccsimulator.model.Usuario;

import java.text.DateFormat;
import java.util.Calendar;

public class SignUp2Activity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    private Usuario usuario;
    private EditText et_NombreSignUp;
    private EditText et_ApellidoSignUp;
    private EditText et_DniSignUp;
    private TextView tv_FechaNacSignUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up2);

        tv_FechaNacSignUp = findViewById(R.id.tv_FechaNacSignUp);

        usuario = new Usuario();

        tv_FechaNacSignUp.setOnClickListener(new View.OnClickListener() {
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

        tv_FechaNacSignUp.setText(currentDateString);
    }
}
