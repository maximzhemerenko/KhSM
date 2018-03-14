using System.Collections.Generic;
using Backend.Data.Entities;
using Backend.Domain;
using Microsoft.AspNetCore.Mvc;

namespace Backend.Controllers
{
    public class NewsController : ApiController
    {
        private readonly NewsManager _newsManager;

        public NewsController(NewsManager newsManager, UsersManager usersManager) : base(usersManager)
        {
            _newsManager = newsManager;
        }
        
        [HttpGet]
        public IEnumerable<News> Get()
        {
            return _newsManager.GetNewsAsyns();
        }
    }
}