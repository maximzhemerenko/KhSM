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
            using (var command = new MySqlCommand("select * from discipline", Connection))
            using (var reader = command.ExecuteReader())
            {
                return ReadDisciplines(reader);
            }
        }

        public Discipline GetDiscipline(int id, MySqlTransaction transaction = null, bool readCounting = false)
        {
            const string disciplineIdKey = "discipline_id";
            
            using (var command = new MySqlCommand($"select * from discipline where {disciplineIdKey} = @{disciplineIdKey}", Connection, transaction)
            {
                Parameters =
                {
                    new MySqlParameter(disciplineIdKey, id)
                }
            })
            using (var reader = command.ExecuteReader())
            {
                return reader.Read() ? GetDiscipline(reader, readCounting) : null;
            }
        }

        public static Discipline GetDiscipline(MySqlDataReader reader, bool readCounting = false)
        {
            var discipline = new Discipline
            {
                Id = reader.GetInt32("discipline_id"),
                Name = reader.GetString("name"),
                Description = reader.GetString("description")
            };
            if (readCounting)
            {
                discipline.Counting = reader.GetString("counting");
            }
            return discipline;
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