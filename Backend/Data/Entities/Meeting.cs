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
        public DateTimeOffset Date { get; set; }
    }
}