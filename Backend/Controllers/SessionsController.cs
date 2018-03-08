using Backend.Data.Entities;
using Backend.Domain;
using Microsoft.AspNetCore.Mvc;

namespace Backend.Controllers
{
    public class SessionsController : ApiController
    {
        private readonly UsersManager _usersManager;

        public SessionsController(UsersManager usersManager) : base(usersManager)
        {
            _usersManager = usersManager;
        }

        [HttpPost]
        public Session Login([FromBody] CreateSessionRequest createSessionRequest)
        {
            return _usersManager.Login(createSessionRequest);
        }
    }
}