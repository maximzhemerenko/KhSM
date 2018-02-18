using System;
using System.Collections.Generic;
using System.Threading.Tasks;
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

        [HttpGet]
        [ProducesResponseType(typeof(User), 200)]
        public async Task<IEnumerable<User>> Get()
        {
            return await _usersManager.GetUsersAsync();
        }
    }
}