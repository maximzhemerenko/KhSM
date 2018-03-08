using System;
using System.Collections.Generic;
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
            const string userIdKey = "user_id";
            
            using (var command = new MySqlCommand(Connection, transaction)
            {
                CommandText = $"select * from user where {userIdKey} = @{userIdKey}",
                Parameters =
                {
                    new MySqlParameter(userIdKey, id)
                }
            })
            using (var reader = command.ExecuteReader())
            {
                return reader.Read() ? GetUser(reader, readPrivateFields) : null;
            }
        }

        public User GetUserByEmail(string email, bool readPrivateFields, MySqlTransaction transaction)
        {
            const string emailKey = "email";
            
            using (var command = new MySqlCommand(Connection, transaction)
            {
                CommandText = $"select * from user where {emailKey} = @{emailKey}",
                Parameters =
                {
                    new MySqlParameter(emailKey, email)
                }
            })
            using (var reader = command.ExecuteReader())
            {
                return reader.Read() ? GetUser(reader, readPrivateFields) : null;
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

        public static Gender ParseGenderString(string genderString)
        {
            if (!Enum.TryParse(genderString, true, out Gender gender))
                throw new Exception();

            return gender;
        }

        public void AddUser(User user, MySqlTransaction transaction)
        {
            const string firstNameKey = "first_name";
            const string lastNameKey = "last_name";
            const string genderKey = "gender";
            const string emailKey = "email";
            
            using (var command = new MySqlCommand(Connection, transaction)
            {
                CommandText = $"insert into user({firstNameKey}, {lastNameKey}, {genderKey}, {emailKey}) " +
                              $"values(@{firstNameKey}, @{lastNameKey}, @{genderKey}, @{emailKey})",
                Parameters =
                {
                    new MySqlParameter(firstNameKey, user.FirstName),
                    new MySqlParameter(lastNameKey, user.LastName),
                    new MySqlParameter(genderKey, user.Gender == Gender.Male ? "male" : "female"),
                    new MySqlParameter(emailKey, user.Email)
                }
            })
            {
                command.ExecuteNonQuery();

                user.Id = (int) command.LastInsertedId;
            }
        }

        public void AddLogin(User user, byte[] passwordHash, MySqlTransaction transaction)
        {
            const string userIdKey = "user_id";
            const string passwordHashKey = "password_hash";

            using (var command = new MySqlCommand(Connection, transaction)
            {
                CommandText = $"insert into login({userIdKey}, {passwordHashKey}) " +
                              $"values(@{userIdKey}, @{passwordHashKey})",
                Parameters =
                {
                    new MySqlParameter(userIdKey, user.Id),
                    new MySqlParameter(passwordHashKey, passwordHash)
                }
            })
            {
                command.ExecuteNonQuery();

                user.Id = (int) command.LastInsertedId;
            }
        }

        public Login GetLogin(int userId, MySqlTransaction transaction)
        {
            const string userIdKey = "user_id";
            
            using (var command = new MySqlCommand(Connection, transaction)
            {
                CommandText = $"select * from login where {userIdKey} = @{userIdKey}",
                Parameters =
                {
                    new MySqlParameter(userIdKey, userId)
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
    }
}