using Backend.Data.Database;
using Microsoft.AspNetCore.Mvc;

namespace Backend.Controllers
{
    public abstract class ApiController : Controller
    {
        protected DatabaseContext DatabaseContext { get; }

        protected ApiController()
        {
            DatabaseContext = new DatabaseContext();
        }
        
        protected override void Dispose(bool disposing)
        {
            if (disposing)
            {
                DatabaseContext.Dispose();
            }

            base.Dispose(disposing);
        }
    }
}