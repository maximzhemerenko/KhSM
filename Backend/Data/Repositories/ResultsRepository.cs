using System.Collections.Generic;
using System.Linq;
using Backend.Data.Database;
using Backend.Data.Database.Entities;
using Backend.Data.Entities;
using MySql.Data.MySqlClient;

namespace Backend.Data.Repositories
{
    // ReSharper disable once ClassNeverInstantiated.Global
    public class ResultsRepository : BaseRepository
    {
        public ResultsRepository(DatabaseContext databaseContext) : base(databaseContext)
        {
        }

        public IEnumerable<Result> GetMeetingResults(int meetingId, bool readMeeting = false, bool readDiscipline = false, bool readUser = false)
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

                // read results from meeting
                command.CommandText = $"select * from meeting_results where {meetingIdKey} = @{meetingIdKey}";

                using (var reader = command.ExecuteReader())
                {
                    var resultAttemptsDictionary = new Dictionary<Result, List<Attempt>>();

                    while (reader.Read())
                    {
                        var result = GetResult(reader);
                        List<Attempt> attempts;
                        
                        if (!resultAttemptsDictionary.ContainsKey(result))
                        {
                            if (readMeeting)
                                result.Meeting = MeetingsRepository.GetMeeting(reader);
                            if (readDiscipline)
                                result.Discipline = DisciplinesRepository.GetDiscipline(reader);
                            if (readUser)
                                result.User = UserRepository.GetUser(reader);
                            
                            attempts = new List<Attempt>();
                            
                            resultAttemptsDictionary.Add(result, attempts);
                        }
                        else
                        {
                            attempts = resultAttemptsDictionary[result];
                        }

                        var attempt = GetAttempt(reader);
                        if (attempt != null)
                        {
                            attempts.Add(attempt);                            
                        }
                    }

                    return resultAttemptsDictionary.Select(pair =>
                    {
                        var result = pair.Key;
                        
                        result.Attempts = pair.Value.Select(attempt => attempt.Time);
                        
                        return result;
                    });
                }
            }
        }

        private static Result GetResult(MySqlDataReader reader)
        {
            return new Result
            {
                Id = reader.GetInt32("result_id"),
                Average = !reader.IsDBNull(reader.GetOrdinal("average")) ? (decimal?)reader.GetDecimal("average") : null
            };
        }

        private static Attempt GetAttempt(MySqlDataReader reader)
        {
            const string attemptIdKey = "attempt_id";

            if (reader.IsDBNull(reader.GetOrdinal(attemptIdKey)))
                return null;
            
            return new Attempt
            {
                Id = reader.GetInt32(attemptIdKey),
                Time = !reader.IsDBNull(reader.GetOrdinal("time")) ? (decimal?)reader.GetDecimal("time") : null
            };
        }
    }
}