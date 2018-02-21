using Backend.Data.Database;
using Backend.Data.Entities;
using MySql.Data.MySqlClient;

namespace Backend.Data.Repositories
{
    public class ResultsRepository : BaseRepository
    {
        public ResultsRepository(DatabaseContext databaseContext) : base(databaseContext)
        {
        }

        public static Result GetResult(MySqlDataReader reader)
        {
            return new Result
            {
                Id = reader.GetInt32("result_id"),
                Average = reader.GetFieldValue<decimal?>(reader.GetOrdinal("average"))
            };
        }
    }
}