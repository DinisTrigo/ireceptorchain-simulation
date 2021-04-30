package iReceptorPlus.Blockchain.BlockchainForHealthDataMASForSimulation.Agents;

import iReceptorPlus.Blockchain.BlockchainForHealthDataMASForSimulation.Agents.CreatingBehavior.EthicalCreator;
import iReceptorPlus.Blockchain.BlockchainForHealthDataMASForSimulation.Agents.CreatingBehavior.SpammingBehavior;
import iReceptorPlus.Blockchain.BlockchainForHealthDataMASForSimulation.Agents.VotingBehaviors.Ethical;
import iReceptorPlus.Blockchain.BlockchainForHealthDataMASForSimulation.Agents.VotingBehaviors.Majority;
import iReceptorPlus.Blockchain.BlockchainForHealthDataMASForSimulation.Agents.VotingBehaviors.Random;

import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

public class Agent
{
    /**
     * The id of the entity that represents this agent on the blockchain.
     */
    protected String entityId;

    /**
     * The rate at which the agent can process data, in work units per second.
     */
    protected Double workRateCapacity;

    /**
     * The risk that the agent wants to take when operating the system.
     * Should be a value between the one defined as the MIN_RISK and the MAX_RISK.
     */
    protected Double risk;

    public static final double MIN_RISK = 0;
    public static final double MAX_RISK = 1.0;

    BehaviorIncidence behaviorIncidence;


    protected Behavior behavior;

    public Agent(String entityId, Double workRateCapacity, Double risk, BehaviorIncidence behaviorIncidence)
    {
        this.entityId = entityId;
        this.workRateCapacity = workRateCapacity;
        this.risk = risk;
        this.behaviorIncidence = behaviorIncidence;
    }

    public String getEntityId()
    {
        return entityId;
    }

    public Double getWorkRateCapacity()
    {
        return workRateCapacity;
    }

    public Double getRisk()
    {
        return risk;
    }

    public BehaviorIncidence getBehaviorIncidence()
    {
        return behaviorIncidence;
    }

    public void performBehavior()
    {
        Double totalBehaviorPercentage = behaviorIncidence.getCreatingBehaviorIncidence() + behaviorIncidence.getVotingBehaviorIncidence();
        Double rand = ThreadLocalRandom.current().nextDouble() * totalBehaviorPercentage;
        if (rand < behaviorIncidence.getCreatingBehaviorIncidence())
        {
            pickCreatingBehavior(behaviorIncidence.getCreatingEthicnessIncidence());
        } else
        {
            pickVotingBehaviorEthicness(behaviorIncidence.getVotingEthicnessIncidence());
        }

        try
        {
            behavior.perform();
        }
        catch (IndexOutOfBoundsException exception)
        {

        }
    }

    public void pickCreatingBehavior(BehaviorIncidence.EthicnessBehaviorIncidence behaviorIncidence)
    {
        Double totalBehaviorPercentage = behaviorIncidence.ethicalBehaviorIncidence + behaviorIncidence.unEthicalBehaviorIncidence;
        Double rand = ThreadLocalRandom.current().nextDouble() * totalBehaviorPercentage;
        if (rand < behaviorIncidence.ethicalBehaviorIncidence)
        {
            behavior = new EthicalCreator(this);
        } else
        {
            behavior = new SpammingBehavior(this);
        }
    }

    public void pickVotingBehaviorEthicness(BehaviorIncidence.EthicnessBehaviorIncidence behaviorIncidence)
    {
        Double totalBehaviorPercentage = behaviorIncidence.ethicalBehaviorIncidence + behaviorIncidence.unEthicalBehaviorIncidence;
        Double rand = ThreadLocalRandom.current().nextDouble() * totalBehaviorPercentage;
        if (rand < behaviorIncidence.ethicalBehaviorIncidence)
        {
            behavior = new Ethical(this);
        } else
        {
            pickUnEthicalVotingBehavior(behaviorIncidence.unEthicalVotingBehaviorIncidence);
        }
    }

    public void pickUnEthicalVotingBehavior(BehaviorIncidence.EthicnessBehaviorIncidence.UnEthicalVotingBehaviorIncidence behaviorIncidence)
    {
        Double totalBehaviorPercentage = behaviorIncidence.randomBehaviorIncidence + behaviorIncidence.majorityVotingBehaviorIncidence;
        Double rand = ThreadLocalRandom.current().nextDouble() * totalBehaviorPercentage;
        if (rand < behaviorIncidence.randomBehaviorIncidence)
        {
            behavior = new Random(this);
        }
        else
        {
            pickUnEthicalMajorityVotingBehavior(behaviorIncidence.majorityVotingBehaviorIncidenceDescription);
        }
    }

    private void pickUnEthicalMajorityVotingBehavior(BehaviorIncidence.EthicnessBehaviorIncidence.UnEthicalVotingBehaviorIncidence.MajorityVotingBehaviorIncidence behaviorIncidence)
    {
        Double totalBehaviorPercentage = behaviorIncidence.followMajorityCreatingBehaviorIncidence + behaviorIncidence.contradictMajorityCreatingBehaviorIncidence;
        Double rand = ThreadLocalRandom.current().nextDouble() * totalBehaviorPercentage;
        boolean follow;
        if (rand < behaviorIncidence.followMajorityCreatingBehaviorIncidence)
        {
            follow = true;
        }
        else
        {
            follow = false;
        }

        behavior = new Majority(this, behaviorIncidence.majorityImportance, behaviorIncidence.rewardImportance, follow);
    }
}
