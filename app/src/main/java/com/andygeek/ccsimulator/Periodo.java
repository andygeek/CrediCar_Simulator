package com.andygeek.ccsimulator;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class Periodo {

    private int dia;
    private int mes;
    private int anio;
    private double interes;                             //si se calcula aqui
    private double amortizacion;                        //si se calcula aqui
    private double cuota_total;                         //si se calcula aqui
    private double tasa_ajustada_al_plazo;              //Si se calcula aqui
    private double tasa_degravamen_ajustada_al_plazo;   //Si se calcula aqui
    private double seguro_degravamen;                   //Si se calcula aqui
    private double tasa_seguro_vehicular_ajustada_plazo;//Si se calcula aqui
    private double seguro_vehicular_mensual;            //Si se calcula aqui
    private double saldo_final;                         //si se calcula aqui
    private double cuota;                               //Si se calcula aqui
    private double interes_sobre_financiamiento;        //Si se calcula aqui

    private double saldo_inicial;                       //No se calcula aqui
    private double valor_vehiculo;                      //No se calcula aqui
    private double cuota_inicial;                       //no se calcula aqiio
    private double monto_prestamo;                      //no se calcula aqui
    private double tasa_nominal_anual;                  //No se calcula aqui
    private double tasa_degravamen_anual;               //No se calcula aqui
    private double tasa_seguro_vehicular_anual;         //No se calcula aqui
    private double envio;


    public Periodo(double saldo_inicial, double tasa_nominal_anual, double tasa_degravamen_anual, double tasa_seguro_vehicular_anual, double valor_vehiculo, double cuota_inicial, double envio, int dia, int mes, int anio){
        this.saldo_inicial = saldo_inicial;
        this.tasa_nominal_anual = tasa_nominal_anual;
        this.tasa_degravamen_anual = tasa_degravamen_anual;
        this.tasa_seguro_vehicular_anual = tasa_seguro_vehicular_anual;
        this.valor_vehiculo = valor_vehiculo;
        this.cuota_inicial = cuota_inicial;
        this.envio = envio;
    }

    public void calcular(int periodos){

        tasa_ajustada_al_plazo = (tasa_nominal_anual/365)*numero_dias_mes(mes, anio);
        interes = redondeo(saldo_inicial * tasa_ajustada_al_plazo);
        tasa_degravamen_ajustada_al_plazo = (tasa_degravamen_anual/365)*numero_dias_mes(mes, anio);
        seguro_degravamen = redondeo(saldo_inicial*tasa_degravamen_ajustada_al_plazo);
        tasa_seguro_vehicular_ajustada_plazo = (tasa_seguro_vehicular_anual*numero_dias_mes(mes, anio))/365;
        seguro_vehicular_mensual = redondeo(valor_vehiculo*tasa_seguro_vehicular_ajustada_plazo);
        monto_prestamo = valor_vehiculo - cuota_inicial;
        interes_sobre_financiamiento = redondeo(monto_prestamo*tasa_ajustada_al_plazo);
        cuota = monto_prestamo*((tasa_ajustada_al_plazo)/(1-(Math.pow(1+tasa_ajustada_al_plazo, - periodos))));
        amortizacion = cuota - interes_sobre_financiamiento;
        cuota_total = interes + seguro_degravamen + seguro_vehicular_mensual + amortizacion + envio;
        saldo_final = saldo_inicial - amortizacion;
    }
    public double redondeo(double numero){
        return (double)Math.round(numero * 100d) / 100d;
    }

    public int numero_dias_mes(int mes, int anio) {
        Calendar cal = new GregorianCalendar(anio, mes-1, 1);
        int days = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
        return days;
    }

    public int getDia() {
        return dia;
    }

    public int getMes() {
        return mes;
    }

    public int getAnio() {
        return anio;
    }

    public double getInteres() {
        return interes;
    }

    public double getAmortizacion() {
        return amortizacion;
    }

    public double getCuota_total() {
        return cuota_total;
    }

    public double getTasa_ajustada_al_plazo() {
        return tasa_ajustada_al_plazo;
    }

    public double getTasa_degravamen_ajustada_al_plazo() {
        return tasa_degravamen_ajustada_al_plazo;
    }

    public double getSeguro_degravamen() {
        return seguro_degravamen;
    }

    public double getTasa_seguro_vehicular_ajustada_plazo() {
        return tasa_seguro_vehicular_ajustada_plazo;
    }

    public double getSeguro_vehicular_mensual() {
        return seguro_vehicular_mensual;
    }

    public double getSaldo_final() {
        return saldo_final;
    }

    public double getCuota() {
        return cuota;
    }

    public double getInteres_sobre_financiamiento() {
        return interes_sobre_financiamiento;
    }

    public double getSaldo_inicial() {
        return saldo_inicial;
    }

    public double getValor_vehiculo() {
        return valor_vehiculo;
    }

    public double getCuota_inicial() {
        return cuota_inicial;
    }

    public double getMonto_prestamo() {
        return monto_prestamo;
    }

    public double getTasa_nominal_anual() {
        return tasa_nominal_anual;
    }

    public double getTasa_degravamen_anual() {
        return tasa_degravamen_anual;
    }

    public double getTasa_seguro_vehicular_anual() {
        return tasa_seguro_vehicular_anual;
    }
}
