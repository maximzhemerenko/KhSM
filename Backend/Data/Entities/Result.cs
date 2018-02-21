using System.Collections.Generic;

namespace Backend.Data.Entities
{
    public class Result
    {
        public int Id { get; set; }
        public Meeting Meeting { get; set; }
        public Discipline Discipline { get; set; }
        public decimal? Average { get; set; }
        public IEnumerable<decimal?> Attemts { get; set; }
    }
}