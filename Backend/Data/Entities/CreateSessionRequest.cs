using System;
using System.ComponentModel.DataAnnotations;

namespace Backend.Data.Entities
{
    public class CreateSessionRequest
    {
        [Required]
        public string Email { get; set; }
        [Required]
        public string Password { get; set; }
    }
}