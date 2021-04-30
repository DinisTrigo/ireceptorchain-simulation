package iReceptorPlus.Blockchain.BlockchainForHealthDataMASForSimulation.Agents;

public class BehaviorIncidence
{
    /**
     * The amount of times (relative to votingBehaviorIncidence) that this agent will behave as a creator.
     */
    protected Double creatingBehaviorIncidence;

    protected Double willingnessToAdditionalRewardWhenCreating;

    /**
     * The amount of times (relative to creatingBehaviorIncidence) that this agent will behave as a voter.
     */
    protected Double votingBehaviorIncidence;

    public static class EthicnessBehaviorIncidence
    {
        /**
         * The amount of times (relative to unEthicalBehaviorIncidence) that this agent will behave ethically.
         */
        protected Double ethicalBehaviorIncidence;

        /**
         * The amount of times (relative to ethicalBehaviorIncidence) that this agent will behave unethically.
         */
        protected Double unEthicalBehaviorIncidence;


        public static class UnEthicalVotingBehaviorIncidence
        {
            /**
             * The amount of times (relative to majorityCreatingBehaviorIncidence) that this agent will behave randomly.
             */
            protected Double randomBehaviorIncidence;

            /**
             * The amount of times (relative to randomBehaviorIncidence) that this agent will behave according to the majority.
             */
            protected Double majorityVotingBehaviorIncidence;

            public static class MajorityVotingBehaviorIncidence
            {
                /**
                 * The importance given to the majority when choosing the traceability data entry to vote for.
                 * The significance of this value is relative to the importance of reward value, stored in variable rewardImportance).
                 */
                public Double majorityImportance;

                /**
                 * The importance given to the reward when choosing the traceability data entry to vote for.
                 * The significance of this value is relative to the importance of majority, stored in variable majorityImportance).
                 */
                protected Double rewardImportance;

                /**
                 * The amount of times (relative to randomBehaviorIncidence and contradictMajorityCreatingBehaviorIncidence) that this agent will behave unethically.
                 */
                protected Double followMajorityCreatingBehaviorIncidence;

                /**
                 * The amount of times (relative to randomBehaviorIncidence and followMajorityCreatingBehaviorIncidence) that this agent will behave unethically.
                 */
                protected Double contradictMajorityCreatingBehaviorIncidence;

                public MajorityVotingBehaviorIncidence(Double majorityImportance, Double rewardImportance, Double followMajorityCreatingBehaviorIncidence, Double contradictMajorityCreatingBehaviorIncidence)
                {
                    this.majorityImportance = majorityImportance;
                    this.rewardImportance = rewardImportance;
                    this.followMajorityCreatingBehaviorIncidence = followMajorityCreatingBehaviorIncidence;
                    this.contradictMajorityCreatingBehaviorIncidence = contradictMajorityCreatingBehaviorIncidence;
                }
            }

            protected MajorityVotingBehaviorIncidence majorityVotingBehaviorIncidenceDescription;

            public UnEthicalVotingBehaviorIncidence(Double randomBehaviorIncidence, Double majorityVotingBehaviorIncidence, MajorityVotingBehaviorIncidence majorityVotingBehaviorIncidenceDescription)
            {
                this.randomBehaviorIncidence = randomBehaviorIncidence;
                this.majorityVotingBehaviorIncidence = majorityVotingBehaviorIncidence;
                this.majorityVotingBehaviorIncidenceDescription = majorityVotingBehaviorIncidenceDescription;
            }
        }
        protected UnEthicalVotingBehaviorIncidence unEthicalVotingBehaviorIncidence;

        public EthicnessBehaviorIncidence(Double ethicalBehaviorIncidence, Double unEthicalBehaviorIncidence, UnEthicalVotingBehaviorIncidence unEthicalVotingBehaviorIncidence)
        {
            this.ethicalBehaviorIncidence = ethicalBehaviorIncidence;
            this.unEthicalBehaviorIncidence = unEthicalBehaviorIncidence;
            this.unEthicalVotingBehaviorIncidence = unEthicalVotingBehaviorIncidence;
        }
    }

    protected EthicnessBehaviorIncidence creatingEthicnessIncidence;

    protected EthicnessBehaviorIncidence votingEthicnessIncidence;

    public BehaviorIncidence(Double creatingBehaviorIncidence, Double willingnessToAdditionalRewardWhenCreating, Double votingBehaviorIncidence, EthicnessBehaviorIncidence creatingEthicnessIncidence, EthicnessBehaviorIncidence votingEthicnessIncidence)
    {
        this.creatingBehaviorIncidence = creatingBehaviorIncidence;
        this.willingnessToAdditionalRewardWhenCreating = willingnessToAdditionalRewardWhenCreating;
        this.votingBehaviorIncidence = votingBehaviorIncidence;
        this.creatingEthicnessIncidence = creatingEthicnessIncidence;
        this.votingEthicnessIncidence = votingEthicnessIncidence;
    }

    public Double getVotingBehaviorIncidence()
    {
        return votingBehaviorIncidence;
    }

    public Double getCreatingBehaviorIncidence()
    {
        return creatingBehaviorIncidence;
    }

    public EthicnessBehaviorIncidence getCreatingEthicnessIncidence()
    {
        return creatingEthicnessIncidence;
    }

    public EthicnessBehaviorIncidence getVotingEthicnessIncidence()
    {
        return votingEthicnessIncidence;
    }

    public Double getWillingnessToAdditionalRewardWhenCreating()
    {
        return willingnessToAdditionalRewardWhenCreating;
    }
}
