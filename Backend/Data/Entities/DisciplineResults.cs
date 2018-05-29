using System.Collections.Generic;
using System.ComponentModel.DataAnnotations;

namespace Backend.Data.Entities
{
    public class DisciplineResults
    {
        [Required]
        public Discipline Discipline { get; set; }
        [Required]
        public IEnumerable<Result> Results { get; set; }
    }
}