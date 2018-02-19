using System;
using System.Collections.Generic;
using System.Threading.Tasks;
using Backend.Data.Entities;

namespace Backend.Domain
{
    // ReSharper disable once ClassNeverInstantiated.Global
    public class MeetingsManager
    {
        public async Task<IEnumerable<Meeting>> GetMeetingsAsync()
        {
            // todo: use db
            return await Task.Factory.StartNew(() =>
                new List<Meeting>
                {
                    new Meeting {Id = 0, Number = 1, DateTimeOffset = DateTimeOffset.Now},
                    new Meeting {Id = 1, Number = 2, DateTimeOffset = DateTimeOffset.Now},
                    new Meeting {Id = 2, Number = 3, DateTimeOffset = DateTimeOffset.Now},
                    new Meeting {Id = 3, Number = 4, DateTimeOffset = DateTimeOffset.Now},
                    new Meeting {Id = 4, Number = 5, DateTimeOffset = DateTimeOffset.Now}
                });
        }
    }
}