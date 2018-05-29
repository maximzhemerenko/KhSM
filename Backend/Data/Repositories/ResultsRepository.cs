using System.Collections.Generic;
using System.Linq;
using System.Text;
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

        public IEnumerable<Result> GetResults((int? meetingId, int? userId) filter, bool readMeeting = false, bool readDiscipline = false, bool readUser = false)
        {
            using (var command = new MySqlCommand(Connection, null))
            {
                var whereClauseValues = new List<(string key, object value)>();
                
                // check is meeting exists in the db
                if (filter.meetingId != null)
                {
                    whereClauseValues.Add((Db.Meeting.MeetingIdKey, filter.meetingId.Value));
                    
                    command.Parameters.Clear();
                    command.Parameters.Add(new MySqlParameter(Db.Meeting.MeetingIdKey, filter.meetingId.Value));
                    
                    command.CommandText = $"select {Db.Meeting.MeetingIdKey} from {Db.MeetingKey} where {Db.Meeting.MeetingIdKey} = @{Db.Meeting.MeetingIdKey}";

                    var exists = (int?) command.ExecuteScalar();
                    if (!exists.HasValue)
                        return null;
                }
                
                // check is user exists in the db
                if (filter.userId != null)
                {
                    whereClauseValues.Add((Db.User.UserIdKey, filter.userId.Value));
                    
                    command.Parameters.Clear();
                    command.Parameters.Add(new MySqlParameter(Db.User.UserIdKey, filter.userId.Value));
                    
                    command.CommandText = $"select {Db.User.UserIdKey} from {Db.UserKey} where {Db.User.UserIdKey} = @{Db.User.UserIdKey}";

                    var exists = (int?) command.ExecuteScalar();
                    if (!exists.HasValue)
                        return null;
                }
                
                // read results
                command.Parameters.Clear();
                
                var whereClause = new StringBuilder();
                if (whereClauseValues.Count > 0)
                {
                    whereClause.Append("where");

                    for (var i = 0; i < whereClauseValues.Count; i++)
                    {
                        var pair = whereClauseValues[i];
                        
                        if (i > 0)
                            whereClause.Append(" and");

                        command.Parameters.Add(new MySqlParameter(pair.key, pair.value));
                        
                        whereClause.Append($" {pair.key} = @{pair.key}");
                    }
                }
                
                command.CommandText = $"select * from meeting_results {whereClause}";

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
                                result.User = UserRepository.GetUser(reader, false);
                            
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
                        
                        result.Attempts = pair.Value.Select(attempt => attempt.Time).ToList();
                        
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
                Average = !reader.IsDBNull(reader.GetOrdinal("average")) ? (decimal?)reader.GetDecimal("average") : null,
                AttemptCount = reader.GetInt32("attempt_count")
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

        public void AddResult(Result result, MySqlTransaction transaction)
        {
            const string userIdKey = "user_id";
            const string meetingIdKey = "meeting_id";
            const string disciplineIdKey = "discipline_id";
            const string averageKey = "average";
            const string attemptCountKey = "attempt_count";

            using (var command = new MySqlCommand(Connection, transaction)
            {
                CommandText = $"insert into result({averageKey}, {userIdKey}, {meetingIdKey}, {disciplineIdKey}, {attemptCountKey}) " +
                              $"values(@{averageKey}, @{userIdKey}, @{meetingIdKey}, @{disciplineIdKey}, @{attemptCountKey})",
                Parameters =
                {
                    new MySqlParameter(userIdKey, result.User.Id),
                    new MySqlParameter(meetingIdKey, result.Meeting.Id),
                    new MySqlParameter(disciplineIdKey, result.Discipline.Id),
                    new MySqlParameter(averageKey, result.Average),
                    new MySqlParameter(attemptCountKey, result.AttemptCount)
                }
            })
            {
                command.ExecuteNonQuery();

                result.Id = (int) command.LastInsertedId;
            }
            
            foreach (var attemptTime in result.Attempts)
            {
                var attempt = new Attempt
                {
                    Result = result,
                    Time = attemptTime
                };

                AddAttempt(attempt, transaction);
            }
        }

        private void AddAttempt(Attempt attempt, MySqlTransaction transaction)
        {
            const string resultIdKey = "result_id";
            const string timeKey = "time";
            
            using (var command = new MySqlCommand(Connection, transaction)
            {
                CommandText = $"insert into attempt({resultIdKey}, {timeKey}) " +
                              $"values(@{resultIdKey}, @{timeKey})",
                Parameters =
                {
                    new MySqlParameter(resultIdKey, attempt.Result.Id),
                    new MySqlParameter(timeKey, attempt.Time)
                }
            })
            {
                command.ExecuteNonQuery();

                attempt.Id = (int) command.LastInsertedId;
            }
        }
    }
}