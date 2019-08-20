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
import com.andygeek.ccsimulator.model.Fecha;

import java.io.Console;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Random;

public class PeriodosActivity extends AppCompatActivity {



    private ExpandableListView lv_PlanPago;
    private Datos_credito dc;
    private ArrayList<Datos_periodo> listaPeriodos;
    private TextView tv_vTCEA;
    private TextView tv_vTIR;
    private TextView tv_vTapagar;
    private TextView tv_vTEA;
    private TextView tv_vFecha_desembolso;
    private TextView tv_vTFinanciado;
    private TextView tv_vCuotaInicial;
    private TextView tv_vPrecioVehiculo;

    private double monto_financiado;

    private static final String TAG = "MyActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_periodos);
        tv_vTCEA = findViewById(R.id.tv_vTCEA);
        tv_vTIR = findViewById(R.id.tv_vTIR);
        lv_PlanPago = findViewById(R.id.lv_PlanPago);
        tv_vTapagar = findViewById(R.id.tv_vTapagar);
        tv_vTEA = findViewById(R.id.tv_vTEA);
        tv_vFecha_desembolso = findViewById(R.id.tv_vFecha_desembolso);
        tv_vCuotaInicial = findViewById(R.id.tv_vCuotaInicial);
        tv_vTFinanciado = findViewById(R.id.tv_vTFinanciado);
        tv_vCuotaInicial = findViewById(R.id.tv_vCuotaInicial);
        tv_vPrecioVehiculo = findViewById(R.id.tv_vPrecioVehiculo);

        dc = (Datos_credito)getIntent().getExtras().getParcelable("objeto");

        listaPeriodos = new ArrayList<Datos_periodo>();

        hallar_raiz(dc,listaPeriodos);

        PeriodoAdapter adapter = new PeriodoAdapter(listaPeriodos, this);
        lv_PlanPago.setAdapter(adapter);


        double tir = hallando_tir(0,0, 100,listaPeriodos, dc);
        double redondeo_tir = (double)Math.round((tir*100) * 100d) / 100d;
        tv_vTIR.setText(String.valueOf(redondeo_tir));

        double tcea = hallar_tcea(tir);
        double redondeo_tcea = (double)Math.round((tcea*100) * 100d) / 100d;
        tv_vTCEA.setText(String.valueOf(redondeo_tcea));

        double total_a_pagar = total_pagar(listaPeriodos);
        double redondeo_total_a_pagar = (double)Math.round(total_a_pagar * 100d) / 100d;
        tv_vTapagar.setText(String.valueOf(redondeo_total_a_pagar));

        double redondeo_tea = (double)Math.round((dc.getTea()*100) * 100d) / 100d;
        tv_vTEA.setText(String.valueOf(redondeo_tea));

        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, dc.getAnio());
        c.set(Calendar.MONTH, dc.getMes());
        c.set(Calendar.DAY_OF_MONTH, dc.getDia());
        String mostrar_fecha = DateFormat.getDateInstance(DateFormat.DATE_FIELD).format(c.getTime());
        tv_vFecha_desembolso.setText(mostrar_fecha);

        monto_financiado = dc.getPrecio_vehiculo() - dc.getInicial();
        double redondeo_monto_financiado = (double)Math.round(monto_financiado * 100d) / 100d;
        tv_vTFinanciado.setText(String.valueOf(redondeo_monto_financiado));

        double redondeo_inicial = (double)Math.round(dc.getInicial() * 100d) / 100d;
        tv_vCuotaInicial.setText(String.valueOf(redondeo_inicial));

        double redondeo_vehiculo = (double)Math.round(dc.getPrecio_vehiculo() * 100d) / 100d;
        tv_vPrecioVehiculo.setText(String.valueOf(redondeo_vehiculo));

    }

    double hallar_tcea(double tir) {
        double tcea = Math.pow((1+tir),(360f/365f)) - 1f;
        return tcea;
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
            Fecha fecha_pago = fecha_de_pago_periodoN(n, d);//SOLO PARA MOSTRAR
            int numero_dias = dias_del_mes(fecha_de_pago_periodoN(n - 1, d));
            dp.setDias_de_mes(numero_dias);
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
            double redondeo_Envio = (double)Math.round(d.getEnvio() * 100d) / 100d;
            dp.setEnvio(redondeo_Envio);
            double redondeo_Poliza = (double)Math.round(d.getPoliza() * 100d) / 100d;
            dp.setPolisa_endosada(redondeo_Poliza);
            double redondeo_Cuota = (double)Math.round(x * 100d) / 100d;
            dp.setCuota(redondeo_Cuota);
            double cuota_a_pagar = redondeo_Cuota + redondeo_Poliza + redondeo_Envio;
            dp.setCuota_a_pagar(cuota_a_pagar);
            dp.setDia(fecha_pago.getDia());
            dp.setMes(fecha_pago.getMes());
            dp.setAnio(fecha_pago.getAnio());
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




    static int dias_del_mes(Fecha fecha)
    {
        Calendar cal = new GregorianCalendar(fecha.getAnio(), fecha.getMes(), 1);
        int days = cal.getActualMaximum(Calendar.DAY_OF_MONTH); // 28
        return days;
    }

    static Fecha fecha_de_pago_periodoN(int n, Datos_credito d)
    {
        double doce = 12;
        double mes_gg = d.getMes();
        double aux_anio = (mes_gg + n)/doce;
        double double_anio = Math.floor(aux_anio);
        int anio = (int)double_anio + d.getAnio();

        int mes;

        int aux_mes = (d.getMes() + n)% 12;
        if (aux_mes > 0)
        {
            mes = aux_mes;
        }
        else
        {
            mes = 12;
            anio--;
        }
        Fecha fecha_pago = new Fecha(d.getDia(),mes,anio);

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




    //VAC
    double valc(double c, ArrayList<Datos_periodo> lp, Datos_credito dc){



        double valor = 0;
        double dias = 0;
        for(int i = 0; i<lp.size();i++){
            dias = dias + lp.get(i).getDias_de_mes();
            valor = valor + (lp.get(i).getCuota_a_pagar())/(Math.pow((1f+c),(dias/365f)));

        }
        return valor;
    }

    double hallando_tir(int cont,  double a,double b, ArrayList<Datos_periodo> lp, Datos_credito dc){

        double c = (a+b)/2;
        double financiado = dc.getPrecio_vehiculo() - dc.getInicial();
        if(cont == 1000){
            return c;
        }
        else{
            if(valc(c, lp, dc)<financiado){
                return hallando_tir(cont+1, a, c, lp, dc);
            }
            else{
                return hallando_tir(cont+1, c, b, lp, dc);
            }
        }
    }


    double total_pagar(ArrayList<Datos_periodo> lp){
        double respuesta = 0;
        for(int i=0; i<lp.size();i++){
            respuesta = respuesta + lp.get(i).getCuota_a_pagar();
        }
        return respuesta;
    }















}



