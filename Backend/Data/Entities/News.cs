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
        public DateTime DateAndTime { get; set; }
    }
}