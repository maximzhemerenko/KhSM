using System;
using System.ComponentModel.DataAnnotations;

namespace Backend.Data.Entities
{
    public class Session
    {
        [Required]
        public User User { get; set; }
        [Required]
        public string Token { get; set; }
        [Required]
        public DateTimeOffset Created { get; set; }
    }
}