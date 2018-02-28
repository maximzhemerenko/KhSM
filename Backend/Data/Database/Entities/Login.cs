using System.ComponentModel.DataAnnotations;
using Backend.Data.Entities;

namespace Backend.Data.Database.Entities
{
    public class Login
    {
        [Required]
        public int Id { get; set; }
        [Required]
        public byte[] Hash { get; set; }
    }
}