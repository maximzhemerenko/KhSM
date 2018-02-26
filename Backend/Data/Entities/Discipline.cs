using System.ComponentModel.DataAnnotations;

namespace Backend.Data.Entities
{
    public class Discipline
    {
        [Required]
        public int Id { get; set; }
        [Required]
        public string Name { get; set; }
        public string Description { get; set; }

        protected bool Equals(Discipline other)
        {
            return Id == other.Id;
        }

        public override bool Equals(object obj)
        {
            if (ReferenceEquals(null, obj)) return false;
            if (ReferenceEquals(this, obj)) return true;
            if (obj.GetType() != this.GetType()) return false;
            return Equals((Discipline) obj);
        }

        public override int GetHashCode()
        {
            return Id;
        }
    }
}