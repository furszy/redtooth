package iop_sdk.profile_server;

import java.util.List;

import iop_sdk.profile_server.engine.SearchProfilesQuery;
import iop_sdk.profile_server.engine.futures.MsgListenerFuture;
import iop_sdk.profile_server.engine.futures.SearchMessageFuture;
import iop_sdk.profile_server.engine.futures.SubsequentSearchMsgListenerFuture;
import iop_sdk.profile_server.engine.listeners.ProfSerMsgListener;
import iop_sdk.profile_server.protocol.IopProfileServer;

/**
 * Created by mati on 22/11/16.
 */

public interface ModuleProfileServer {



    int registerProfile(Signer signer, String name, byte[] img, int latitude, int longitude, String extraData, ProfSerMsgListener listener) throws Exception;

    int updateProfile(Signer signer, byte[] version, String name, byte[] img, int latitude, int longitude, String extraData, ProfSerMsgListener msgListener) throws Exception;

    int updateProfileExtraData(Signer signer, String extraData) throws Exception;

    boolean isIdentityCreated();

    /* Search queries **/

    void searchProfileByName(String name, ProfSerMsgListener<List<IopProfileServer.IdentityNetworkProfileInformation>> listener);

    /**  */
    SearchMessageFuture<List<IopProfileServer.IdentityNetworkProfileInformation>> searchProfiles(SearchProfilesQuery searchProfilesQuery);

    SubsequentSearchMsgListenerFuture<List<IopProfileServer.IdentityNetworkProfileInformation>> searchSubsequentsProfiles(SearchProfilesQuery searchProfilesQuery);
}
