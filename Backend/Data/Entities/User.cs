using System;
using System.ComponentModel.DataAnnotations;

namespace Backend.Data.Entities
{
    public class User
    {
        [Required]
        public int Id { get; set; }
        [Required]
        public string FirstName { get; set; }
        [Required]
        public string LastName { get; set; }
        public string City { get; set; }
        // ReSharper disable once InconsistentNaming
        public string WCAID { get; set; }
        public string PhoneNumber { get; set; }
        [Required]
        public Gender Gender { get; set; }
        public DateTimeOffset? BirthDate { get; set; }
        public DateTimeOffset? Approved { get; set; }
    }
}