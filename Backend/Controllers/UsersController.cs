using System.Collections.Generic;
using System.Threading.Tasks;
using Backend.Data.Entities;
using Backend.Domain;
using Microsoft.AspNetCore.Mvc;

namespace Backend.Controllers
{
    [Route("api/[controller]")]
    public class UsersController : ApiController
    {
        private readonly UsersManager _usersManager;

        public UsersController(UsersManager usersManager)
        {
            _usersManager = usersManager;
        }

        [HttpGet]
        public async Task<IEnumerable<User>> Get()
        {
            var users = await _usersManager.GetUsersAsync();
            return users;
        }
    }
}