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
            var discipline = _disciplinesManager.GetDisciplinesAsync().Single(d => d.Name == "3x3");

            var maxim = _usersManager.GetUsers().Single(u => u.FirstName == "Максим" && u.LastName == "Жемеренко");

            // check for fail
            var result = new Result
            {
                Attempts = Attempts(1, 2, 3, 4, 5, 6),
                Discipline = discipline,
                User = maxim
            };

            Assert.Throws<Exception>(() => TestAddResult(result));
            
            // add valid
            result = new Result
            {
                Attempts = Attempts(1, 2, 3, 4, 15),
                Discipline = discipline,
                User = maxim
            };
            
            TestAddResult(result);
            
            Assert.Equal(result.Average, 3);
            
            // add valid with null attempt
            result = new Result
            {
                Attempts = Attempts(1, 2, null, 4, 15),
                Discipline = discipline,
                User = maxim
            };
            
            TestAddResult(result);

            Assert.Equal(Math.Round(result.Average.Value, 2), (decimal)2.33);
            
            // add valid with two null attempts
            result = new Result
            {
                Attempts = Attempts(1, 2, null, null, 15),
                Discipline = discipline,
                User = maxim
            };

            TestAddResult(result);

            Assert.Equal(result.Average, null);
            
            // add valid with null attempt (4 in general)
            result = new Result
            {
                Attempts = Attempts(1, 2, null, 15),
                Discipline = discipline,
                User = maxim
            };

            TestAddResult(result);

            Assert.Equal(result.Average, null);
            
            // add valid (4 in general)
            result = new Result
            {
                Attempts = Attempts(1, 2, 15, 16),
                Discipline = discipline,
                User = maxim
            };

            TestAddResult(result);

            Assert.Equal(result.Average, (decimal)8.5d);
            
            // add (3 in general)
            result = new Result
            {
                Attempts = Attempts(1, 2, 15),
                Discipline = discipline,
                User = maxim
            };

            TestAddResult(result);

            Assert.Equal(result.Average, null);
        }

        private void TestAddResult(Result result)
        {
            var meeting = new Meeting {Date = DateTimeOffset.Now};
            _meetingsManager.AddMeeting(meeting);
            Assert.NotNull(meeting);

            result.Meeting = meeting;
            
            _resultsManager.AddResult(result);
            
            Assert.True(result.Id > 0);
        }

        private static IEnumerable<decimal?> Attempts(params decimal?[] attempts) => attempts.ToArray();
    }
}