using System.ComponentModel.DataAnnotations;

namespace Backend.Data.Entities
{
    public class User
    {
        [Required]
        public int Id { get; set; }
        [Required]
        public string FirstName { get; set; }
    }
}