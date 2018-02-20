using System.Collections.Generic;
using System.Net;
using Backend.Data.Entities;
using Backend.Domain;
using Microsoft.AspNetCore.Mvc;

namespace Backend.Controllers
{
    public class MeetingsController : ApiController
    {
        private readonly MeetingsManager _meetingsManager;

        public MeetingsController(MeetingsManager meetingsManager)
        {
            _meetingsManager = meetingsManager;
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

        [HttpGet("{id}/results")]
        [ProducesResponseType(typeof(IEnumerable<DisciplineResults>), (int)HttpStatusCode.OK)]
        [ProducesResponseType((int)HttpStatusCode.NotFound)]
        public IActionResult GetResults(int id)
        {
            var disciplineResultses = _meetingsManager.GetMeetingResults(id);
            if (disciplineResultses == null)
                return NotFound();
            
            return Json(disciplineResultses);
        }
    }
}