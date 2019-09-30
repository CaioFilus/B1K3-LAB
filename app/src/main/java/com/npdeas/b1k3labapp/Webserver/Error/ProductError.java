package com.npdeas.b1k3labapp.Webserver.Error;

public enum ProductError implements ErrorInterface {
    NO_ERROR(0),
    ERROR_PRODUCT_ALREADY_EXIST (40);


    public int error;

    ProductError(int error){
        this.error = error;
    }
    @Override
    public int getInt() {
        return error;
    }

}
