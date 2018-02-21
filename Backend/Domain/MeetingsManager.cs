using System.Collections.Generic;
using System.Linq;
using Backend.Data.Entities;
using Backend.Data.Repositories;

namespace Backend.Domain
{
    // ReSharper disable once ClassNeverInstantiated.Global
    public class MeetingsManager
    {
        private readonly MeetingsRepository _meetingsRepository;

        public MeetingsManager(MeetingsRepository meetingsRepository)
        {
            _meetingsRepository = meetingsRepository;
        }

        public IEnumerable<Meeting> GetMeetings()
        {
            return _meetingsRepository.GetMeetings();
        }

        public Meeting GetMeeting(int id)
        {
            return _meetingsRepository.GetMeeting(id);
        }

        public IEnumerable<DisciplineResults> GetMeetingResults(int meetingId)
        {
            return _meetingsRepository.GetMeetingResults(meetingId, readDiscipline: true)?
                .GroupBy(pair => pair.Discipline)
                .Select(pairs => new DisciplineResults
                {
                    Discipline = pairs.Key,
                    Results = pairs.Select(result =>
                    {
                        result.Discipline = null;
                        return result;
                    })
                });
        }
    }
}