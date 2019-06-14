package com.andygeek.ccsimulator;

public class Periodo {
    private int numero;
    private double tes;
    private double saldo_inicial;
    private double interes;
    private double cuota;
    private double amortizacion;
    private double saldo_final;

    public int getNumero() {
        return numero;
    }

    public void setNumero(int numero) {
        this.numero = numero;
    }

    public double getTes() {
        return tes;
    }

    public void setTes(double tes) {
        this.tes = tes;
    }

    public double getSaldo_inicial() {
        return saldo_inicial;
    }

    public void setSaldo_inicial(double saldo_inicial) {
        this.saldo_inicial = saldo_inicial;
    }

    public double getInteres() {
        return interes;
    }

    public void setInteres(double interes) {
        this.interes = interes;
    }

    public double getCuota() {
        return cuota;
    }

    public void setCuota(double cuota) {
        this.cuota = cuota;
    }

    public double getAmortizacion() {
        return amortizacion;
    }

    public void setAmortizacion(double amortizacion) {
        this.amortizacion = amortizacion;
    }

    public double getSaldo_final() {
        return saldo_final;
    }

    public void setSaldo_final(double saldo_final) {
        this.saldo_final = saldo_final;
    }

    public void calcular(int periodos){

        interes = redondeo(saldo_inicial * tes);
        cuota = redondeo(saldo_inicial*((tes*Math.pow(1+tes, periodos-numero))/(Math.pow(1+tes, periodos-numero)-1)));
        amortizacion = redondeo(cuota - interes);
        saldo_final = redondeo(saldo_inicial - amortizacion);
    }

    public double redondeo(double numero){
        return (double)Math.round(numero * 100d) / 100d;
    }

}
