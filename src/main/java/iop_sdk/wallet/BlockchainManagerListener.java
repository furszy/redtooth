package iop_sdk.wallet;

import org.bitcoinj.core.PeerGroup;

import java.util.Set;

import iop_sdk.wallet.utils.BlockchainState;

/**
 * Created by mati on 19/12/16.
 */
public interface BlockchainManagerListener {

    void peerGroupInitialized(PeerGroup peerGroup);

    void onBlockchainOff(Set<BlockchainState.Impediment> impediments);

    void checkStart();

    void checkEnd();

}
