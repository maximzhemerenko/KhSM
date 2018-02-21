using System.Collections.Generic;

namespace Backend.Data.Entities
{
    public class DisciplineResults
    {
        public Discipline Discipline { get; set; }
        public IEnumerable<Result> Results { get; set; }
    }
}