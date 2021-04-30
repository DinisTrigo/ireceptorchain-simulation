package iReceptorPlus.Blockchain.RepastSimulation.PerformanceMetrics;

public class IterationStateMetrics
{
    private Integer numberOfTraceabilityDataEntiresAwaitingValidation;
    private Integer numberOfTraceabilityDataEntiresValidated;
    private Integer numberOfTraceabilityDataEntiresIncorrectlyClassified;

    public IterationStateMetrics(Integer numberOfTraceabilityDataEntiresAwaitingValidation, Integer numberOfTraceabilityDataEntiresValidated, Integer numberOfTraceabilityDataEntiresIncorrectlyClassified)
    {
        this.numberOfTraceabilityDataEntiresAwaitingValidation = numberOfTraceabilityDataEntiresAwaitingValidation;
        this.numberOfTraceabilityDataEntiresValidated = numberOfTraceabilityDataEntiresValidated;
        this.numberOfTraceabilityDataEntiresIncorrectlyClassified = numberOfTraceabilityDataEntiresIncorrectlyClassified;
    }

    public Integer getNumberOfTraceabilityDataEntiresAwaitingValidation()
    {
        return numberOfTraceabilityDataEntiresAwaitingValidation;
    }

    public Integer getNumberOfTraceabilityDataEntiresValidated()
    {
        return numberOfTraceabilityDataEntiresValidated;
    }

    public Integer getNumberOfTraceabilityDataEntiresIncorrectlyClassified()
    {
        return numberOfTraceabilityDataEntiresIncorrectlyClassified;
    }

    @Override
    public String toString()
    {
        return "IterationStateMetrics{" +
                "numberOfTraceabilityDataEntiresAwaitingValidation=" + numberOfTraceabilityDataEntiresAwaitingValidation +
                ", numberOfTraceabilityDataEntiresValidated=" + numberOfTraceabilityDataEntiresValidated +
                ", numberOfTraceabilityDataEntiresIncorrectlyClassified=" + numberOfTraceabilityDataEntiresIncorrectlyClassified +
                '}';
    }
}
