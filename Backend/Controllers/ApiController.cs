using System;
using System.Linq;
using Backend.Data.Entities;
using Backend.Domain;
using Microsoft.AspNetCore.Mvc;
using Microsoft.AspNetCore.Mvc.Filters;

namespace Backend.Controllers
{
    [Route("api/[controller]")]
    [Produces("application/json")]
    public abstract class ApiController : Controller
    {
        private readonly UsersManager _usersManager;
        
        // ReSharper disable once MemberCanBePrivate.Global
        public Session Session { get; private set; }
        // ReSharper disable once UnusedMember.Global
        public new User User => Session?.User;

        protected ApiController(UsersManager usersManager)
        {
            _usersManager = usersManager;
        }

        public override void OnActionExecuting(ActionExecutingContext context)
        {
            base.OnActionExecuting(context);
         
            var authorization = Request.Headers["Authorization"].SingleOrDefault();

            Authenticate(authorization);
        }

        protected bool IsMe(int userId)
        {
            return User?.Id == userId;
        }

        protected bool IsAuthenticated()
        {
            // ReSharper disable once UnusedVariable
            return IsAuthenticated(out var user);
        }

        // ReSharper disable once MemberCanBePrivate.Global
        protected bool IsAuthenticated(out User user)
        {
            user = User;
            return user == null;
        }

        // ReSharper disable once MemberCanBePrivate.Global
        protected bool IsAdmin(out User user)
        {
            return IsAuthenticated(out user) && user.IsAdmin();
        }

        // ReSharper disable once UnusedMember.Global
        protected bool IsAdmin()
        {
            // ReSharper disable once UnusedVariable
            return IsAdmin(out var user);
        }

        private void Authenticate(string sessionToken)
        {
            if (sessionToken == null) return;
            
            Session = _usersManager.FindSession(sessionToken) ?? throw new Exception("Authorization failed");
        }
    }
}