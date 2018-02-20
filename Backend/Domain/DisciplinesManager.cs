using System.Collections.Generic;
using Backend.Data.Entities;
using Backend.Data.Repositories;

namespace Backend.Domain
{
    // ReSharper disable once ClassNeverInstantiated.Global
    public class DisciplinesManager
    {
        private readonly DisciplinesRepository _disciplinesRepository;

        public DisciplinesManager(DisciplinesRepository disciplinesRepository)
        {
            _disciplinesRepository = disciplinesRepository;
        }
        
        public IEnumerable<Discipline> GetDisciplinesAsync()
        {
            return _disciplinesRepository.GetDisciplines();
        }
    }
}