using System.Collections.Generic;
using Backend.Data.Entities;
using Backend.Domain;
using Microsoft.AspNetCore.Mvc;

namespace Backend.Controllers
{
    public class DisciplinesController : ApiController
    {
        private readonly DisciplinesManager _disciplinesManager;

        public DisciplinesController(DisciplinesManager disciplinesManager)
        {
            _disciplinesManager = disciplinesManager;
        }

        [HttpGet]
        public IEnumerable<Discipline> Get()
        {
            return _disciplinesManager.GetDisciplinesAsync();
        }
    }
}