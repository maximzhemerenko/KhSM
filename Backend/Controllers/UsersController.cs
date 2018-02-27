using Backend.Data.Entities;
using Backend.Domain;
using Microsoft.AspNetCore.Mvc;

namespace Backend.Controllers
{
    public class UsersController : ApiController
    {
        private readonly UsersManager _usersManager;

        public UsersController(UsersManager usersManager)
        {
            _usersManager = usersManager;
        }
        
        [HttpPost]
        public Session Register(CreateUserRequest createUserRequest)
        {
            _usersManager.Register(createUserRequest);
            return null;
        }
    }
}