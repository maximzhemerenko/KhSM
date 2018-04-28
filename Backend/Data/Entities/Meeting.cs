using System;
using System.ComponentModel.DataAnnotations;

namespace Backend.Data.Entities
{
    public class Meeting
    {
        [Required]
        public int Id { get; set; }
        [Required]
        public int Number { get; set; }
        [Required]
        public DateTime Date { get; set; }

        protected bool Equals(Meeting other)
        {
            return Id == other.Id;
        }

        public override bool Equals(object obj)
        {
            if (ReferenceEquals(null, obj)) return false;
            if (ReferenceEquals(this, obj)) return true;
            if (obj.GetType() != this.GetType()) return false;
            return Equals((Meeting) obj);
        }

        public override int GetHashCode()
        {
            return Id;
        }
    }
}