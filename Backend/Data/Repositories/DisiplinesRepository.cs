using System.Collections.Generic;
using Backend.Data.Database;
using Backend.Data.Entities;
using MySql.Data.MySqlClient;

namespace Backend.Data.Repositories
{
    public class DisciplinesRepository : BaseRepository
    {
        public DisciplinesRepository(DatabaseContext databaseContext) : base(databaseContext)
        {
        }

        public IEnumerable<Discipline> GetDisciplines()
        {
            using(var transaction = Connection.BeginTransaction())
            using (var command = new MySqlCommand("select * from discipline", Connection, transaction))
            using (var reader = command.ExecuteReader())
            {
                var disciplines = new List<Discipline>();

                while (reader.Read())
                {
                    disciplines.Add(new Discipline
                    {
                        Id = reader.GetInt32("discipline_id"),
                        Name = reader.GetString("name"),
                        Description = reader.GetString("description"),
                        AttemptsCount = reader.GetInt32("attempt_count")
                    });
                }

                return disciplines;
            }
        }
    }
}