using System.Collections.Generic;
using System.Threading.Tasks;
using Backend.Data.Database;
using Backend.Data.Entities;
using MySql.Data.MySqlClient;

namespace Backend.Data.Repositories
{
    // ReSharper disable once ClassNeverInstantiated.Global
    public class UsersRepository : BaseRepository
    {
        public UsersRepository(DatabaseContext databaseContext) : base(databaseContext)
        {
        }

        public async Task<IEnumerable<User>> GetUsersAsync()
        {
            using (var transaction = await Connection.BeginTransactionAsync())
            using (var command = new MySqlCommand("select * from user", Connection, transaction))
            using (var reader = await command.ExecuteReaderAsync())
            {
                var users = new List<User>();

                while (await reader.ReadAsync())
                {
                    users.Add(new User
                    {
                        Id = reader.GetInt32(reader.GetOrdinal("user_id")),
                        FirstName = reader.GetString(reader.GetOrdinal("first_name"))
                    });
                }

                return users;
            }
        }
    }
}