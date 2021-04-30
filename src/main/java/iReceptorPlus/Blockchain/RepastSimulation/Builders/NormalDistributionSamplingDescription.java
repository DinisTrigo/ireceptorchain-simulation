package iReceptorPlus.Blockchain.RepastSimulation.Builders;

public class NormalDistributionSamplingDescription
{
    private Double mean;
    private Double standardDeviation;

    public NormalDistributionSamplingDescription(Double mean, Double standardDeviation)
    {
        this.mean = mean;
        this.standardDeviation = standardDeviation;
    }

    public Double getMean()
    {
        return mean;
    }

    public Double getStandardDeviation()
    {
        return standardDeviation;
    }
}
