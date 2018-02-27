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
            return _resultsRepository.GetMeetingResults(meetingId, readDiscipline: true, readUser: true)?
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
            _databaseContext.UseTransaction(transaction =>
            {
                var discipline = _disciplinesRepository.GetDiscipline(result.Discipline.Id, transaction, true);
                if (discipline == null)
                    throw new ArgumentException("Bad discipline id is provided");

                var countingFormula = CountingFormula.Get(discipline.Counting);

                result.Average = countingFormula.ComputeAverage(result.Attempts);
                result.AttemptCount = countingFormula.AttemptCount;

                _resultsRepository.AddResult(result, transaction);
            });
        }
    }
}
