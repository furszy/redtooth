package iop_sdk.profile_server.engine;

import iop_sdk.profile_server.CantConnectException;
import iop_sdk.profile_server.CantSendMessageException;
import iop_sdk.profile_server.client.ProfSerRequest;

/**
 * Created by mati on 02/04/17.
 */
public abstract class ProfSerRequestImp implements ProfSerRequest{

    private int msgId;

    public ProfSerRequestImp(int msgId) {
        this.msgId = msgId;
    }

    @Override
    public int getMessageId() {
        return msgId;
    }

    public abstract void send() throws CantConnectException, CantSendMessageException;
}
