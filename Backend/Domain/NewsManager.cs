using System.Collections.Generic;
using Backend.Data.Database;
using Backend.Data.Entities;
using Backend.Data.Repositories;

namespace Backend.Domain
{
    public class NewsManager
    {
        private readonly DatabaseContext _databaseContext;
        private readonly NewsRepository _newsRepository;

        public NewsManager(DatabaseContext databaseContext, NewsRepository newsRepository)
        {
            _databaseContext = databaseContext;
            _newsRepository = newsRepository;
        }
        
        public IEnumerable<News> GetNewsAsyns()
        {
            return _newsRepository.GetNews();
        }
        
        public News GetNews(int id)
        {
            return _newsRepository.GetNews(id);
        }

        public News GetLastNews()
        {
            return _newsRepository.GetLastNews();
        }
        
        public void AddNews(News news)
        {
            _databaseContext.UseTransaction(transaction =>
                _newsRepository.AddNews(news, transaction)
            );
        }
    }
}