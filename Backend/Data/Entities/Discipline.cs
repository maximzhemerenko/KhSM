using System;
using System.ComponentModel.DataAnnotations;

namespace Backend.Data.Entities
{
    public class Discipline
    {
        [Required]
        public int Id { get; set; }
        [Required]
        public string Name { get; set; }
        [Required]
        public string Description { get; set; }
        [Required]
        public int AttemptsCount { get; set; }
    }
}