using System.Collections.Generic;
using Backend.Data.Database;
using Backend.Data.Entities;
using MySql.Data.MySqlClient;

namespace Backend.Data.Repositories
{
    // ReSharper disable once ClassNeverInstantiated.Global
    public class MeetingsRepository : BaseRepository
    {
        public MeetingsRepository(DatabaseContext databaseContext) : base(databaseContext)
        {
        }

        public IEnumerable<Meeting> GetMeetings()
        {
            using (var command = new MySqlCommand("select * from meeting", Connection))
            using (var reader = command.ExecuteReader())
            {
                var meetings = new List<Meeting>();

                while (reader.Read())
                {
                    meetings.Add(GetMeeting(reader));
                }

                return meetings;
            }
        }

        public Meeting GetMeeting(int id)
        {
            using (var command =
                new MySqlCommand("select * from meeting where meeting_id = @meeting_id", Connection)
                {
                    Parameters = {new MySqlParameter("meeting_id", id)}
                })
            using (var reader = command.ExecuteReader())
            {
                return ReadMeeting(reader);
            }
        }
        
        public Meeting GetLastMeeting()
        {
            using (var command = new MySqlCommand("select * from meeting order by date desc limit 1", Connection))
            using (var reader = command.ExecuteReader())
            {
                return ReadMeeting(reader);
            }
        }
        
        public static Meeting GetMeeting(MySqlDataReader reader)
        {
            return new Meeting
            {
                Id = reader.GetInt32("meeting_id"),
                Number = reader.GetInt32("meeting_number"),
                Date = reader.GetDateTime("date")
            };
        }

        public void AddMeeting(Meeting meeting, MySqlTransaction transaction)
        {
            const string meetingNumberKey = "meeting_number";
            const string dateKey = "date";
            
            using (var command = new MySqlCommand(Connection, transaction)
            {
                CommandText = $"insert into meeting({meetingNumberKey}, {dateKey}) " +
                              $"values(@{meetingNumberKey}, @{dateKey})",
                Parameters =
                {
                    new MySqlParameter(meetingNumberKey, meeting.Number),
                    new MySqlParameter(dateKey, meeting.Date)
                }
            })
            {
                command.ExecuteNonQuery();

                meeting.Id = (int) command.LastInsertedId;
            }
        }
        
        public static Meeting ReadMeeting(MySqlDataReader reader)
        {
            return reader.Read() ? GetMeeting(reader) : null;
        }
    }
}