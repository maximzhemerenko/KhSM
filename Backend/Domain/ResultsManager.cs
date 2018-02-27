using System.Collections.Generic;
using System.Linq;
using Backend.Data.Database;
using Backend.Data.Entities;
using Backend.Data.Repositories;

namespace Backend.Domain
{
    // ReSharper disable once ClassNeverInstantiated.Global
    public class ResultsManager
    {
        private readonly DatabaseContext _databaseContext;
        private readonly ResultsRepository _resultsRepository;

        public ResultsManager(DatabaseContext databaseContext, ResultsRepository resultsRepository)
        {
            _databaseContext = databaseContext;
            _resultsRepository = resultsRepository;
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

        public Result AddResult(Result result)
        {
            return _databaseContext.UseTransaction(transaction =>
                _resultsRepository.AddResult(result, transaction)
            );
        }
    }
}