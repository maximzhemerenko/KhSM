using System;
using System.ComponentModel.DataAnnotations;

namespace Backend.Data.Entities
{
    public class CreateUserRequest
    {
        [Required]
        public User User { get; set; }
        [Required]
        public string Password { get; set; }
    }
}