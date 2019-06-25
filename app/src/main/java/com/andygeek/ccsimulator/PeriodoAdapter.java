package com.andygeek.ccsimulator;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.andygeek.ccsimulator.model.Datos_periodo;

import java.util.ArrayList;

public class PeriodoAdapter extends BaseExpandableListAdapter {


    private ArrayList<Datos_periodo> periodos;
    private Context context;

    public PeriodoAdapter(ArrayList<Datos_periodo> periodos, Context context) {
        this.periodos = periodos;
        this.context = context;
    }

    @Override
    public int getGroupCount() {
        return periodos.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return 1;
    }

    @Override
    public Object getGroup(int groupPosition) {
        return periodos.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return null;
    }

    @Override
    public long getGroupId(int groupPosition) {
        return 0;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        convertView = LayoutInflater.from(context).inflate(R.layout.item_periodo, null);
        TextView tv_Numero = convertView.findViewById(R.id.tv_Numero);
        TextView tv_Fecha = convertView.findViewById(R.id.tv_Fecha);
        TextView tv_Saldo = convertView.findViewById(R.id.tv_Saldo);

        tv_Numero.setText(String.valueOf(groupPosition + 1));
        tv_Fecha.setText(String.valueOf(periodos.get(groupPosition).getFecha_pago()));
        tv_Saldo.setText(String.valueOf(periodos.get(groupPosition).getSaldo_actual()));

        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        convertView = LayoutInflater.from(context).inflate(R.layout.item_detalle_periodo, null);
        TextView tv_dInteres = convertView.findViewById(R.id.tv_dInteres);
        TextView tv_dSeguroDegrabamen = convertView.findViewById(R.id.tv_dSeguroDegrabamen);
        TextView tv_dSeguroVehicular = convertView.findViewById(R.id.tv_dSeguroVehicular);
        TextView tv_dAmortizacion = convertView.findViewById(R.id.tv_dAmortizacion);
        TextView tv_dPortes = convertView.findViewById(R.id.tv_dPortes);
        TextView tv_dCuota = convertView.findViewById(R.id.tv_dCuota);

        tv_dInteres.setText(String.valueOf(periodos.get(groupPosition).getInteres()));
        tv_dSeguroDegrabamen.setText(String.valueOf(periodos.get(groupPosition).getSeguro_degravamen()));
        tv_dSeguroVehicular.setText(String.valueOf(periodos.get(groupPosition).getSeguro_vehicular()));
        tv_dAmortizacion.setText(String.valueOf(periodos.get(groupPosition).getAmortizacion()));
        tv_dPortes.setText(String.valueOf(periodos.get(groupPosition).getPortes()));
        tv_dCuota.setText(String.valueOf(periodos.get(groupPosition).getCuota_a_pagar()));

        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }
}
