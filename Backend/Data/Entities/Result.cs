using System;
using System.Collections.Generic;

namespace Backend.Data.Entities
{
    public class Result
    {
        public int Id { get; set; }
        public Meeting Meeting { get; set; }
        public Discipline Discipline { get; set; }
        public decimal? Average { get; set; }
        public IEnumerable<decimal?> Attemts { get; set; }

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
    }
}