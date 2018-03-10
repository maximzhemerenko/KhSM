using System;
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
        public void AddMeeting()
        {
            var meeting = new Meeting
            {
                Date = DateTimeOffset.Now
            };
            
            _meetingsManager.AddMeeting(meeting);
            
            Assert.True(meeting.Id > 0);
        }

        [Fact]
        public void AddResult()
        {
            Random random = new Random();
            
            var meeting = new Meeting {Date = DateTimeOffset.Now};
            _meetingsManager.AddMeeting(meeting);
            Assert.NotNull(meeting);

            var discipline = _disciplinesManager.GetDisciplinesAsync().Single(d => d.Name == "3x3");

            var maxim = _usersManager.GetUsers(false).Single(u => u.FirstName == "Maxim" && u.LastName == "Zhemerenko");

            Result result;
            
            // add valid
            result = new Result
            {
                Attempts = Attempts(random.Next(1, 100), random.Next(1, 100), random.Next(1, 100), random.Next(1, 100), random.Next(1, 100)),
                Discipline = discipline,
                User = maxim
            };
            
            TestAddResult(meeting, result);
            
            // 2x2
            discipline = _disciplinesManager.GetDisciplinesAsync().Single(d => d.Name == "2x2");
            
            // add valid
            result = new Result
            {
                Attempts = Attempts(random.Next(1, 100), random.Next(1, 100), random.Next(1, 100)),
                Discipline = discipline,
                User = maxim
            };
            
            TestAddResult(meeting, result);
        }

        private void TestAddResult(Meeting meeting, Result result)
        {
            result.Meeting = meeting;
            
            _resultsManager.AddResult(result);
            
            Assert.True(result.Id > 0);
        }

        private static IEnumerable<decimal?> Attempts(params decimal?[] attempts) => attempts.ToArray();
    }
}