package com.npdeas.b1k3labapp.Webserver.Error;

public enum RouteError implements ErrorInterface{

    NO_ERROR(0),
    ERROR_ROUTE_ALREADY_EXIST (40),
    ERROR_DATE_FORMAT_INCORRECT (41),
    ERROR_ROUTE_ALREADY_SENDED (42),
    ERROR_ROUTE_NOT_FULL_SENDED (43);

    int error;
    RouteError(int error){
        this.error =error;
    }

    @Override
    public int getInt() {
        return error;
    }
}
