package iop_sdk.profile_server.engine.listeners;

/**
 * Created by mati on 30/03/17.
 */

public interface ProfSerPartSearchListener<O> extends ProfSerMsgListenerBase {

    void onMessageReceive(int messageId, O message,int recordIndex,int recordCount);

}
