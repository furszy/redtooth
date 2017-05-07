package iop_sdk.profile_server.engine.futures;

import java.util.List;

import iop_sdk.profile_server.engine.SearchProfilesQuery;
import iop_sdk.profile_server.engine.listeners.ProfSerMsgListener;
import iop_sdk.profile_server.engine.listeners.ProfSerPartSearchListener;
import iop_sdk.profile_server.protocol.IopProfileServer;

/**
 * Created by mati on 31/03/17.
 */

public class SearchMessageFuture <O extends List<IopProfileServer.IdentityNetworkProfileInformation>> extends BaseMsgFuture<O> implements ProfSerMsgListener<O> {

    private SearchProfilesQuery searchProfilesQuery;

    public SearchMessageFuture(SearchProfilesQuery searchProfilesQuery) {
        this.searchProfilesQuery = searchProfilesQuery;
    }

    @Override
    public void onMessageReceive(int messageId, O message) {
        this.messageId = messageId;
        this.searchProfilesQuery.addListToChache(0,message);
        queue.offer(message);
    }

    @Override
    public void onMsgFail(int messageId, int statusValue, String details) {
        this.status = statusValue;
        this.statusDetail = details;
        queue.offer(null);
    }

}
