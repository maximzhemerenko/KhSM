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
        
        public IEnumerable<Meeting> GetMeetingsAsync()
        {
            return _meetingsRepository.GetMeetings();
        }
    }
}