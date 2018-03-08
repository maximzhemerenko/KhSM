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
        private const string UserIdKey = "user_id";
        private const string FirstNameKey = "first_name";
        private const string LastNameKey = "last_name";
        private const string GenderKey = "gender";
        private const string EmailKey = "email";
        private const string PasswordHashKey = "password_hash";
        private const string CityKey = "city";
        private const string WcaIdKey = "wca_id";
        private const string PhoneNumberKey = "phone_number";
        private const string BirthDateKey = "birth_date";
        
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
            using (var command = new MySqlCommand(Connection, transaction)
            {
                CommandText = $"select * from user where {UserIdKey} = @{UserIdKey}",
                Parameters =
                {
                    new MySqlParameter(UserIdKey, id)
                }
            })
            using (var reader = command.ExecuteReader())
            {
                return reader.Read() ? GetUser(reader, readPrivateFields) : null;
            }
        }

        public User GetUserByEmail(string email, bool readPrivateFields, MySqlTransaction transaction)
        {
            using (var command = new MySqlCommand(Connection, transaction)
            {
                CommandText = $"select * from user where {EmailKey} = @{EmailKey}",
                Parameters =
                {
                    new MySqlParameter(EmailKey, email)
                }
            })
            using (var reader = command.ExecuteReader())
            {
                return reader.Read() ? GetUser(reader, readPrivateFields) : null;
            }
        }
        
        public void UpdateUser(User user, MySqlTransaction transaction)
        {
            Debug.Assert(user.Id != null, "user.Id != null");
            
            using (var command = new MySqlCommand(Connection, transaction))
            {
                var parameters = new List<(string key, MySqlParameter parameter)>();
                
                if (user.FirstName != null)
                {
                    parameters.Add((FirstNameKey, new MySqlParameter(FirstNameKey, user.FirstName)));
                }
                
                if (user.LastName != null)
                {
                    parameters.Add((LastNameKey, new MySqlParameter(LastNameKey, user.LastName)));
                }
                
                if (user.City != null)
                {
                    parameters.Add((CityKey, new MySqlParameter(CityKey, user.City)));
                }
                
                if (user.WCAID != null)
                {
                    parameters.Add((WcaIdKey, new MySqlParameter(WcaIdKey, user.WCAID)));
                }
                
                if (user.PhoneNumber != null)
                {
                    parameters.Add((PhoneNumberKey, new MySqlParameter(PhoneNumberKey, user.PhoneNumber)));
                }
                
                if (user.Gender != null)
                {
                    parameters.Add((GenderKey, new MySqlParameter(GenderKey, GenderToSqlString(user.Gender.Value))));
                }

                if (user.BirthDate != null)
                {
                    parameters.Add((BirthDateKey, new MySqlParameter(BirthDateKey, user.BirthDate)));
                }
                
                if (parameters.Count < 1)
                    return;

                var sb = new StringBuilder("update user set").AppendLine();
                
                for (var i = 0; i < parameters.Count; i++)
                {
                    var pair = parameters[i];
                    var parameter = pair.parameter;
                    
                    sb.Append($"  {pair.key} = @{parameter.ParameterName}");
                    if (i < parameters.Count - 1)
                        sb.Append(",");
                    sb.AppendLine();
                    
                    command.Parameters.Add(parameter);
                }

                sb.AppendLine($"where {UserIdKey} = @{UserIdKey}");
                command.Parameters.Add(new MySqlParameter(UserIdKey, user.Id.Value));

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
                CommandText = $"insert into user({FirstNameKey}, {LastNameKey}, {GenderKey}, {EmailKey}) " +
                              $"values(@{FirstNameKey}, @{LastNameKey}, @{GenderKey}, @{EmailKey})",
                Parameters =
                {
                    new MySqlParameter(FirstNameKey, user.FirstName),
                    new MySqlParameter(LastNameKey, user.LastName),
                    new MySqlParameter(GenderKey, GenderToSqlString(user.Gender.Value)),
                    new MySqlParameter(EmailKey, user.Email)
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
                CommandText = $"insert into login({UserIdKey}, {PasswordHashKey}) " +
                              $"values(@{UserIdKey}, @{PasswordHashKey})",
                Parameters =
                {
                    new MySqlParameter(UserIdKey, user.Id),
                    new MySqlParameter(PasswordHashKey, passwordHash)
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
                CommandText = $"select * from login where {UserIdKey} = @{UserIdKey}",
                Parameters =
                {
                    new MySqlParameter(UserIdKey, userId)
                }
            })
            using (var reader = command.ExecuteReader())
            {
                return reader.Read() ? GetLogin(reader) : null;
            }
        }

        private Login GetLogin(MySqlDataReader reader)
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

        private static string GenderToSqlString(Gender gender)
        {
            return gender == Gender.Male ? "male" : "female";
        }
    }
}