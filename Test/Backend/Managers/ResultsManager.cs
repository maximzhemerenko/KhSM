using System.Collections.Generic;
using System.Linq;
using Backend;
using Backend.Data.Entities;
using Backend.Domain;
using Microsoft.Extensions.DependencyInjection;
using Xunit;

namespace TestProject.Managers
{
    public class ResultsManager
    {
        private readonly DisciplinesManager _disciplinesManager;
        private readonly MeetingsManager _meetingsManager;
        private readonly Backend.Domain.ResultsManager _resultsManager;
        private readonly UsersManager _usersManager;
        
        public ResultsManager()
        {
            var services = new ServiceCollection();
            Startup.ConfigDataServices(services);

            var serviceProvider = services.BuildServiceProvider();

            _disciplinesManager = serviceProvider.GetService<DisciplinesManager>();
            _meetingsManager = serviceProvider.GetService<MeetingsManager>();
            _resultsManager = serviceProvider.GetService<Backend.Domain.ResultsManager>();
            _usersManager = serviceProvider.GetService<UsersManager>();
        }
        
        [Fact]
        public void AddResult()
        {
            var meeting1 = _meetingsManager.GetMeeting(1);
            Assert.NotNull(meeting1);

            var discipline = _disciplinesManager.GetDisciplinesAsync().Single(d => d.Name == "3x3");

            var maxim = _usersManager.GetUsers().Single(u => u.FirstName == "Максим" && u.LastName == "Жемеренко");

            var result = new Result
            {
                Attempts = Attempts(5, 7, 10, 11, 13),
                Discipline = discipline,
                Meeting = meeting1,
                User = maxim
            };

            _resultsManager.AddResult(result);
        }

        private IEnumerable<decimal?> Attempts(params decimal?[] attempts)
        {
            return attempts.ToArray();
        }
    }
}