using System.Collections.Generic;
using System.Threading.Tasks;
using Backend.Data.Database;
using Backend.Data.Entities;
using Backend.Data.Repositories;

namespace Backend.Domain
{
    public class UsersManager
    {
        private readonly UsersRepository _usersRepository;

        public UsersManager(DatabaseContext databaseContext)
        {
            _usersRepository = new UsersRepository(databaseContext);
        }

        public async Task<IEnumerable<User>> GetUsersAsync()
        {
            return await _usersRepository.GetUsersAsync();
        }
    }
}