using Backend.Data.Database;
using MySql.Data.MySqlClient;

namespace Backend.Data.Repositories
{
    public abstract class BaseRepository
    {
        protected BaseRepository(DatabaseContext databaseContext)
        {
            DatabaseContext = databaseContext;
        }

        private DatabaseContext DatabaseContext { get; }
        protected MySqlConnection Connection => DatabaseContext.Connection;
    }
}