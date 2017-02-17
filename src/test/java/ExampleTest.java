import org.bitcoinj.core.Context;
import org.bitcoinj.core.NetworkParameters;
import org.bitcoinj.params.RegTestParams;
import org.bitcoinj.wallet.Wallet;
import org.junit.Test;

/**
 * Created by mati on 17/02/17.
 */
public class ExampleTest {


    @Test
    public void exampleTest(){

        NetworkParameters networkParameters = RegTestParams.get();

        Context context = new Context(networkParameters);

        Context.propagate(context);

        Wallet wallet = new Wallet(networkParameters);



    }

}
