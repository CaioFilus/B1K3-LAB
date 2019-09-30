package com.npdeas.b1k3labapp.Webserver.Error;

public class Error {

    private ErrorInterface[][] errorEnuns = new ErrorInterface[][]{
            ConnError.values(), LoginError.values(), ProductError.values(), ProductOrderError.values()};
    private ErrorInterface error = null;

    public static final int NO_ERROR = 0;

    public Error(int error) {
        for (ErrorInterface[] Enum : errorEnuns) {
            for (ErrorInterface errors : Enum) {
                if (errors.getInt() == error) {
                    this.error = errors;
                    return;
                }
            }
        }
    }

    public boolean isErrorFromMyClass(Class classe) {
        if (classe.getName().startsWith(error.getClass().getName())) {
            return true;
        }
        return false;
    }

    public Enum getError() {
        return (Enum) error;
    }

    public int getIntError() {
        return error.getInt();
    }

    public static String getErrorString(Enum error) {
        if (error.ordinal() == NO_ERROR) {
            return "Sem Erros";
        }
        if (error instanceof ConnError) {
            ConnError connError = (ConnError) error;
            switch (connError) {
                case NO_ERROR:
                    return "Sem Erros";
                case ERROR_INTERNET_ERROR:
                    return "Erro na conexão";
                case ERROR_NOT_A_JSON:
                    return "Não é um Json";
                case ERROR_ON_DB:
                    return "Erro no banco de dados";
                case ERROR_MISSING_VALUE:
                    return "Faltando Valores";
                case ERROR_ON_SERVER_CONN:
                    return "Não foi possivel se conectar ao servidor";
                case ERROR_ON_JSON:
                    return "Erro no JSON";
            }
        } else if (error instanceof LoginError) {
            LoginError loginError = (LoginError) error;
            switch (loginError) {
                case ERROR_USER_NOT_FOUND:
                    return "Usuario não encontrado";
                case ERROR_ALREADY_EXIST:
                    return "Usuario já Existe";
                case ERROR_USER_OR_PASS_INCORRECT:
                    return "Usuario ou senha incorretos";
                case ERROR_USER_NOT_LOGGED:
                    return "Usuario não Loggado";
                case ERROR_NOT_A_ADMIN:
                    return "Você não é um Adimistrador";
            }
        } else if (error instanceof RouteError) {
            RouteError routeError = (RouteError) error;
            switch (routeError) {
                case ERROR_ROUTE_ALREADY_SENDED:
                    return "A rota já esta completa";
                case ERROR_DATE_FORMAT_INCORRECT:
                    return "Formato da data esta incorreto";
                case ERROR_ROUTE_NOT_FULL_SENDED:
                    return "Rota não esta completa";
            }
        } else if (error instanceof ProductOrderError) {
            ProductOrderError routeError = (ProductOrderError) error;
            switch (routeError) {
                case ERROR_ORDER_NOT_FOUND:
                    return "Pedido não encontrado";
                case ERROR_PRODUCT_ORDER_NOT_FOUND:
                    return "Produto nao encontrado";
            }
        }
        return "";
    }

}
