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
            if (!IsAuthenticated(out var user))
                return Unauthorized();
            
            return Json(user);
        }
        
        [HttpGet]
        public IActionResult GetUsers()
        {
            if (!IsAdmin())
                return Unauthorized();
            
            return Json(_usersManager.GetUsers(false));
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
            if (!IsAuthenticated(out var me))
                return Unauthorized();

            Debug.Assert(me.Id != null, "me.Id != null");
            var id = me.Id.Value;
            
            if (user.Id.HasValue && user.Id.Value != id)
                return Unauthorized();

            user.Id = id;
            
            return Json(_usersManager.UpdateUser(user));
        }

        [HttpPut("{id}/password")]
        [ProducesResponseType((int) HttpStatusCode.OK)]
        [ProducesResponseType((int) HttpStatusCode.Unauthorized)]
        public IActionResult UpdatePassword(int id, [FromBody] UpdatePasswordRequest updatePasswordRequest)
        {
            if (!IsAuthenticated(out var me))
                return Unauthorized();

            Debug.Assert(me.Id != null, "me.Id != null");
            if (me.Id.Value != id)
                return Unauthorized();

            _usersManager.UpdatePassword(id, updatePasswordRequest.Password);

            return Ok();
        }

        [HttpPut("me/password")]
        [ProducesResponseType((int) HttpStatusCode.OK)]
        [ProducesResponseType((int) HttpStatusCode.Unauthorized)]
        public IActionResult UpdatePassword([FromBody] UpdatePasswordRequest updatePasswordRequest)
        {
            if (!IsAuthenticated(out var me))
                return Unauthorized();

            Debug.Assert(me.Id != null, "me.Id != null");
            var id = me.Id.Value;

            _usersManager.UpdatePassword(id, updatePasswordRequest.Password);

            return Ok();
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
            if (!IsAuthenticated(out var me))
                return Unauthorized();

            Debug.Assert(me.Id != null, "me.Id != null");
            var id = me.Id.Value;
            
            var results = _resultsManager.GetUserResults(id);
            if (results == null)
                return NotFound();

            return Json(results);
        }

        [HttpGet("{id}/records")]
        [ProducesResponseType(typeof(IEnumerable<DisciplineRecord>), (int) HttpStatusCode.OK)]
        [ProducesResponseType((int) HttpStatusCode.Unauthorized)]
        public IActionResult GetUserRecords(int id)
        {            
            if (!IsMe(id))
                return Unauthorized();

            var records = _resultsManager.GetUserRecords(id);
            if (records == null)
                return NotFound();

            return Json(records);
        }
        
        [HttpGet("me/records")]
        [ProducesResponseType(typeof(IEnumerable<DisciplineRecord>), (int) HttpStatusCode.OK)]
        [ProducesResponseType((int) HttpStatusCode.Unauthorized)]
        public IActionResult GetUserRecords()
        {
            if (!IsAuthenticated(out var me))
                return Unauthorized();

            Debug.Assert(me.Id != null, "me.Id != null");
            var id = me.Id.Value;
            
            var records = _resultsManager.GetUserRecords(id);
            if (records == null)
                return NotFound();

            return Json(records);
        }
    }
}