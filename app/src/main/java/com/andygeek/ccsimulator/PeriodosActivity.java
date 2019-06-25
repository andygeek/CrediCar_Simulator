package com.andygeek.ccsimulator;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.TextView;

import com.andygeek.ccsimulator.model.Datos_credito;
import com.andygeek.ccsimulator.model.Datos_periodo;

import java.io.Console;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Random;

public class PeriodosActivity extends AppCompatActivity {

    private ExpandableListView lv_PlanPago;
    private TextView tv_dTna;
    private Datos_credito dc;
    private ArrayList<Datos_periodo> listaPeriodos;

    private static final String TAG = "MyActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_periodos);
        tv_dTna = findViewById(R.id.tv_dTna);
        lv_PlanPago = findViewById(R.id.lv_PlanPago);

        dc = (Datos_credito)getIntent().getExtras().getParcelable("objeto");

        listaPeriodos = new ArrayList<Datos_periodo>();

        hallar_raiz(dc,listaPeriodos);

        PeriodoAdapter adapter = new PeriodoAdapter(listaPeriodos, this);
        lv_PlanPago.setAdapter(adapter);

    }

    double recursividad(int n, double x, Datos_credito d)
    {
        if (n == 0)
        {
            return d.getPrecio_vehiculo() - d.getInicial();
        }
        else
        {
            double saldo_anterior = recursividad(n - 1, x, d);
            int numero_dias = dias_del_mes(fecha_de_pago_periodoN(n - 1, d));

            double d_tasa_nominal_ajustada = tasa_nominal_ajustada(numero_dias, d.getTna());
            double d_tasa_degravamen_ajustado = tasa_seguro_degravamen_ajustada(numero_dias, d.getTda());
            double d_tasa_vehicular_ajustada = tasa_seguro_vehicular_ajustada(numero_dias, d.getSva());

            double d_seguro_degrabamen = seguro_degravamen(saldo_anterior, d_tasa_degravamen_ajustado);
            double d_seguro_vehicular = seguro_vehicular(d.getPrecio_vehiculo(), d_tasa_vehicular_ajustada);
            double d_interes = interes(saldo_anterior, d_tasa_nominal_ajustada);

            double amortz = amortizacion(x, suma_intereses_seguros(d_seguro_degrabamen, d_seguro_vehicular, d_interes));

            Log.i(TAG,"  numero_dias: " + numero_dias );
            Log.i(TAG,"  saldo anterior: " + saldo_anterior );
            Log.i(TAG,"  tasa_nominal_ajustada: " + d_tasa_nominal_ajustada );
            Log.i(TAG,"  d_seguro_degrabamen: " + d_seguro_degrabamen );
            Log.i(TAG,"  d_seguro_vehicular: " + d_seguro_vehicular );
            Log.i(TAG,"  d_interes: " + d_interes );
            Log.i(TAG,"  amortz: " + amortz );

            Log.i(TAG," RRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRR " );

            return saldo_anterior - amortz;
        }
    }

    double recursividad_PRINT(int n, double x, Datos_credito d, ArrayList<Datos_periodo> lp)
    {
        Datos_periodo dp = new Datos_periodo();

        if (n == 0)
        {
            return d.getPrecio_vehiculo() - d.getInicial();
        }
        else
        {
            double saldo_anterior = recursividad_PRINT(n - 1, x, d, lp);
            Date fecha_pago = fecha_de_pago_periodoN(n, d);//SOLO PARA MOSTRAR
            int numero_dias = dias_del_mes(fecha_de_pago_periodoN(n - 1, d));

            double d_tasa_nominal_ajustada = tasa_nominal_ajustada(numero_dias, d.getTna());
            double d_tasa_degravamen_ajustado = tasa_seguro_degravamen_ajustada(numero_dias, d.getTda());
            double d_tasa_vehicular_ajustada = tasa_seguro_vehicular_ajustada(numero_dias, d.getSva());

            double d_seguro_degrabamen = seguro_degravamen(saldo_anterior, d_tasa_degravamen_ajustado);
            double d_seguro_vehicular = seguro_vehicular(d.getPrecio_vehiculo(), d_tasa_vehicular_ajustada);
            double d_interes = interes(saldo_anterior, d_tasa_nominal_ajustada);

            double amortz = amortizacion(x, suma_intereses_seguros(d_seguro_degrabamen, d_seguro_vehicular, d_interes));
            double d_saldo_actual = saldo_anterior - amortz;

            double redondeo_Saldo_anterior = (double)Math.round(saldo_anterior * 100d) / 100d;
            dp.setSaldo_anterior(redondeo_Saldo_anterior);
            double redondeo_Saldo_actual = (double)Math.round(d_saldo_actual * 100d) / 100d;
            dp.setSaldo_actual(redondeo_Saldo_actual);
            double redondeo_Seguro_degravamen = (double)Math.round(d_seguro_degrabamen * 100d) / 100d;
            dp.setSeguro_degravamen(redondeo_Seguro_degravamen);
            double redondeo_Seguro_vehicular = (double)Math.round(d_seguro_vehicular * 100d) / 100d;
            dp.setSeguro_vehicular(redondeo_Seguro_vehicular);
            double redondeo_Interes = (double)Math.round(d_interes * 100d) / 100d;
            dp.setInteres(redondeo_Interes);
            double redondeo_Amortizacion = (double)Math.round(amortz * 100d) / 100d;
            dp.setAmortizacion(redondeo_Amortizacion);
            double redondeo_Portes = (double)Math.round(d.getPortes() * 100d) / 100d;
            dp.setPortes(redondeo_Portes);
            double redondeo_Cuota = (double)Math.round(x * 100d) / 100d;
            dp.setCuota(redondeo_Cuota);
            double cuota_a_pagar = redondeo_Cuota + redondeo_Portes;
            dp.setCuota_a_pagar(cuota_a_pagar);
            dp.setFecha_pago(fecha_pago);

            lp.add(dp);

            return saldo_anterior - amortz;
        }
    }



    double tasa_nominal_ajustada(int dias, double tna)
    {
        return (tna*dias)/ 365;
    }
    double tasa_seguro_degravamen_ajustada(int dias, double tda)
    {
        return (tda*dias)/ 365;
    }
    double tasa_seguro_vehicular_ajustada(int dias, double sva)
    {
        return (dias * sva) / 365;
    }

    double seguro_degravamen(double saldo_anterior, double seguro_degravamen_ajustado)
    {
        return saldo_anterior * seguro_degravamen_ajustado;
    }
    double seguro_vehicular(double precio_vehiculo, double seguro_vehicular_ajustado)
    {
        return precio_vehiculo * seguro_vehicular_ajustado;
    }
    double interes(double saldo_anterior, double tasa_nominal_ajustada)
    {
        return saldo_anterior * tasa_nominal_ajustada;
    }

    double suma_intereses_seguros(double seguro_degravamen, double seguro_vehicular, double interes)
    {
        return seguro_degravamen + seguro_vehicular + interes;
    }
    double amortizacion(double cuota, double suma)
    {
        return cuota - suma;
    }




    static int dias_del_mes(Date fecha)
    {
        Calendar cal = new GregorianCalendar(fecha.getYear(), fecha.getMonth(), 1);
        int days = cal.getActualMaximum(Calendar.DAY_OF_MONTH); // 28
        return days;
    }

    static Date fecha_de_pago_periodoN(int n, Datos_credito d)
    {
        double aux_anio = (d.getFecha_prestamo().getYear() + n)/12f;
        double double_anio = Math.floor(aux_anio);
        int anio = (int)double_anio + d.getFecha_prestamo().getYear();

        int mes;

        int aux_mes = (d.getFecha_prestamo().getMonth() + n)% 12;
        if (aux_mes > 0)
        {
            mes = aux_mes;
        }
        else
        {
            mes = 12;
        }
        Date fecha_pago = new Date(anio,mes,d.getFecha_prestamo().getDay());

        return fecha_pago;
    }



    void hallar_raiz(Datos_credito d, ArrayList<Datos_periodo> lp)
    {
        double respuesta;

        Random n = new Random();

        int a;
        int b;
        double fa;
        double fb;
        int referencia = (int)d.getPrecio_vehiculo() - (int)d.getInicial();
        a = n.nextInt(referencia);
        b = n.nextInt(referencia);

        fa = recursividad(d.getPeriodos(), a, d);

        fb = recursividad(d.getPeriodos(), b, d);


        double pendiente = (fa-fb) / (a-b);
        double B = recursividad(d.getPeriodos(), 0, d);

        respuesta = (-(B)) / pendiente;

        recursividad_PRINT(d.getPeriodos(), respuesta, d, lp);
    }



}
