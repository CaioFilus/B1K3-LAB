package com.npdeas.b1k3labapp.Npdeas;

import com.npdeas.b1k3labapp.R;

/**
 * Created by NPDEAS on 5/30/2018.
 */

public enum ModalType {
    FEET(R.id.fab4),BIKE(R.id.fab4),CAR(R.id.fab4),BUS(R.id.fab4);
    public int valor;
    ModalType(int valor){
        this.valor = valor;
    }

    public int getValor() {
        return valor;
    }
}
