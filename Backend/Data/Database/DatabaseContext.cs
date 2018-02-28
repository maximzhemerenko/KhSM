using System;
using MySql.Data.MySqlClient;

namespace Backend.Data.Database
{
    // ReSharper disable once ClassNeverInstantiated.Global
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

        public void UseTransaction(Action<MySqlTransaction> action)
        {
            MySqlTransaction transaction = null;
            try
            {
                transaction = Connection.BeginTransaction();
                action(transaction);
                transaction.Commit();
            }
            catch
            {
                transaction?.Rollback();
                throw;
            }
            finally
            {
                transaction?.Dispose();
            }
        }

        public T UseTransaction<T>(Func<MySqlTransaction, T> action)
        {
            MySqlTransaction transaction = null;
            try
            {
                transaction = Connection.BeginTransaction();
                var value = action(transaction);
                transaction.Commit();
                return value;
            }
            catch
            {
                transaction?.Rollback();
                throw;
            }
            finally
            {
                transaction?.Dispose();
            }
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