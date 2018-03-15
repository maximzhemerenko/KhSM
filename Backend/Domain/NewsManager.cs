using System.Collections.Generic;
using Backend.Data.Entities;
using Backend.Data.Repositories;

namespace Backend.Domain
{
    public class NewsManager
    {
        private readonly NewsRepository _newsRepository;

        public NewsManager(NewsRepository newsRepository)
        {
            _newsRepository = newsRepository;
        }
        
        public IEnumerable<News> GetNewsAsyns()
        {
            return _newsRepository.GetNews();
        }
    }
}