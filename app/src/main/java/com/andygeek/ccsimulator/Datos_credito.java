package com.andygeek.ccsimulator;

import android.os.Parcel;
import android.os.Parcelable;

public class Datos_credito implements Parcelable {
    double valor_vehiculo;
    int dia;
    int mes;
    int anio;
    int numero_periodos;
    double tasa_efectiva_anual;
    double envio;
    boolean individual;
    double tasa_seguro_degravamen_mensual;   // porcentaje -> Individual
    double monto_seguro_degravamen_endosado; // fijo       -> Endosado
    double cuota_inicial;

    protected Datos_credito(Parcel in) {
        valor_vehiculo = in.readDouble();
        dia = in.readInt();
        mes = in.readInt();
        anio = in.readInt();
        numero_periodos = in.readInt();
        tasa_efectiva_anual = in.readDouble();
        envio = in.readDouble();
        individual = in.readByte() != 0x00;
        tasa_seguro_degravamen_mensual = in.readDouble();
        monto_seguro_degravamen_endosado = in.readDouble();
        cuota_inicial = in.readDouble();
    }

    public Datos_credito(){

    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeDouble(valor_vehiculo);
        dest.writeInt(dia);
        dest.writeInt(mes);
        dest.writeInt(anio);
        dest.writeInt(numero_periodos);
        dest.writeDouble(tasa_efectiva_anual);
        dest.writeDouble(envio);
        dest.writeByte((byte) (individual ? 0x01 : 0x00));
        dest.writeDouble(tasa_seguro_degravamen_mensual);
        dest.writeDouble(monto_seguro_degravamen_endosado);
        dest.writeDouble(cuota_inicial);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Datos_credito> CREATOR = new Parcelable.Creator<Datos_credito>() {
        @Override
        public Datos_credito createFromParcel(Parcel in) {
            return new Datos_credito(in);
        }

        @Override
        public Datos_credito[] newArray(int size) {
            return new Datos_credito[size];
        }
    };


    public double getValor_vehiculo() {
        return valor_vehiculo;
    }

    public void setValor_vehiculo(double valor_vehiculo) {
        this.valor_vehiculo = valor_vehiculo;
    }

    public int getDia() {
        return dia;
    }

    public void setDia(int dia) {
        this.dia = dia;
    }

    public int getMes() {
        return mes;
    }

    public void setMes(int mes) {
        this.mes = mes;
    }

    public int getAnio() {
        return anio;
    }

    public void setAnio(int anio) {
        this.anio = anio;
    }

    public int getNumero_periodos() {
        return numero_periodos;
    }

    public void setNumero_periodos(int numero_periodos) {
        this.numero_periodos = numero_periodos;
    }

    public double getTasa_efectiva_anual() {
        return tasa_efectiva_anual;
    }

    public void setTasa_efectiva_anual(double tasa_efectiva_anual) {
        this.tasa_efectiva_anual = tasa_efectiva_anual;
    }

    public double getEnvio() {
        return envio;
    }

    public void setEnvio(double envio) {
        this.envio = envio;
    }

    public boolean isIndividual() {
        return individual;
    }

    public void setIndividual(boolean individual) {
        this.individual = individual;
    }

    public double getTasa_seguro_degravamen_mensual() {
        return tasa_seguro_degravamen_mensual;
    }

    public void setTasa_seguro_degravamen_mensual(double tasa_seguro_degravamen_mensual) {
        this.tasa_seguro_degravamen_mensual = tasa_seguro_degravamen_mensual;
    }

    public double getMonto_seguro_degravamen_endosado() {
        return monto_seguro_degravamen_endosado;
    }

    public void setMonto_seguro_degravamen_endosado(double monto_seguro_degravamen_endosado) {
        this.monto_seguro_degravamen_endosado = monto_seguro_degravamen_endosado;
    }

    public double getCuota_inicial() {
        return cuota_inicial;
    }

    public void setCuota_inicial(double cuota_inicial) {
        this.cuota_inicial = cuota_inicial;
    }

    public static Creator<Datos_credito> getCREATOR() {
        return CREATOR;
    }
}