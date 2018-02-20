using System.Collections.Generic;
using Backend.Data.Entities;
using Backend.Data.Repositories;

namespace Backend.Domain
{
    // ReSharper disable once ClassNeverInstantiated.Global
    public class MeetingsManager
    {
        private readonly MeetingsRepository _meetingsRepository;
        private readonly DisciplinesRepository _disciplinesRepository;

        public MeetingsManager(MeetingsRepository meetingsRepository, DisciplinesRepository disciplinesRepository)
        {
            _meetingsRepository = meetingsRepository;
            _disciplinesRepository = disciplinesRepository;
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
            var disciplines = _disciplinesRepository.GetDisciplinesByMeetingId(meetingId);
            if (disciplines == null)
                return null;

            var results = new List<DisciplineResults>();
            
            foreach (var discipline in disciplines)
            {
                results.Add(new DisciplineResults{Discipline = discipline});
            }

            return results;
        }
    }
}