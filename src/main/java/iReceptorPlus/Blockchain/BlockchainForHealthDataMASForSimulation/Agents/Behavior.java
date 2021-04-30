package iReceptorPlus.Blockchain.BlockchainForHealthDataMASForSimulation.Agents;

import iReceptorPlus.Blockchain.BlockchainForHealthDataMASForSimulation.LoggingAndSettings.Logging;

public abstract class Behavior
{
    protected Agent agent;

    /**
     * Performs the behavior: picks work to do and does that work.
     */
    public abstract void perform();

    public Behavior(Agent agent)
    {
        this.agent = agent;
    }

    /**
     * This method is used by subclasses to build exception error messages, with a prefix that identifies the agent.
     * @return A string that represents the exception message prefix that identifies the agent.
     */
    protected String getGenericExceptionMessagePrefix()
    {
        return "Agent " + agent.getEntityId() + " ";
    }

    protected void logErrorWithMessage(String message)
    {
        Logging.LogError(getGenericExceptionMessagePrefix() + message);
    }
    protected void logErrorOnTryingToGetMyReputation()
    {
        logErrorWithMessage("got ObjectWithGivenKeyNotFoundOnBlockchainDB error when trying to get its reputation");
    }

    protected void logErrorOnTryingToGetMyReputation(String additionalInfo)
    {
        logErrorWithMessage("got ObjectWithGivenKeyNotFoundOnBlockchainDB error when trying to get its reputation " + additionalInfo);
    }

}
