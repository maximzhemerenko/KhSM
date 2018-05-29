using System.ComponentModel.DataAnnotations;

namespace Backend.Data.Entities
{
    public class DisciplineRecord
    {
        [Required]
        public Discipline Discipline { get; set; }
        [Required]
        public Result BestSingleResult { get; set; }
        [Required]
        public Result BestAverageResult { get; set; }
    }
}