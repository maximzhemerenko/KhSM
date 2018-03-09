using Backend.Data.Database;
using MySql.Data.MySqlClient;

namespace Backend.Data.Repositories
{
    public abstract class BaseRepository
    {
        protected BaseRepository(DatabaseContext databaseContext)
        {
            DatabaseContext = databaseContext;
        }

        private DatabaseContext DatabaseContext { get; }
        public MySqlConnection Connection => DatabaseContext.Connection;
        
        public static class Db
        {
            public const string MeetingKey = "meeting";
            public static class Meeting
            {
                public const string MeetingIdKey = "meeting_id";                
            }
            
            public const string UserKey = "user";
            public static class User
            {
                public const string UserIdKey = "user_id";
                public const string FirstNameKey = "first_name";
                public const string LastNameKey = "last_name";
                public const string GenderKey = "gender";
                public const string EmailKey = "email";
                public const string PasswordHashKey = "password_hash";
                public const string CityKey = "city";
                public const string WcaIdKey = "wca_id";
                public const string PhoneNumberKey = "phone_number";
                public const string BirthDateKey = "birth_date";
            }
        }
    }
}