using System;
using System.Collections.Generic;
using System.Diagnostics;
using System.Net;
using Backend.Data.Entities;
using Backend.Domain;
using Microsoft.AspNetCore.Mvc;

namespace Backend.Controllers
{
    public class UsersController : ApiController
    {
        private readonly UsersManager _usersManager;
        private readonly ResultsManager _resultsManager;

        public UsersController(UsersManager usersManager, ResultsManager resultsManager) : base(usersManager)
        {
            _usersManager = usersManager;
            _resultsManager = resultsManager;
        }
        
        [HttpPost]
        public Session Register([FromBody] CreateUserRequest createUserRequest)
        {
            return _usersManager.Register(createUserRequest);
        }

        [HttpGet("{id}")]
        [ProducesResponseType(typeof(User), (int)HttpStatusCode.OK)]
        [ProducesResponseType((int)HttpStatusCode.NotFound)]
        public IActionResult GetUser(int id)
        {
            var readPrivateFields = id == User?.Id;
            
            var user = _usersManager.GetUser(id, readPrivateFields);
            if (user == null)
                return NotFound();
            
            return Json(user);
        }
        
        [HttpGet("me")]
        [ProducesResponseType(typeof(User), (int)HttpStatusCode.OK)]
        [ProducesResponseType((int)HttpStatusCode.Unauthorized)]
        public IActionResult GetUser()
        {
            var user = User;
            if (user == null)
                return Unauthorized();
            
            return Json(user);
        }

        [HttpPut("{id}")]
        [ProducesResponseType(typeof(User), (int) HttpStatusCode.OK)]
        [ProducesResponseType((int) HttpStatusCode.Unauthorized)]
        public IActionResult UpdateUser(int id, [FromBody] User user)
        {
            if (!IsMe(id))
                return Unauthorized();

            if (user.Id.HasValue && user.Id.Value != id)
                throw new Exception("Not consistent user id provided");

            user.Id = id;
            
            return Json(_usersManager.UpdateUser(user));
        }
        
        [HttpPut("me")]
        [ProducesResponseType(typeof(User), (int) HttpStatusCode.OK)]
        [ProducesResponseType((int) HttpStatusCode.Unauthorized)]
        public IActionResult UpdateUser([FromBody] User user)
        {
            var me = User;
            if (me == null)
                return Unauthorized();

            Debug.Assert(me.Id != null, "me.Id != null");
            var id = me.Id.Value;
            
            if (user.Id.HasValue && user.Id.Value != id)
                throw new Exception("Not consistent user id provided");

            user.Id = id;
            
            return Json(_usersManager.UpdateUser(user));
        }

        [HttpGet("{id}/results")]
        [ProducesResponseType(typeof(IEnumerable<DisciplineResults>), (int) HttpStatusCode.OK)]
        [ProducesResponseType((int) HttpStatusCode.Unauthorized)]
        public IActionResult GetUserResults(int id)
        {
            if (!IsMe(id))
                return Unauthorized();

            var results = _resultsManager.GetUserResults(id);
            if (results == null)
                return NotFound();

            return Json(results);
        }
        
        [HttpGet("me/results")]
        [ProducesResponseType(typeof(IEnumerable<DisciplineResults>), (int) HttpStatusCode.OK)]
        [ProducesResponseType((int) HttpStatusCode.Unauthorized)]
        public IActionResult GetUserResults()
        {
            var me = User;
            if (me == null)
                return Unauthorized();

            Debug.Assert(me.Id != null, "me.Id != null");
            var id = me.Id.Value;
            
            var results = _resultsManager.GetUserResults(id);
            if (results == null)
                return NotFound();

            return Json(results);
        }
    }
}