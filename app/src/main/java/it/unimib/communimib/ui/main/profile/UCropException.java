package it.unimib.communimib.ui.main.profile;

public class UCropException extends RuntimeException{

    // Costruttore senza argomenti
    public UCropException() {
        super();
    }

    // Costruttore con messaggio di errore
    public UCropException(String message) {
        super(message);
    }

    // Costruttore con messaggio di errore e causa
    public UCropException(String message, Throwable cause) {
        super(message, cause);
    }

    // Costruttore con causa
    public UCropException(Throwable cause) {
        super(cause);
    }
}
