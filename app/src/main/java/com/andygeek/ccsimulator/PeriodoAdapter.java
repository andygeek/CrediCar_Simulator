package com.andygeek.ccsimulator;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class PeriodoAdapter extends BaseExpandableListAdapter {


    private ArrayList<Periodo> periodos;
    private Context context;

    public PeriodoAdapter(ArrayList<Periodo> periodos, Context context) {
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
        TextView tv_Amortizacion = convertView.findViewById(R.id.tv_Amortizacion);

        tv_Numero.setText(String.valueOf(periodos.get(groupPosition).getNumero() + 1));
        tv_Amortizacion.setText(String.valueOf(periodos.get(groupPosition).getAmortizacion()));

        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        convertView = LayoutInflater.from(context).inflate(R.layout.item_detalle_periodo, null);
        TextView tv_SaldoInicial = convertView.findViewById(R.id.tv_SaldoInicial);
        TextView tv_SaldoFinal = convertView.findViewById(R.id.tv_SaldoFinal);

        tv_SaldoInicial.setText(String.valueOf(periodos.get(groupPosition).getSaldo_inicial()));
        tv_SaldoFinal.setText(String.valueOf(periodos.get(groupPosition).getSaldo_final()));

        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }
}
