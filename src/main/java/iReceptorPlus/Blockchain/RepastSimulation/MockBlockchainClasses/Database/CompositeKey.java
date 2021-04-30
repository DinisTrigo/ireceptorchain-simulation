package iReceptorPlus.Blockchain.RepastSimulation.MockBlockchainClasses.Database;

public class CompositeKey
{
    String identifier;
    String uuid;

    public CompositeKey(String identifier, String uuid)
    {
        this.identifier = identifier;
        this.uuid = uuid;
    }

    @Override
    public String toString()
    {
        return identifier + "-" + uuid;
    }
}
