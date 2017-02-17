package iop_sdk.governance.propose;


/**
 * Created by mati on 29/12/16.
 */

public class ProposalUtil {


    /**
     * Return time in minutes
     *
     * @param proposal
     * @return
     */
    public static double getEstimatedTimeToContractExecution(Proposal proposal){
        long startBlockHeight = proposal.getStartBlock()+1000;
        long endBlock = startBlockHeight+proposal.getEndBlock();
        return (endBlock-startBlockHeight)*10;
    }

}
