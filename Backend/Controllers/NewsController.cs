using System.Collections.Generic;
using System.Net;
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
            if (!IsAdmin(out var user))
                return Unauthorized();

            news.User = user;
            
            _newsManager.AddNews(news);

            return Json(news);
        }
    }
}