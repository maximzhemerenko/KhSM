using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations;
using System.Diagnostics;
using System.Linq;

namespace Backend.Data.Entities
{
    public class Result
    {
        [Required]
        public int Id { get; set; }
        public Meeting Meeting { get; set; }
        public Discipline Discipline { get; set; }
        public User User { get; set; }
        public decimal? Average { get; set; }
        [Required]
        public IEnumerable<decimal?> Attempts { get; set; }
        public int AttemptCount { get; set; }

        protected bool Equals(Result other)
        {
            return Id == other.Id;
        }

        public override bool Equals(object obj)
        {
            if (ReferenceEquals(null, obj)) return false;
            if (ReferenceEquals(this, obj)) return true;
            if (obj.GetType() != this.GetType()) return false;
            return Equals((Result) obj);
        }

        public override int GetHashCode()
        {
            return Id;
        }
        
        public class Comparer : IComparer<Result>
        {
            private readonly Mode _mode;

            public Comparer(Mode mode)
            {
                _mode = mode;
            }

            public int Compare(Result x, Result y)
            {
                switch (_mode)
                {
                    case Mode.Average:
                    {
                        var c = CompareByAverage(x, y);
                        if (c == 0)
                            c = ComareByAttempts(x, y);
                        return c;
                    }
                    case Mode.Single:
                    {
                        var c = ComareByAttempts(x, y);
                        return c;
                    }
                    default:
                        throw new Exception("Not supported mode");
                }
            }

            private static int CompareByAverage(Result x, Result y)
            {
                Debug.Assert(x != null, nameof(x) + " != null");
                Debug.Assert(y != null, nameof(y) + " != null");

                // ReSharper disable once ConvertIfStatementToSwitchStatement
                if (x.Average == null && y.Average == null)
                    return 0;

                if (x.Average == null)
                    return 1;
                if (y.Average == null)
                    return -1;

                return decimal.Compare(x.Average.Value, y.Average.Value);
            }

            private static int ComareByAttempts(Result x, Result y)
            {
                var bestAttemptX = x.Attempts?.Where(a => a != null).Min();
                var bestAttemptY = y.Attempts?.Where(a => a != null).Min();

                // ReSharper disable once ConvertIfStatementToSwitchStatement
                if (bestAttemptX == null && bestAttemptY == null)
                    return 0;

                if (bestAttemptX == null)
                    return 1;
                if (bestAttemptY == null)
                    return -1;

                return decimal.Compare(bestAttemptX.Value, bestAttemptY.Value);
            }

            public enum Mode
            {
                Single, Average
            }
        }
    }
}