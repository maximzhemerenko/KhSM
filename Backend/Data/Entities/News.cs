using System;
using System.ComponentModel.DataAnnotations;

namespace Backend.Data.Entities
{
    public class News
    {
        [Required]
        public int Id { get; set; }
        [Required]
        public User User { get; set; }
        [Required]
        public String Text { get; set; }
        [Required]
        public DateTime? DateAndTime { get; set; }
        
        protected bool Equals(News other)
        {
            return Id == other.Id;
        }
        
        public override bool Equals(object obj)
        {
            if (ReferenceEquals(null, obj)) return false;
            if (ReferenceEquals(this, obj)) return true;
            if (obj.GetType() != this.GetType()) return false;
            return Equals((News) obj);
        }
        
        public override int GetHashCode()
        {
            return Id;
        }
    
    }
    
}
 
 
    