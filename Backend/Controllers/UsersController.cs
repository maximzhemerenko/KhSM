using System.Net;
using Backend.Data.Entities;
using Backend.Domain;
using Microsoft.AspNetCore.Mvc;

namespace Backend.Controllers
{
    public class UsersController : ApiController
    {
        private readonly UsersManager _usersManager;

        public UsersController(UsersManager usersManager) : base(usersManager)
        {
            _usersManager = usersManager;
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
    }
}