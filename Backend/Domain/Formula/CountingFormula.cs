using System;
using System.Collections.Generic;
using System.Linq;

namespace Backend.Domain.Formula
{
    public abstract class CountingFormula
    {
        private CountingFormula()
        {
        }

        public abstract int AttemptCount { get; }

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
            public override int AttemptCount => 5;

            public override decimal? ComputeAverage(IEnumerable<decimal?> attempts)
            {
                return attempts.Average(arg => arg); // todo
            }
        }

        private class Mo3 : CountingFormula
        {
            public override int AttemptCount => 3;
            
            public override decimal? ComputeAverage(IEnumerable<decimal?> attempts)
            {
                throw new NotSupportedException();
            }
        }

        private class Bo3 : CountingFormula
        {
            public override int AttemptCount => 3;
            
            public override decimal? ComputeAverage(IEnumerable<decimal?> attempts)
            {
                throw new NotSupportedException();
            }
        }
    }
}