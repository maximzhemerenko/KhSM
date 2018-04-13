using System.Collections.Generic;
using System.Linq;
using System.Net;
using Backend.Data.Entities;
using Backend.Domain;
using Microsoft.AspNetCore.Mvc;

namespace Backend.Controllers
{
    public class MeetingsController : ApiController
    {
        private readonly MeetingsManager _meetingsManager;
        private readonly ResultsManager _resultsManager;
        
        public MeetingsController(MeetingsManager meetingsManager, ResultsManager resultsManager, UsersManager usersManager) : base(usersManager)
        {
            _meetingsManager = meetingsManager;
            _resultsManager = resultsManager;
        }

        [HttpGet]
        [ProducesResponseType(typeof(List<Meeting>), (int)HttpStatusCode.OK)]
        public IEnumerable<Meeting> Get()
        {
            return _meetingsManager.GetMeetings();
        }

        [HttpGet("{id}")]
        [ProducesResponseType(typeof(Meeting), (int)HttpStatusCode.OK)]
        [ProducesResponseType((int)HttpStatusCode.NotFound)]
        public IActionResult Get(int id)
        {
            var meeting = _meetingsManager.GetMeeting(id);
            if (meeting == null)
                return NotFound();
            
            return Json(meeting);
        }

        [HttpGet("last")]
        [ProducesResponseType(typeof(Meeting), (int)HttpStatusCode.OK)]
        [ProducesResponseType((int)HttpStatusCode.NotFound)]
        public IActionResult GetLast()
        {
            var meeting = _meetingsManager.GetLastMeeting();
            if (meeting == null)
                return NotFound();
            
            return Json(meeting);
        }

        [HttpGet("{id}/results")]
        [ProducesResponseType(typeof(IEnumerable<DisciplineResults>), (int)HttpStatusCode.OK)]
        [ProducesResponseType((int)HttpStatusCode.NotFound)]
        public IActionResult GetResults(int id)
        {
            var results = _resultsManager.GetMeetingResults(id);
            if (results == null)
                return NotFound();
            
            return Json(results);
        }

        [HttpPost]
        public IActionResult CreateMeeting([FromBody] Meeting meeting)
        {
            var user = User;
            if (user == null)
                return Unauthorized();
            
            if (!user.Roles.Contains("Admin"))
                return Unauthorized();

            _meetingsManager.AddMeeting(meeting);

            return Json(meeting);
        }

        [HttpPost("results")]
        public IActionResult CreateResult([FromBody] Result result)
        {
            var user = User;
            if (user == null)
                return Unauthorized();
            
            if (!user.Roles.Contains("Admin"))
                return Unauthorized();
            
            _resultsManager.AddResult(result);
            
            return Json(result);
        }
    }
}