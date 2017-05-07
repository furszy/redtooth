package iop_sdk.profile_server.engine;

import iop_sdk.profile_server.engine.futures.BaseMsgFuture;
import iop_sdk.profile_server.engine.listeners.ProfSerMsgListenerBase;

/**
 * Created by mati on 02/04/17.
 */

public class MsgSendRequest {

    private int msgId;
    private ProfSerMsgListenerBase listener;

    public MsgSendRequest(int msgId) {
        this.msgId = msgId;
    }

    public void send(){

    }


}
