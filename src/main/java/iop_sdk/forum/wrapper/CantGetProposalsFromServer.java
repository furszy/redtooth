package iop_sdk.forum.wrapper;

/**
 * Created by mati on 23/12/16.
 */
public class CantGetProposalsFromServer extends Throwable {
    public CantGetProposalsFromServer(String s, Exception e) {
        super(s,e);
    }

    public CantGetProposalsFromServer(String s) {
        super(s);
    }
}
