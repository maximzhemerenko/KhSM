using System.ComponentModel.DataAnnotations;

namespace Backend.Data.Entities
{
    public class DisciplineRecord
    {
        [Required]
        public Discipline Discipline { get; set; }
        [Required]
        public Result BestTime { get; set; }
        [Required]
        public Result BestOverageTime { get; set; }
    }
}