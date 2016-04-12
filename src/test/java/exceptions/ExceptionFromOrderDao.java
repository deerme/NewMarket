package exceptions;

/**
 * Created by pizmak on 2016-04-01.
 */
public class ExceptionFromOrderDao extends RuntimeException {
    public ExceptionFromOrderDao(String message) {
        super(message);
    }
}
