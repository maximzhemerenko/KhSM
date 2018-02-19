using System.Collections.Generic;
using Backend.Data.Database;
using Backend.Data.Entities;
using MySql.Data.MySqlClient;

namespace Backend.Data.Repositories
{
    public class MeetingsRepository : BaseRepository
    {
        public MeetingsRepository(DatabaseContext databaseContext) : base(databaseContext)
        {
        }

        public IEnumerable<Meeting> GetMeetings()
        {
            using(var transaction = Connection.BeginTransaction())
            using (var command = new MySqlCommand("select * from meeting", Connection, transaction))
            using (var reader = command.ExecuteReader())
            {
                var meetings = new List<Meeting>();

                while (reader.Read())
                {
                    meetings.Add(new Meeting
                    {
                        Id = reader.GetInt32("meeting_id"),
                        Number = reader.GetInt32("meeting_number"),
                        Date = reader.GetDateTimeOffset("date")
                    });
                }

                return meetings;
            }
        }
    }
}