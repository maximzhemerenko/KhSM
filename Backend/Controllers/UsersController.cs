using System.Collections.Generic;
using System.Threading.Tasks;
using Backend.Data.Database;
using Backend.Data.Entities;
using Backend.Data.Repositories;
using Microsoft.AspNetCore.Mvc;

namespace Backend.Controllers
{
    [Route("api/[controller]")]
    public class UsersController : Controller
    {
        [HttpGet]
        public async Task<IEnumerable<User>> Get()
        {
            using (var connection = DatabaseContext.CreateConnection())
            {
                await connection.OpenAsync();
                
                var databaseContext = new DatabaseContext(connection);
                var userRepository = new UserRepository(databaseContext);

                return await userRepository.GetUsersAsync();    
            }
        }
    }
}