using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations;

namespace Backend.Data.Entities
{
    public class Result
    {
        [Required]
        public int Id { get; set; }
        [Required]
        public Meeting Meeting { get; set; }
        public Discipline Discipline { get; set; }
        public decimal? Average { get; set; }
        [Required]
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