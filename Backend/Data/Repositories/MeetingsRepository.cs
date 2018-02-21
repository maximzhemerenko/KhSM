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
            using (var command = new MySqlCommand("select * from meeting", Connection, Transaction))
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
                new MySqlCommand("select * from meeting where meeting_id = @meeting_id", Connection, Transaction)
                {
                    Parameters = {new MySqlParameter("meeting_id", id)}
                })
            using (var reader = command.ExecuteReader())
            {
                return reader.Read() ? GetMeeting(reader) : null;
            }
        }

        public IEnumerable<Result> GetMeetingResults(int meetingId, bool readMeeting = false, bool readDiscipline = false)
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
                command.CommandText = $"select * from meeting_results where {meetingIdKey} = @{meetingIdKey}";

                using (var reader = command.ExecuteReader())
                {
                    var resultsDictionary = new Dictionary<int, Result>();

                    while (reader.Read())
                    {
                        var result = ResultsRepository.GetResult(reader);
                        if (!resultsDictionary.ContainsKey(result.Id))
                        {
                            if (readMeeting)
                                result.Meeting = GetMeeting(reader);
                            if (readDiscipline)
                                result.Discipline = DisciplinesRepository.GetDiscipline(reader);
                            
                            // todo: implement
                            result.Attemts = new List<decimal?>{7, 9, (decimal) 10.5, 11, 13};
                            
                            resultsDictionary.Add(result.Id, result);
                        }
                    }

                    return resultsDictionary.Values;
                }
            }
        }

        private static Meeting GetMeeting(MySqlDataReader reader)
        {
            return new Meeting
            {
                Id = reader.GetInt32("meeting_id"),
                Number = reader.GetInt32("meeting_number"),
                Date = reader.GetDateTimeOffset("date")
            };
        }
    }
}