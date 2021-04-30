package iReceptorPlus.Blockchain.RepastSimulation.Builders;

import java.util.Random;

public class PRNGNormalDistribution implements PRNG
{
    protected Double mean;
    protected Double standardDeviation;
    protected Random random;

    public PRNGNormalDistribution(Double mean, Double standardDeviation)
    {
        this.mean = mean;
        this.standardDeviation = standardDeviation;
        random = new Random();
    }

    @Override
    public Double getNextDouble()
    {
        return random.nextGaussian() * standardDeviation + mean;
    }

    public Double getNextPositiveDouble()
    {
        double value;
        do
        {
            value = random.nextGaussian() * standardDeviation + mean;
        } while (value < 0);

        return value;
    }
}
