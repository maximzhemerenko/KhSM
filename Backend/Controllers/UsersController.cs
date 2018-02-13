using System.Collections.Generic;
using System.Threading.Tasks;
using Backend.Data.Database;
using Backend.Data.Entities;
using Backend.Domain;
using Microsoft.AspNetCore.Mvc;

namespace Backend.Controllers
{
    [Route("api/[controller]")]
    public class UsersController : Controller
    {
        [HttpGet]
        public async Task<IEnumerable<User>> Get()
        {
            using (var databaseContext = new DatabaseContext())
            {
                await databaseContext.Connection.OpenAsync();
                var usersManager = new UsersManager(databaseContext);
                return await usersManager.GetUsersAsync();
            }
        }
    }
}