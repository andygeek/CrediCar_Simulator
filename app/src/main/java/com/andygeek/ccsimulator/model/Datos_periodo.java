package com.andygeek.ccsimulator.model;

import java.util.Date;

public class Datos_periodo {
    private Date fecha_pago;
    private double saldo_anterior;
    private double saldo_actual;
    private double tasa_ajustada_al_plazo;
    private double tasa_seguro_degravamen_ajustado;
    private double tasa_seguro_vehicular_ajustado;
    private double seguro_degravamen;
    private double seguro_vehicular;
    private double interes;
    private double amortizacion;
    private double portes;

    public double getPortes() {
        return portes;
    }

    public void setPortes(double portes) {
        this.portes = portes;
    }

    public Date getFecha_pago() {
        return fecha_pago;
    }

    public void setFecha_pago(Date fecha_pago) {
        this.fecha_pago = fecha_pago;
    }

    public double getSaldo_anterior() {
        return saldo_anterior;
    }

    public void setSaldo_anterior(double saldo_anterior) {
        this.saldo_anterior = saldo_anterior;
    }

    public double getSaldo_actual() {
        return saldo_actual;
    }

    public void setSaldo_actual(double saldo_actual) {
        this.saldo_actual = saldo_actual;
    }

    public double getTasa_ajustada_al_plazo() {
        return tasa_ajustada_al_plazo;
    }

    public void setTasa_ajustada_al_plazo(double tasa_ajustada_al_plazo) {
        this.tasa_ajustada_al_plazo = tasa_ajustada_al_plazo;
    }

    public double getTasa_seguro_degravamen_ajustado() {
        return tasa_seguro_degravamen_ajustado;
    }

    public void setTasa_seguro_degravamen_ajustado(double tasa_seguro_degravamen_ajustado) {
        this.tasa_seguro_degravamen_ajustado = tasa_seguro_degravamen_ajustado;
    }

    public double getTasa_seguro_vehicular_ajustado() {
        return tasa_seguro_vehicular_ajustado;
    }

    public void setTasa_seguro_vehicular_ajustado(double tasa_seguro_vehicular_ajustado) {
        this.tasa_seguro_vehicular_ajustado = tasa_seguro_vehicular_ajustado;
    }

    public double getSeguro_degravamen() {
        return seguro_degravamen;
    }

    public void setSeguro_degravamen(double seguro_degravamen) {
        this.seguro_degravamen = seguro_degravamen;
    }

    public double getSeguro_vehicular() {
        return seguro_vehicular;
    }

    public void setSeguro_vehicular(double seguro_vehicular) {
        this.seguro_vehicular = seguro_vehicular;
    }

    public double getInteres() {
        return interes;
    }

    public void setInteres(double interes) {
        this.interes = interes;
    }

    public double getAmortizacion() {
        return amortizacion;
    }

    public void setAmortizacion(double amortizacion) {
        this.amortizacion = amortizacion;
    }
}
