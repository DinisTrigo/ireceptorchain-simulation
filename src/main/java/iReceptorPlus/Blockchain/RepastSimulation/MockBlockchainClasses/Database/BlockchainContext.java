package iReceptorPlus.Blockchain.RepastSimulation.MockBlockchainClasses.Database;

public class BlockchainContext
{
    ChaincodeStub stub;

    ClientIdentity clientIdentity;

    public BlockchainContext(ChaincodeStub stub)
    {
        this.stub = stub;
    }

    public ChaincodeStub getStub()
    {
        return stub;
    }

    public ClientIdentity getClientIdentity()
    {
        return clientIdentity;
    }

    public void setClientId(String id)
    {
        clientIdentity = new ClientIdentity(id);
    }
}
