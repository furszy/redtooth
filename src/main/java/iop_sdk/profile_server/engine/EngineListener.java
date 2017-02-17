package iop_sdk.profile_server.engine;

import java.util.List;

import iop_sdk.profile_server.model.Profile;
import iop_sdk.profile_server.protocol.IopProfileServer;

/**
 * Created by mati on 15/02/17.
 */

public interface EngineListener {

    void onCheckInCompleted(Profile profile);

//    void onProfileSearchReceived(List<IopProfileServer.IdentityNetworkProfileInformation> profileInformationList);


}
