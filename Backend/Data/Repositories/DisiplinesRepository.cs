using System.Collections.Generic;
using Backend.Data.Database;
using Backend.Data.Entities;
using MySql.Data.MySqlClient;

namespace Backend.Data.Repositories
{
    // ReSharper disable once ClassNeverInstantiated.Global
    public class DisciplinesRepository : BaseRepository
    {
        public DisciplinesRepository(DatabaseContext databaseContext) : base(databaseContext)
        {
        }

        public IEnumerable<Discipline> GetDisciplines()
        {
            using (var command = new MySqlCommand("select * from discipline", Connection, Transaction))
            using (var reader = command.ExecuteReader())
            {
                return ReadDisciplines(reader);
            }
        }

        public static Discipline GetDiscipline(MySqlDataReader reader)
        {
            return new Discipline
            {
                Id = reader.GetInt32("discipline_id"),
                Name = reader.GetString("name"),
                Description = reader.GetString("description"),
                AttemptsCount = reader.GetInt32("attempt_count")
            };
        }
        
        private static IEnumerable<Discipline> ReadDisciplines(MySqlDataReader reader)
        {
            var disciplines = new List<Discipline>();

            while (reader.Read())
            {
                disciplines.Add(GetDiscipline(reader));
            }

            return disciplines;
        }
    }
}