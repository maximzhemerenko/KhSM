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
                return ReadNews(reader);
            }
        }
        
        public static News GetNews(MySqlDataReader reader)
        {
            var news = new News
            {
                Id = reader.GetInt32("news_id"),
                User = UserRepository.GetUser(reader, false),
                Text = reader.GetString("text"),
                DateAndTime = reader.GetDateTimeOffset("date_and_time")
            };
            return news;
        }

        
        private static IEnumerable<News> ReadNews(MySqlDataReader reader)
        {
            var news = new List<News>();

            while (reader.Read())
            {
                news.Add(GetNews(reader));
            }

            return news;
        }
    }
}