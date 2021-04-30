package iReceptorPlus.Blockchain.RepastSimulation.Builders;

import iReceptorPlus.Blockchain.BlockchainForHealthDataMASForSimulation.Agents.BehaviorIncidence;

public class AgentsDescription
{
    private Long numberOfAgents;

    private Double incidenceOfEthicalAgents;
    private Double incidenceOfUnEthicalAgents;

    private BehaviorIncidence behaviorIncidenceOfEthicalAgents;
    private BehaviorIncidence behaviorIncidenceOfUnEthicalAgents;

    private Double initialReputation;

    private NormalDistributionSamplingDescription workRateCapacityDistribution;
    private NormalDistributionSamplingDescription riskDistribution;

    public AgentsDescription(Long numberOfAgents, Double incidenceOfEthicalAgents, Double incidenceOfUnEthicalAgents,
                             BehaviorIncidence behaviorIncidenceOfEthicalAgents, BehaviorIncidence behaviorIncidenceOfUnEthicalAgents,
                             Double initialReputation, NormalDistributionSamplingDescription workRateCapacityDistribution,
                             NormalDistributionSamplingDescription riskDistribution)
    {
        this.numberOfAgents = numberOfAgents;
        this.incidenceOfEthicalAgents = incidenceOfEthicalAgents;
        this.incidenceOfUnEthicalAgents = incidenceOfUnEthicalAgents;
        this.behaviorIncidenceOfEthicalAgents = behaviorIncidenceOfEthicalAgents;
        this.behaviorIncidenceOfUnEthicalAgents = behaviorIncidenceOfUnEthicalAgents;
        this.initialReputation = initialReputation;
        this.workRateCapacityDistribution = workRateCapacityDistribution;
        this.riskDistribution = riskDistribution;
    }

    public Long getNumberOfAgents()
    {
        return numberOfAgents;
    }

    public Double getIncidenceOfEthicalAgents()
    {
        return incidenceOfEthicalAgents;
    }

    public Double getIncidenceOfUnEthicalAgents()
    {
        return incidenceOfUnEthicalAgents;
    }

    public BehaviorIncidence getBehaviorIncidenceOfEthicalAgents()
    {
        return behaviorIncidenceOfEthicalAgents;
    }

    public BehaviorIncidence getBehaviorIncidenceOfUnEthicalAgents()
    {
        return behaviorIncidenceOfUnEthicalAgents;
    }

    public Double getInitialReputation()
    {
        return initialReputation;
    }

    public NormalDistributionSamplingDescription getWorkRateCapacityDistribution()
    {
        return workRateCapacityDistribution;
    }

    public NormalDistributionSamplingDescription getRiskDistribution()
    {
        return riskDistribution;
    }
}
