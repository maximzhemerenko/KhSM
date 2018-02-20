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

        public IEnumerable<Discipline> GetDisciplinesByMeetingId(int meetingId)
        {
            const string meetingIdKey = "meeting_id";
            
            using (var command = new MySqlCommand(Connection, Transaction)
            {
                Parameters = {new MySqlParameter(meetingIdKey, meetingId)}
            })
            {
                // check is meeting exists in the db
                command.CommandText = $"select {meetingIdKey} from meeting where {meetingIdKey} = @{meetingIdKey}";
                
                var exists = (int?) command.ExecuteScalar();
                if (!exists.HasValue)
                {
                    return null;
                }

                // read disciplines from meeting results
                command.CommandText = $"select * from meeting_discipline where {meetingIdKey} = @{meetingIdKey}";

                using (var reader = command.ExecuteReader())
                {
                    return ReadDisciplines(reader);
                }
            }
        }

        private static Discipline GetDiscipline(MySqlDataReader reader)
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