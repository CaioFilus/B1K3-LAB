package com.npdeas.b1k3labapp.Webserver.Error;

public enum ConnError implements ErrorInterface{
    NO_ERROR(0),

    ERROR_NOT_A_JSON (1),
    ERROR_MISSING_VALUE (2),
    ERROR_INTERNET_ERROR(3),
    ERROR_ON_SERVER_CONN(4),
    ERROR_ON_JSON(5),

    ERROR_ON_DB (10);

    public int value;

     ConnError(int error){
        this.value = error;
    }

    public boolean Compare(int i){return value == i;}

    public static Enum getValue(int status) {
        ConnError[] As = ConnError.values();
        for(int i = 0; i < As.length; i++)
        {
            if(As[i].Compare(status))
                return As[i];
        }
        return null;
    }

    @Override
    public int getInt() {
        return value;
    }
}
