using System.Collections.Generic;
using Backend.Data.Database;
using Backend.Data.Entities;
using Backend.Data.Repositories;

namespace Backend.Domain
{
    // ReSharper disable once ClassNeverInstantiated.Global
    public class MeetingsManager
    {
        private readonly DatabaseContext _databaseContext;
        private readonly MeetingsRepository _meetingsRepository;

        public MeetingsManager(DatabaseContext databaseContext, MeetingsRepository meetingsRepository)
        {
            _databaseContext = databaseContext;
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

        public Meeting GetLastMeeting()
        {
            return _meetingsRepository.GetLastMeeting();
        }

        public void AddMeeting(Meeting meeting)
        {
            _databaseContext.UseTransaction(transaction =>
                _meetingsRepository.AddMeeting(meeting, transaction)
            );
        }
    }
}