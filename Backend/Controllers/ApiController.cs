using Microsoft.AspNetCore.Mvc;

namespace Backend.Controllers
{
    [Route("api/[controller]")]
    [Produces("application/json")]
    public abstract class ApiController : Controller
    {
    }
}