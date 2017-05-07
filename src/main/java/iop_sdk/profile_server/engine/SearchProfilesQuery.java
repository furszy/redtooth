package iop_sdk.profile_server.engine;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import iop_sdk.profile_server.protocol.IopProfileServer;

import static iop_sdk.profile_server.protocol.MessageFactory.NO_LOCATION;

/**
 * Created by mati on 30/03/17.
 *
 * todo: this class will saved the search query con las siguientes busquedas de datos.
 *
 */

public class SearchProfilesQuery {

    private String id;

    private boolean onlyHostedProfiles;
    private boolean includeThumbnailImages;
    private String profileType;
    private String profileName;
    private int maxResponseRecordCount;
    private int maxTotalRecordCount;
    private int latitude;
    private int longitude;
    private int radius;
    private String extraData;

    /** Index of the result to retrieve in the next search query */
    private int recordIndex;
    /** Number of results to obtain in the next part search query */
    private int recordCount;


    private int totalRecordCountServer;
    /** Data cached for each request */
    private Map<Integer,List<IopProfileServer.IdentityNetworkProfileInformation>> cacheData;
    /**  */
    private List<String> coveredServers;
    /** Start with zero in the first search query, then for each partSearchRequest i will add one */
    private int lastRecordIndex;
    private int lastRecordCount;


    public SearchProfilesQuery(String profileType, String profileName, int maxResponseRecordCount, int maxTotalRecordCount) {
        this(false,false,profileType,profileName,maxResponseRecordCount,maxTotalRecordCount,NO_LOCATION,NO_LOCATION,0,null);
    }

    public SearchProfilesQuery(boolean onlyHostedProfiles, boolean includeThumbnailImages, String profileType, String profileName, int maxResponseRecordCount, int maxTotalRecordCount, int latitude, int longitude, int radius, String extraData) {
        this.onlyHostedProfiles = onlyHostedProfiles;
        this.includeThumbnailImages = includeThumbnailImages;
        this.profileType = profileType;
        this.profileName = profileName;
        this.maxResponseRecordCount = maxResponseRecordCount;
        this.maxTotalRecordCount = maxTotalRecordCount;
        this.latitude = latitude;
        this.longitude = longitude;
        this.radius = radius;
        this.extraData = extraData;
        this.cacheData = new HashMap<>();
    }

    public boolean isOnlyHostedProfiles() {
        return onlyHostedProfiles;
    }

    public boolean isIncludeThumbnailImages() {
        return includeThumbnailImages;
    }

    public String getProfileType() {
        return profileType;
    }

    public String getProfileName() {
        return profileName;
    }

    public int getMaxResponseRecordCount() {
        return maxResponseRecordCount;
    }

    public int getMaxTotalRecordCount() {
        return maxTotalRecordCount;
    }

    public int getLatitude() {
        return latitude;
    }

    public int getLongitude() {
        return longitude;
    }

    public int getRadius() {
        return radius;
    }

    public String getExtraData() {
        return extraData;
    }

    public int getRecordIndex() {
        return recordIndex;
    }

    public int getRecordCount() {
        return recordCount;
    }

    public Map<Integer, List<IopProfileServer.IdentityNetworkProfileInformation>> getCacheData() {
        return cacheData;
    }

    public int getLastRecordIndex() {
        return lastRecordIndex;
    }

    public int getLastRecordCount() {
        return lastRecordCount;
    }

    public void setRecordIndex(int recordIndex) {
        this.recordIndex = recordIndex;
    }

    public void setRecordCount(int recordCount) {
        this.recordCount = recordCount;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void addListToChache(int index,List<IopProfileServer.IdentityNetworkProfileInformation> list) {
        this.cacheData.put(index,list);
    }

    public List<IopProfileServer.IdentityNetworkProfileInformation> getListFromCache(int index){
        return this.cacheData.get(index);
    }

    public void setLastRecordIndex(int lastRecordIndex) {
        this.lastRecordIndex = lastRecordIndex;
    }

    public void setLastRecordCount(int lastRecordCount) {
        this.lastRecordCount = lastRecordCount;
    }
}
