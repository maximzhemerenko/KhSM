using System.Collections.Generic;
using System.Linq;
using Backend.Data.Entities;
using Backend.Data.Repositories;

namespace Backend.Domain
{
    public class ResultsManager
    {
        private readonly ResultsRepository _resultsRepository;

        public ResultsManager(ResultsRepository resultsRepository)
        {
            _resultsRepository = resultsRepository;
        }
        
        public IEnumerable<DisciplineResults> GetMeetingResults(int meetingId)
        {
            return _resultsRepository.GetMeetingResults(meetingId, readDiscipline: true)?
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
    }
}