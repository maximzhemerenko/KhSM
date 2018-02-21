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
        [Required]
        public string City { get; set; }
        [Required]
        // ReSharper disable once InconsistentNaming
        public string WCAID { get; set; }
        [Required]
        public string PhoneNumber { get; set; }
        [Required]
        public string Gender { get; set; }
        [Required]
        public DateTimeOffset? BirthDate { get; set; }
        [Required]
        public DateTimeOffset? Approved { get; set; }
    }
}