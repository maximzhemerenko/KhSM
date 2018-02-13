using System.Collections.Generic;
using System.Threading.Tasks;
using Backend.Data.Entities;
using Backend.Data.Repositories;

namespace Backend.Domain
{
    // ReSharper disable once ClassNeverInstantiated.Global
    public class UsersManager
    {
        private readonly UsersRepository _usersRepository;

        public UsersManager(UsersRepository usersRepository)
        {
            _usersRepository = usersRepository;
        }

        public async Task<IEnumerable<User>> GetUsersAsync()
        {
            return await _usersRepository.GetUsersAsync();
        }
    }
}