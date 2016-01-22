package com.example.ttth.andyams;

/**
 * Created by TTTH on 1/11/2016.
 */
import java.util.Arrays;

public class Andyams
{
    public static abstract class ScorePolicy
    {
        private final String name;

        public ScorePolicy(String name)
        {
            this.name = name;
        }

        public String getName()
        {
            return name;
        }

        /** Compute the score for the given dice values
         * We assume that the dice values are already sorted
         * (that may be done by Arrays.sort(dices) before calling the method
         */
        public abstract int computeScore(int[] dices);

        @Override
        public String toString()
        {
            return getName();
        }
    }

    public static class NumberScorePolicy extends ScorePolicy
    {
        private final int number;

        public NumberScorePolicy(int number)
        {
            super("" + number);
            this.number = number;
        }

        @Override
        public int computeScore(int[] dices)
        {
            int score = 0;
            for (int i = 0; i < dices.length; i++)
                if (dices[i] == number)
                    score += number;
            return score;
        }
    }

    /** Return the number of occurrences of the most present dice in the sorted array */
    private static int getMaxOccurrences(int[] tab)
    {
        assert(tab.length > 0);
        int runs = 1;
        int maxOccurrences = 0;
        for (int i = 1; i < tab.length; i++)
            if (tab[i] == tab[i-1])
                runs++;
            else
            {
                maxOccurrences = Math.max(runs, maxOccurrences);
                runs = 1;
            }
        maxOccurrences = Math.max(runs, maxOccurrences);
        return maxOccurrences;
    }

    /** Compute the sum of the dices */
    private static int getDiceSum(int[] tab)
    {
        int sum = 0;
        for (int v: tab) sum += v;
        return sum;
    }

    /** Get the number of distinct dices */
    private static int getDistinctDices(int[] tab)
    {
        int vector = 0;
        for (int v: tab)
            vector = vector | (1 << (v-1));
        return Integer.bitCount(vector);
    }

    /** Get the largest sequence length */
    private static int getMaxSequenceLength(int[] tab)
    {
        int maxLength = 0;
        int length = 1;
        for (int i = 1; i < tab.length; i++)
            if (tab[i] == tab[i-1] + 1)
                length++;
            else
            {
                maxLength = Math.max(maxLength, length);
                length = 1;
            }
        maxLength = Math.max(maxLength, length);
        return maxLength;
    }

    private static final ScorePolicy[] SCORE_POLICIES = new ScorePolicy[]{
            // number policies
            new NumberScorePolicy(1),
            new NumberScorePolicy(2),
            new NumberScorePolicy(3),
            new NumberScorePolicy(4),
            new NumberScorePolicy(5),
            new NumberScorePolicy(6),

            new ScorePolicy("yahtzee") {
                @Override
                public int computeScore(int[] dices)
                {
                    return (getMaxOccurrences(dices) >= dices.length)?50:0;
                }
            },

            new ScorePolicy("four") {
                @Override
                public int computeScore(int[] dices)
                {
                    return (getMaxOccurrences(dices) >= dices.length - 1)?getDiceSum(dices):0;
                }
            },

            new ScorePolicy("brelan") {
                @Override
                public int computeScore(int[] dices)
                {
                    return (getMaxOccurrences(dices) >= dices.length - 2)?getDiceSum(dices):0;
                }
            },

            new ScorePolicy("full") {
                @Override
                public int computeScore(int[] dices)
                {
                    return (getMaxOccurrences(dices) == dices.length - 2 && getDistinctDices(dices) == 2)?25:0;
                }
            },

            new ScorePolicy("smallseq") {
                @Override
                public int computeScore(int[] dices)
                {
                    return (getMaxSequenceLength(dices) >= 4)?30:0;
                }
            },

            new ScorePolicy("largeseq") {
                @Override
                public int computeScore(int[] dices)
                {
                    return (getMaxSequenceLength(dices) >= 5)?40:0;
                }
            },

            new ScorePolicy("chance") {
                @Override
                public int computeScore(int[] dices)
                {
                    return getDiceSum(dices);
                }
            }
    };

    public static ScorePolicy[] getScorePolicies()
    {
        return SCORE_POLICIES;
    }

    public static void testPolicies(int[] dices)
    {
        System.out.println(Arrays.toString(dices));
        Arrays.sort(dices);
        for (ScorePolicy p: getScorePolicies())
            System.out.println("\t" + p.getName() + ": " + p.computeScore(dices));
    }


    public static void main(String[] args)
    {
        // Test some dices with the policies
        for (int[] dices: new int[][]{
                new int[]{5,4,3,6,1},
                new int[]{2,3,3,2,3},
                new int[]{5,4,3,2,1},
                new int[]{1,1,2,2,6},
                new int[]{6,6,6,6,6}
        })
            testPolicies(dices);
    }
}