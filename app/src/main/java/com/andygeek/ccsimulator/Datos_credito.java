package com.andygeek.ccsimulator;

import android.os.Parcel;
import android.os.Parcelable;

public class Datos_credito implements Parcelable {
    private Double prestamo;
    private Double tes;
    private int periodos;


    public Double getPrestamo() {
        return prestamo;
    }

    public void setPrestamo(Double prestamo) {
        this.prestamo = prestamo;
    }

    public Double getTes() {
        return tes;
    }

    public void setTes(Double tes) {
        this.tes = tes;
    }

    public int getPeriodos() {
        return periodos;
    }

    public void setPeriodos(int periodos) {
        this.periodos = periodos;
    }

    public Datos_credito(Double prestamo, Double tes, int periodos) {
        this.prestamo = prestamo;
        this.tes = tes;
        this.periodos = periodos;
    }

    protected Datos_credito(Parcel in) {
        prestamo = in.readByte() == 0x00 ? null : in.readDouble();
        tes = in.readByte() == 0x00 ? null : in.readDouble();
        periodos = in.readInt();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (prestamo == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeDouble(prestamo);
        }
        if (tes == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeDouble(tes);
        }
        dest.writeInt(periodos);
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
}