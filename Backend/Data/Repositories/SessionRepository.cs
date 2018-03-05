using System;
using Backend.Data.Database;
using Backend.Data.Entities;
using MySql.Data.MySqlClient;

namespace Backend.Data.Repositories
{
    public class SessionRepository : BaseRepository
    {
        public SessionRepository(DatabaseContext databaseContext) : base(databaseContext)
        {
        }

        public void AddSession(Session session, MySqlTransaction transaction)
        {
            const string userIdKey = "user_id";
            const string tokenKey = "session_key";
            const string createdKey = "created";
         
            session.Created = DateTimeOffset.Now;
            
            using (var command = new MySqlCommand(Connection, transaction)
            {
                CommandText = $"insert into session({userIdKey}, {tokenKey}, {createdKey}) " +
                              $"values(@{userIdKey}, @{tokenKey}, @{createdKey})",
                Parameters =
                {
                    new MySqlParameter(userIdKey, session.User.Id),
                    new MySqlParameter(tokenKey, session.Token),
                    new MySqlParameter(createdKey, session.Created)
                }
            })
            {
                command.ExecuteNonQuery();
            }
        }
        
    }
}