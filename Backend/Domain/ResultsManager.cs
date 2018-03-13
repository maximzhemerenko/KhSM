using System;
using System.Collections.Generic;
using System.Linq;
using Backend.Data.Database;
using Backend.Data.Entities;
using Backend.Data.Repositories;
using Backend.Domain.Formula;

namespace Backend.Domain
{
    // ReSharper disable once ClassNeverInstantiated.Global
    public class ResultsManager
    {
        private readonly DatabaseContext _databaseContext;
        private readonly DisciplinesRepository _disciplinesRepository;
        private readonly ResultsRepository _resultsRepository;

        public ResultsManager(DatabaseContext databaseContext, ResultsRepository resultsRepository, DisciplinesRepository disciplinesRepository)
        {
            _databaseContext = databaseContext;
            _resultsRepository = resultsRepository;
            _disciplinesRepository = disciplinesRepository;
        }
        
        public IEnumerable<DisciplineResults> GetMeetingResults(int meetingId)
        {
            var results = _resultsRepository.GetResults(filter: (meetingId, null), readDiscipline: true, readUser: true);
            return GrpupResultsByDiscipline(results);
        }

        public IEnumerable<DisciplineResults> GetUserResults(int userId)
        {
            var results = _resultsRepository.GetResults(filter: (null, userId), readDiscipline: true, readMeeting: true);
            return GrpupResultsByDiscipline(results);
        }

        public IEnumerable<DisciplineRecord> GetUserRecords(int userId)
        {
            var averageResultComparer = new Result.Comparer(Result.Comparer.Mode.Average);
            var singleResultComparer = new Result.Comparer(Result.Comparer.Mode.Single);

            var results = _resultsRepository.GetResults(filter: (null, userId), readDiscipline: true, readMeeting: true);
            return GrpupResultsByDiscipline(results)
                .Select(dr => new DisciplineRecord
                {
                    Discipline = dr.Discipline,
                    BestSingleResult = dr.Results.OrderBy(r => r, singleResultComparer).FirstOrDefault(),
                    BestAverageResult = dr.Results.OrderBy(r => r, averageResultComparer).FirstOrDefault()
                });
        }
        
        public IEnumerable<DisciplineResults> GetRankings()
        {
            var results = _resultsRepository.GetResults(filter: (null, null), readDiscipline: true, readMeeting: true, readUser: true);
            return GrpupResultsByDiscipline(results)
                .Select(disciplineResults =>
                {
                    return new DisciplineResults
                    {
                        Discipline = disciplineResults.Discipline,
                        Results = disciplineResults.Results
                            .GroupBy(result => result.User)
                            .Select(group => group.First())
                    };
                });
        }
        
        private static IEnumerable<DisciplineResults> GrpupResultsByDiscipline(IEnumerable<Result> results)
        {
            return results?
                .GroupBy(pair => pair.Discipline)
                .Select(pairs =>
                {
                    var discipline = pairs.Key;

                    discipline.Description = null;
                    
                    return new DisciplineResults
                    {
                        Discipline = discipline,
                        Results = pairs.Select(result =>
                        {
                            result.Discipline = null;
                            return result;
                        })
                    };
                });
        }

        public void AddResult(Result result)
        {
            if (result == null) throw new ArgumentNullException(nameof(result));

            _databaseContext.UseTransaction(transaction =>
            {
                var discipline = _disciplinesRepository.GetDiscipline(result.Discipline.Id, transaction, true);
                if (discipline == null)
                    throw new ArgumentException("Bad discipline id is provided");
                
                var countingFormula = CountingFormula.Get(discipline.Counting);
                
                var attempts = result.Attempts;
                var attemptCount = countingFormula.AttemptCount;
                
                result.Average = countingFormula.ComputeAverage(attempts);
                result.AttemptCount = attemptCount;

                _resultsRepository.AddResult(result, transaction);
            });
        }
    }
}
