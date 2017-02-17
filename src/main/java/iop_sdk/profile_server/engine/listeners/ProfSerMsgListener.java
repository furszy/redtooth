package iop_sdk.profile_server.engine.listeners;

/**
 * Created by mati on 17/02/17.
 */

public interface ProfSerMsgListener<O> {

    void onMessageReceive(int messageId, O message);

}
