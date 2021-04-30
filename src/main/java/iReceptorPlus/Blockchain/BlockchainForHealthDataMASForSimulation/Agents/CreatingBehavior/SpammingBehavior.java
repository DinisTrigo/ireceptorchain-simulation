package iReceptorPlus.Blockchain.BlockchainForHealthDataMASForSimulation.Agents.CreatingBehavior;


import iReceptorPlus.Blockchain.BlockchainForHealthDataMASForSimulation.Agents.Agent;

public class SpammingBehavior extends CreatingBehavior
{
    public SpammingBehavior(Agent agent)
    {
        super(agent);
    }

    @Override
    public void perform()
    {
        createEntry(false);
    }
}
