package iop_sdk.governance.propose;

/**
 * Created by mati on 02/02/17.
 */
public class CantCompleteProposalMaxTransactionExcededException extends Exception {
    public CantCompleteProposalMaxTransactionExcededException(String message,Exception e) {
        super(message,e);
    }
}
