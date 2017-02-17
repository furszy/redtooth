package iop_sdk.profile_server;

/**
 * Created by mati on 22/11/16.
 */

public interface ModuleProfileServer {


    int registerReqeust(Signer signer, String name, byte[] img, int latitude, int longitude, String extraData) throws Exception;

    int updateProfileRequest(Signer signer, byte[] version, String name, byte[] img, int latitude, int longitude, String extraData) throws Exception;

    int updateExtraData(Signer signer, String extraData) throws Exception;

    boolean isIdentityCreated();
}
