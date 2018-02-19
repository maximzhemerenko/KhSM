using System.Collections.Generic;
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
        public IEnumerable<Meeting> Get()
        {
            return _meetingsManager.GetMeetingsAsync();
        }
    }
}