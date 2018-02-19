using System;
using Microsoft.AspNetCore.Mvc;
using MySql.Data.MySqlClient;

namespace Backend.Data.Database
{
    public class DatabaseContext : IDisposable
    {
        public MySqlConnection Connection { get; }
        
        public DatabaseContext()
        {
            Connection = CreateConnection();
            Connection.Open();
        }

        public void Dispose()
        {
            Connection.Dispose();
        }

        private static MySqlConnection CreateConnection()
        {
            return new MySqlConnection(ConectionString);
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

    }
}