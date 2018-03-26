using System;
using System.Collections.Generic;
using System.Diagnostics;
using System.Text;
using Backend.Data.Database;
using Backend.Data.Database.Entities;
using Backend.Data.Entities;
using MySql.Data.MySqlClient;

namespace Backend.Data.Repositories
{
    // ReSharper disable once ClassNeverInstantiated.Global
    public class UserRepository : BaseRepository
    {
        public UserRepository(DatabaseContext databaseContext) : base(databaseContext)
        {
        }
        
        public IEnumerable<User> GetUsers(bool readPrivateFields)
        {
            using (var command = new MySqlCommand("select * from user", Connection))
            using (var reader = command.ExecuteReader())
            {
                var users = new List<User>();

                while (reader.Read())
                {
                    users.Add(GetUser(reader, readPrivateFields));
                }

                return users;
            }
        }

        public User GetUser(int id, bool readPrivateFields, MySqlTransaction transaction = null)
        {
            User user;
            using (var command = new MySqlCommand(Connection, transaction)
            {
                CommandText = $"select * from user where {Db.User.UserIdKey} = @{Db.User.UserIdKey}",
                Parameters =
                {
                    new MySqlParameter(Db.User.UserIdKey, id)
                }
            })
            using (var reader = command.ExecuteReader())
            {
                user = reader.Read() ? GetUser(reader, readPrivateFields) : null;
            }

            if (user == null)
                return null;

            Debug.Assert(user.Id != null, "user.Id != null");
            user.Roles = ReadRoles(user.Id.Value, transaction);
            
            return user;
        }

        public User GetUserByEmail(string email, bool readPrivateFields, MySqlTransaction transaction)
        {
            User user;
            using (var command = new MySqlCommand(Connection, transaction)
            {
                CommandText = $"select * from user where {Db.User.EmailKey} = @{Db.User.EmailKey}",
                Parameters =
                {
                    new MySqlParameter(Db.User.EmailKey, email)
                }
            })
            using (var reader = command.ExecuteReader())
            {
                user = reader.Read() ? GetUser(reader, readPrivateFields) : null;
            }

            if (user == null) return null;

            Debug.Assert(user.Id != null, "user.Id != null");
            user.Roles = ReadRoles(user.Id.Value, transaction);

            return user;
        }
        
        public void UpdateUser(User user, MySqlTransaction transaction)
        {
            Debug.Assert(user.Id != null, "user.Id != null");
            
            using (var command = new MySqlCommand(Connection, transaction))
            {
                var parameters = new List<(string key, object value)>
                {
                    (Db.User.FirstNameKey, user.FirstName),
                    (Db.User.LastNameKey, user.LastName),
                    (Db.User.CityKey, user.City),
                    (Db.User.WcaIdKey, user.WCAID),
                    (Db.User.PhoneNumberKey, user.PhoneNumber),
                    (Db.User.GenderKey, GenderToSqlString(user.Gender)),
                    (Db.User.BirthDateKey, user.BirthDate)
                };
                
                var sb = new StringBuilder("update user set").AppendLine();
                
                for (var i = 0; i < parameters.Count; i++)
                {
                    var pair = parameters[i];
                    var key = pair.key;
                    var value = pair.value;
                    
                    sb.Append($"  {key} = @{key}");
                    if (i < parameters.Count - 1)
                        sb.Append(",");
                    sb.AppendLine();
                    
                    command.Parameters.Add(new MySqlParameter(key, value));
                }

                sb.AppendLine($"where {Db.User.UserIdKey} = @{Db.User.UserIdKey}");
                command.Parameters.Add(new MySqlParameter(Db.User.UserIdKey, user.Id.Value));

                command.CommandText = sb.ToString();

                command.ExecuteNonQuery();
            }
        }

        public static User GetUser(MySqlDataReader reader, bool readPrivateFields, bool readAdminFields = false)
        {
            var user = new User
            {
                Id = reader.GetInt32("user_id"),
                FirstName = reader.GetString("first_name"),
                LastName = reader.GetString("last_name"),
                Gender = ParseGenderString(reader.GetString("gender"))
            };

            if (readPrivateFields) 
            {
                user.City = !reader.IsDBNull(reader.GetOrdinal("city")) ? reader.GetString("city") : null;
                user.WCAID = !reader.IsDBNull(reader.GetOrdinal("wca_id")) ? reader.GetString("wca_id") : null;
                user.PhoneNumber = !reader.IsDBNull(reader.GetOrdinal("phone_number")) ? reader.GetString("phone_number") : null;
                user.BirthDate = !reader.IsDBNull(reader.GetOrdinal("birth_date")) ? (DateTimeOffset?)reader.GetDateTimeOffset("birth_date") : null;
            }

            if (readAdminFields)
            {
                user.Approved =  !reader.IsDBNull(reader.GetOrdinal("approved")) ? (DateTimeOffset?)reader.GetDateTimeOffset("approved") : null;    
            }
            
            return user;
        }

        private static Gender ParseGenderString(string genderString)
        {
            if (!Enum.TryParse(genderString, true, out Gender gender))
                throw new Exception();

            return gender;
        }

        public void AddUser(User user, MySqlTransaction transaction)
        {
            Debug.Assert(user.Gender != null, "user.Gender != null");
            
            using (var command = new MySqlCommand(Connection, transaction)
            {
                CommandText = $"insert into user({Db.User.FirstNameKey}, {Db.User.LastNameKey}, {Db.User.GenderKey}, {Db.User.EmailKey}) " +
                              $"values(@{Db.User.FirstNameKey}, @{Db.User.LastNameKey}, @{Db.User.GenderKey}, @{Db.User.EmailKey})",
                Parameters =
                {
                    new MySqlParameter(Db.User.FirstNameKey, user.FirstName),
                    new MySqlParameter(Db.User.LastNameKey, user.LastName),
                    new MySqlParameter(Db.User.GenderKey, GenderToSqlString(user.Gender.Value)),
                    new MySqlParameter(Db.User.EmailKey, user.Email)
                }
            })
            {
                command.ExecuteNonQuery();

                user.Id = (int) command.LastInsertedId;
            }
        }

        public void AddLogin(User user, byte[] passwordHash, MySqlTransaction transaction)
        {
            using (var command = new MySqlCommand(Connection, transaction)
            {
                CommandText = $"insert into login({Db.User.UserIdKey}, {Db.User.PasswordHashKey}) " +
                              $"values(@{Db.User.UserIdKey}, @{Db.User.PasswordHashKey})",
                Parameters =
                {
                    new MySqlParameter(Db.User.UserIdKey, user.Id),
                    new MySqlParameter(Db.User.PasswordHashKey, passwordHash)
                }
            })
            {
                command.ExecuteNonQuery();

                user.Id = (int) command.LastInsertedId;
            }
        }

        public Login GetLogin(int userId, MySqlTransaction transaction)
        {
            using (var command = new MySqlCommand(Connection, transaction)
            {
                CommandText = $"select * from login where {Db.User.UserIdKey} = @{Db.User.UserIdKey}",
                Parameters =
                {
                    new MySqlParameter(Db.User.UserIdKey, userId)
                }
            })
            using (var reader = command.ExecuteReader())
            {
                return reader.Read() ? GetLogin(reader) : null;
            }
        }

        private static Login GetLogin(MySqlDataReader reader)
        {
            var login = new Login
            {
                Id = reader.GetInt32("user_id")
            };
            
            var buffer = new byte[20];

            var read = reader.GetBytes(reader.GetOrdinal("password_hash"), 0, buffer, 0, buffer.Length);

            login.Hash = new byte[read];

            for (var i = 0; i < read; i++)
            {
                login.Hash[i] = buffer[i];
            }
            
            return login;
        }

        private static string GenderToSqlString(Gender? gender)
        {
            if (gender == null) return null;
            
            return gender == Gender.Male ? "male" : "female";
        }

        private IEnumerable<string> ReadRoles(int userId, MySqlTransaction transaction)
        {
            using (var command = new MySqlCommand(Connection, transaction)
            {
                CommandText =
                    $"select r.name from user_role ur join role r on ur.role_id = r.role_id where {Db.User.UserIdKey} = @{Db.User.UserIdKey}",
                Parameters =
                {
                    new MySqlParameter(Db.User.UserIdKey, userId)
                }
            })
            using (var reader = command.ExecuteReader())
            {
                var roles = new List<string>();
                while (reader.Read())
                {
                    roles.Add(reader["name"].ToString());
                }

                return roles;
            }
        }
    }
}