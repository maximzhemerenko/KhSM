using System.Collections.Generic;
using System.Linq;
using Backend.Data.Database;
using Backend.Data.Database.Entities;
using Backend.Data.Entities;
using MySql.Data.MySqlClient;

namespace Backend.Data.Repositories
{
    public class ResultsRepository : BaseRepository
    {
        public ResultsRepository(DatabaseContext databaseContext) : base(databaseContext)
        {
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

                // read results from meeting results
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
                        
                        result.Attemts = pair.Value.Select(attempt => attempt.Time);
                        
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
                Average = reader.GetFieldValue<decimal?>(reader.GetOrdinal("average"))
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
                Time = reader.GetFieldValue<decimal?>(reader.GetOrdinal("time"))
            };
        }
    }
}