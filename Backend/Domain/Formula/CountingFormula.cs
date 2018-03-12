using System;
using System.Collections.Generic;
using System.Linq;

namespace Backend.Domain.Formula
{
    public abstract class CountingFormula
    {
        public int AttemptCount { get; }

        private CountingFormula(int attemptCount)
        {
            AttemptCount = attemptCount;
        }

        public abstract decimal? ComputeAverage(IEnumerable<decimal?> attempts);

        public static CountingFormula Get(string counting)
        {
            switch (counting)
            {
                case "avg5":
                    return new Avg5();
                case "mo3":
                    return new Mo3();
                case "bo3":
                    return new Bo3();
                default:
                    throw new Exception("Unknown counting");
            }
        }

        private class Avg5 : CountingFormula
        {
            public Avg5() : base(5)
            {
            }

            public override decimal? ComputeAverage(IEnumerable<decimal?> attempts)
            {
                var orderedAttempts = attempts.OrderBy(a => a).ToList();

                if (orderedAttempts.Count > AttemptCount)
                    throw new Exception("Too much attempts provided");

                var nullsCount = orderedAttempts.Count(arg => arg == null) + (AttemptCount - orderedAttempts.Count);

                if (nullsCount > 1)
                    return null;
                
                orderedAttempts.RemoveAt(orderedAttempts.Count - 1);
                orderedAttempts.RemoveAt(0);
                
                return orderedAttempts.Average();
            }
        }

        private class Mo3 : CountingFormula
        {   
            public Mo3() : base(3)
            {
            }
            
            public override decimal? ComputeAverage(IEnumerable<decimal?> attempts)
            {
                // todo fix it 
                return attempts.Average();
            }
        }

        private class Bo3 : CountingFormula
        {
            public Bo3() : base(3)
            {
            }
            
            public override decimal? ComputeAverage(IEnumerable<decimal?> attempts)
            {
                // todo fix it
                throw new NotSupportedException();
            }
        }
    }
}