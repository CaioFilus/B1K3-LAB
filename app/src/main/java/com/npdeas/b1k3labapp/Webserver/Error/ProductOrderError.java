package com.npdeas.b1k3labapp.Webserver.Error;

public enum ProductOrderError implements ErrorInterface {
    NO_ERROR(0),
    ERROR_ORDER_NOT_FOUND(50),
    ERROR_PRODUCT_ORDER_NOT_FOUND(51),
    ERROR_PRODUCT_ENDED(52);

    public int error;

    ProductOrderError(int error) {
        this.error = error;
    }


    @Override
    public int getInt() {
        return 0;
    }
}
