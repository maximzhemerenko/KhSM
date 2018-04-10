using System.Collections.Generic;
using System.Linq;
using System.Net;
using Backend.Data.Entities;
using Backend.Domain;
using Microsoft.AspNetCore.Mvc;

namespace Backend.Controllers
{
    public class NewsController : ApiController
    {
        private readonly NewsManager _newsManager;
        private readonly ResultsManager _resultsManager;

        public NewsController(NewsManager newsManager, ResultsManager resultsManager, UsersManager usersManager) : base(usersManager)
        {
            _newsManager = newsManager;
            _resultsManager = resultsManager;
        }
        
        [HttpGet]
        public IEnumerable<News> Get()
        {
            return _newsManager.GetNewsAsyns();
        }
        
        [HttpGet("{id}")]
        [ProducesResponseType(typeof(News), (int)HttpStatusCode.OK)]
        [ProducesResponseType((int)HttpStatusCode.NotFound)]
        public IActionResult Get(int id)
        {
            var meeting = _newsManager.GetNewsAsyns();
            if (meeting == null)
                return NotFound();
            
            return Json(meeting);
        }
        
        [HttpGet("last")]
        [ProducesResponseType(typeof(News), (int)HttpStatusCode.OK)]
        [ProducesResponseType((int)HttpStatusCode.NotFound)]
        public IActionResult GetLast()
        {
            var meeting = _newsManager.GetNewsAsyns();
            if (meeting == null)
                return NotFound();
            
            return Json(meeting);
        }
        
        [HttpPost]
        public IActionResult AddNews([FromBody] News news)
        {
            var user = User;
            if (user == null)
                return Unauthorized();
            
            if (!user.Roles.Contains("Admin"))
                return Unauthorized();

            news.User = user;
            
            _newsManager.AddNews(news);

            return Json(news);
        }
    }
}