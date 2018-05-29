using Backend.Data.Entities;
using Backend.Domain;
using Microsoft.AspNetCore.Mvc;

namespace Backend.Controllers
{
    public class RankingsController : ApiController
    {
        private readonly ResultsManager _resultsManager;
        
        public RankingsController(UsersManager usersManager, ResultsManager resultsManager) : base(usersManager)
        {
            _resultsManager = resultsManager;
        }

        [HttpGet]
        public IActionResult GetRankings([FromQuery] ResultsManager.FilterType type, [FromQuery] ResultsManager.SortType sort, [FromQuery] Gender? gender)
        {
            var results = _resultsManager.GetRankings(type, sort, gender);
            if (results == null)
                return NotFound();
            
            return Json(results);
        }
    }
}