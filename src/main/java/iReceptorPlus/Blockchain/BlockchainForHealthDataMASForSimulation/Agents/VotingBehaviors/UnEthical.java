package iReceptorPlus.Blockchain.BlockchainForHealthDataMASForSimulation.Agents.VotingBehaviors;

import iReceptorPlus.Blockchain.BlockchainForHealthDataMASForSimulation.Agents.Agent;

public abstract class UnEthical extends VotingBehavior
{
    public UnEthical(Agent agent)
    {
        super(agent);
    }

    @Override
    public void work(Double workUnits) throws InterruptedException
    {
        //UnEthical agents never perform the work corresponding to the entry they vote for
    }
}
