package com.npdeas.b1k3labapp.Webserver.Error;

public enum LoginError implements ErrorInterface {

    NO_ERROR(0),
    ERROR_USER_NOT_FOUND(20),
    ERROR_ALREADY_EXIST(21),
    ERROR_USER_OR_PASS_INCORRECT(22),
    ERROR_USER_NOT_LOGGED(23),
    ERROR_MISSING_TOKEM(24),
    ERROR_INVALID_TOKEN(25),
    ERROR_NOT_A_ADMIN(26)
    ;

    public int error;

    LoginError(int error) {
        this.error = error;
    }

    public static Enum getValue(int status) {
        LoginError[] As = LoginError.values();
        for (int i = 0; i < As.length; i++) {
            if (As[i].error == status)
                return As[i];
        }
        return null;
    }

    @Override
    public int getInt() {
        return error;
    }
}


