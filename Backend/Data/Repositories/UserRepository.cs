using System;
using System.Collections.Generic;
using Backend.Data.Database;
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
        
        public IEnumerable<User> GetUsers()
        {
            using (var command = new MySqlCommand("select * from user", Connection))
            using (var reader = command.ExecuteReader())
            {
                var users = new List<User>();

                while (reader.Read())
                {
                    users.Add(GetUser(reader));
                }

                return users;
            }
        }
        
        public static User GetUser(MySqlDataReader reader)
        {
            return new User
            {
                Id = reader.GetInt32("user_id"),
                FirstName = reader.GetString("first_name"),
                LastName = reader.GetString("last_name"),
                City = !reader.IsDBNull(reader.GetOrdinal("city")) ? reader.GetString("city") : null,
                WCAID = !reader.IsDBNull(reader.GetOrdinal("wca_id")) ? reader.GetString("wca_id") : null,
                PhoneNumber = !reader.IsDBNull(reader.GetOrdinal("phone_number")) ? reader.GetString("phone_number") : null,
                Gender = ParseGenderString(reader.GetString("gender")),
                BirthDate = !reader.IsDBNull(reader.GetOrdinal("birth_date")) ? (DateTimeOffset?)reader.GetDateTimeOffset("birth_date") : null,
                Approved =  !reader.IsDBNull(reader.GetOrdinal("approved")) ? (DateTimeOffset?)reader.GetDateTimeOffset("approved") : null
            };
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
            
            using (var command = new MySqlCommand(Connection, transaction)
            {
                CommandText = $"insert into user({firstNameKey}, {lastNameKey}, {genderKey}) " +
                              $"values(@{firstNameKey}, @{lastNameKey}, @{genderKey})",
                Parameters =
                {
                    new MySqlParameter(firstNameKey, user.FirstName),
                    new MySqlParameter(lastNameKey, user.LastName),
                    new MySqlParameter(genderKey, user.Gender == Gender.Male ? "male" : "female")
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
    }
}