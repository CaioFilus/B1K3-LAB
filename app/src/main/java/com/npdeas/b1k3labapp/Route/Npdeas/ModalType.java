package com.npdeas.b1k3labapp.Route.Npdeas;

import com.npdeas.b1k3labapp.R;

/**
 * Created by NPDEAS on 5/30/2018.
 */

public enum ModalType {

    NONE(0, "", 0),
    FEET(1, "A pé", R.drawable.ic_corpo),
    BIKE(2, "Bicicleta", R.drawable.ic_bike),
    MOTOCICLE(3, "Moto", 0),
    CAR(4, "Carro", R.drawable.ic_carro),
    BUS(5, "Onibus", R.drawable.ic_bus);

    public int value;
    public String name;
    public int imgId;

    ModalType(int valor, String name, int imgId) {
        this.value = valor;
        this.name = name;
        this.imgId = imgId;
    }

    public int getValor() {
        return value;
    }

    public boolean compare(int i) {
        return value == i;
    }

    public static ModalType getValue(int status) {
        ModalType[] as = ModalType.values();
        for (int i = 0; i < as.length; i++) {
            if (as[i].compare(status))
                return as[i];
        }
        return null;
    }
}
