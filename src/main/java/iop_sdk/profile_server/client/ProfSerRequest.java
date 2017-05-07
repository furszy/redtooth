package iop_sdk.profile_server.client;


import iop_sdk.profile_server.CantConnectException;
import iop_sdk.profile_server.CantSendMessageException;

/**
 * Created by mati on 02/04/17.
 */
public interface ProfSerRequest {

    int getMessageId();

    void send() throws CantConnectException, CantSendMessageException;

}
