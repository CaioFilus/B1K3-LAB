package com.npdeas.b1k3labapp.Route;

/**
 * Created by NPDEAS on 6/15/2018.
 */

public enum ComunicationType {
    READ(0),WRITE(1);
    private final int communicationType;
    ComunicationType(int communicationType){
        this.communicationType = communicationType;
    }
}
