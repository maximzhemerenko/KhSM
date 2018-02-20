using System.Collections.Generic;
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
    }
}