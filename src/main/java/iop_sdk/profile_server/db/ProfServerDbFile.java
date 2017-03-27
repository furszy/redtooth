package iop_sdk.profile_server.db;

import java.util.HashMap;
import java.util.Map;

import iop_sdk.profile_server.engine.ProfSerDb;

/**
 * Created by mati on 07/02/17.
 */

public class ProfServerDbFile implements ProfSerDb {

    private Map<String,String> registered;

    public ProfServerDbFile() {
        this.registered = new HashMap<>();
    }

    @Override
    public void setProfileRegistered(String host, String profilePublicKey) {
        registered.put(profilePublicKey,host);
    }

    @Override
    public boolean isRegistered(String host, String profilePublicKey) {
        return (registered.containsKey(profilePublicKey)) && registered.get(profilePublicKey).equals(host);
    }
}
