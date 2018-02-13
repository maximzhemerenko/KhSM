using MySql.Data.MySqlClient;

namespace Backend.Data.Database
{
    public class DatabaseContext
    {
        public MySqlConnection Connection { get; }
        
        public DatabaseContext(MySqlConnection connection)
        {
            Connection = connection;
        }

        private static string ConectionString =>
            new MySqlConnectionStringBuilder
            {
                Server = "localhost",
                Port = 3306,
                UserID = "kh_sm",
                Password = "sKiLlet",
                Database = "kh_sm"
            }.ToString();

        public static MySqlConnection CreateConnection()
        {
            return new MySqlConnection(ConectionString);
        }
    }
}