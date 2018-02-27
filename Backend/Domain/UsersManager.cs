using System.Collections.Generic;
using System.Security.Cryptography;
using System.Text;
using Backend.Data.Database;
using Backend.Data.Entities;
using Backend.Data.Repositories;

namespace Backend.Domain
{
    // ReSharper disable once ClassNeverInstantiated.Global
    public class UsersManager
    {
        private readonly UserRepository _userRepository;
        private readonly DatabaseContext _databaseContext;

        public UsersManager(UserRepository userRepository, DatabaseContext databaseContext)
        {
            _userRepository = userRepository;
            _databaseContext = databaseContext;
        }

        public IEnumerable<User> GetUsers()
        {
            return _userRepository.GetUsers();
        }

        public void Register(CreateUserRequest createUserRequest)
        {
            _databaseContext.UseTransaction(transaction =>
            {
                _userRepository.AddUser(createUserRequest.User, transaction);
                _userRepository.AddLogin(createUserRequest.User, Hash(createUserRequest.Password), transaction);
            });
        }
        
        static byte[] Hash(string input)
        {
            using (var sha1 = new SHA1Managed())
            {
                var hash = sha1.ComputeHash(Encoding.UTF8.GetBytes(input));
                /*var sb = new StringBuilder(hash.Length * 2);

                foreach (byte b in hash)
                {
                    // can be "x2" if you want lowercase
                    sb.Append(b.ToString("X2"));
                }

                return sb.ToString();*/

                return hash;
            }
        }
    }
}