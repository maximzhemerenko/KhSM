using System;
using Backend.Data.Database;
using Backend.Data.Entities;
using MySql.Data.MySqlClient;

namespace Backend.Data.Repositories
{
    public class UserRepository : BaseRepository
    {
        public UserRepository(DatabaseContext databaseContext) : base(databaseContext)
        {
        }

        public static User GetUser(MySqlDataReader reader)
        {
            return new User
            {
                Id = reader.GetInt32("result_id"),
                FirstName = reader.GetString("first_name"),
                LastName = reader.GetString("last_name"),
                City = !reader.IsDBNull(reader.GetOrdinal("city")) ? reader.GetString("city") : null,
                WCAID = !reader.IsDBNull(reader.GetOrdinal("wca_id")) ? reader.GetString("wca_id") : null,
                PhoneNumber = !reader.IsDBNull(reader.GetOrdinal("phone_number")) ? reader.GetString("phone_number") : null,
                Gender = reader.GetString("gender"),
                BirthDate = !reader.IsDBNull(reader.GetOrdinal("birth_date")) ? (DateTimeOffset?)reader.GetDateTimeOffset("birth_date") : null,
                Approved =  !reader.IsDBNull(reader.GetOrdinal("approved")) ? (DateTimeOffset?)reader.GetDateTimeOffset("approved") : null
            };
        }
    }
}