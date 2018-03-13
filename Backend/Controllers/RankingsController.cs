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
        public IActionResult GetRankings()
        {
            var results = _resultsManager.GetRankings();
            if (results == null)
                return NotFound();
            
            return Json(results);
        }
    }
}