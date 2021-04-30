package iReceptorPlus.Blockchain.BlockchainForHealthDataMASForSimulation.Agents.CreatingBehavior;


import iReceptorPlus.Blockchain.BlockchainForHealthDataMASForSimulation.Agents.Agent;

public class EthicalCreator extends CreatingBehavior
{
    public EthicalCreator(Agent agent)
    {
        super(agent);
    }

    @Override
    public void perform()
    {
        createEntry(true);
    }

}
