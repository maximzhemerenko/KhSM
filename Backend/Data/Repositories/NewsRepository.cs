using System.Collections.Generic;
using Backend.Data.Database;
using Backend.Data.Entities;
using MySql.Data.MySqlClient;

namespace Backend.Data.Repositories
{
    public class NewsRepository : BaseRepository
    {
        public NewsRepository(DatabaseContext databaseContext) : base(databaseContext)
        {
        }

        public IEnumerable<News> GetNews()
        {
            using (var command = new MySqlCommand("select * from news n " +
                                                  "inner join user u on n.user_id = u.user_id;", Connection))
            using (var reader = command.ExecuteReader())
            {
                var news = new List<News>();

                while (reader.Read())
                {
                    news.Add(GetNews(reader));
                }

                return news;
                
                // return ReadNews(reader);
            }
        }
        
        public News GetNews(int id)
        {
            using (var command =
                new MySqlCommand("select * from news where news_id = @news_id", Connection)
                {
                    Parameters = {new MySqlParameter("news_id", id)}
                })
                
            using (var reader = command.ExecuteReader())
            {
                return ReadNews(reader);
            }
        }
        
        public News GetLastNews()
        {
            using (var command = new MySqlCommand("select * from news order by date desc limit 1", Connection))
            using (var reader = command.ExecuteReader())
            {
                return ReadNews(reader);
            }
        }
        
        public static News GetNews(MySqlDataReader reader)
        {
            return new News
            {
                Id = reader.GetInt32("news_id"),
                User = UserRepository.GetUser(reader, false),
                Text = reader.GetString("text"),
                DateAndTime = reader.GetDateTime("date_and_time")
            };
        }
        
        public void AddNews(News news, MySqlTransaction transaction)
        {
            const string userIdKey = "user_id";
            const string messageNewsTextKey = "text";
            const string addNewsDateKey = "date_and_time";
            
            using (var command = new MySqlCommand(Connection, transaction)
            {
                CommandText = $"insert into news({userIdKey},{messageNewsTextKey}, {addNewsDateKey}) " +
                              $"values(@{userIdKey}, @{messageNewsTextKey}, @{addNewsDateKey})",
                Parameters =
                {
                    new MySqlParameter(userIdKey, news.User.Id),
                    new MySqlParameter(messageNewsTextKey, news.Text),
                    new MySqlParameter(addNewsDateKey, news.DateAndTime)
                }
            })
            {
                command.ExecuteNonQuery();

                news.Id = (int) command.LastInsertedId;
            }
        }
        
        public static News ReadNews(MySqlDataReader reader)
        {
            return reader.Read() ? GetNews(reader) : null;
        }
    }
}