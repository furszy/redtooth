package iop_sdk.blockchain;

import java.util.concurrent.TimeoutException;

/**
 * Created by mati on 17/12/16.
 */
public class NotConnectedPeersException extends Exception {

    public NotConnectedPeersException() {
    }

    public NotConnectedPeersException(String message) {
        super(message);
    }

    public NotConnectedPeersException(TimeoutException e) {
        super(e);
    }
}
