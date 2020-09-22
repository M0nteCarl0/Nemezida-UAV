namespace WebApplication1.Controllers
{
    using System.Collections.Generic;
    using System.Diagnostics;

    using Microsoft.AspNetCore.Mvc;
    using Microsoft.Extensions.Logging;

    using Nemezida.Web.Models;

    using WebApplication1.Models;

    public class HomeController : Controller
    {
        private readonly ILogger<HomeController> _logger;

        public HomeController(ILogger<HomeController> logger)
        {
            _logger = logger;
        }

        public IActionResult Index()
        {
            var data = new List<DisplayImage>
            {
                new DisplayImage
                {
                    Src = "/images/1.jpg",
                    Active = true
                },
                new DisplayImage
                {
                    Src = "/images/2.jpg",
                },
                new DisplayImage
                {
                    Src = "/images/3.jpg",
                },
            };

            ViewData["Title"] = "Общая статистика";

            return View("StandartView", data);
        }

        public IActionResult OilIcidents()
        {
            var data = new List<DisplayImage>
            {
                new DisplayImage
                {
                    Src = "/images/1.jpg",
                    Active = true
                },
                new DisplayImage
                {
                    Src = "/images/2.jpg",
                },
                new DisplayImage
                {
                    Src = "/images/3.jpg",
                },
            };

            ViewData["Title"] = "Инциденты разлива нефти";

            return View("StandartView", data);
        }

        public IActionResult GroundOpeningIncidents()
        {
            var data = new List<DisplayImage>
            {
                new DisplayImage
                {
                    Src = "/images/1.jpg",
                    Active = true
                },
                new DisplayImage
                {
                    Src = "/images/2.jpg",
                },
                new DisplayImage
                {
                    Src = "/images/3.jpg",
                },
            };

            ViewData["Title"] = "Инциденты Вскрытия грунта";

            return View("StandartView", data);
        }

        public IActionResult MaterialWarehousingIncidents()
        {
            var data = new List<DisplayImage>
            {
                new DisplayImage
                {
                    Src = "/images/1.jpg",
                    Active = true
                },
                new DisplayImage
                {
                    Src = "/images/2.jpg",
                },
                new DisplayImage
                {
                    Src = "/images/3.jpg",
                },
            };

            ViewData["Title"] = "Инциденты Складирования материалов";

            return View("StandartView", data);
        }

        public IActionResult SpecialEquipmentWork()
        {
            var data = new List<DisplayImage>
            {
                new DisplayImage
                {
                    Src = "/images/1.jpg",
                    Active = true
                },
                new DisplayImage
                {
                    Src = "/images/2.jpg",
                },
                new DisplayImage
                {
                    Src = "/images/3.jpg",
                },
            };

            ViewData["Title"] = "Работа спец. техники";

            return View("StandartView", data);
        }

        public IActionResult RawFiles()
        {
            var data = new List<DisplayImage>
            {
                new DisplayImage
                {
                    Src = "/images/1.jpg",
                    Active = true
                },
                new DisplayImage
                {
                    Src = "/images/2.jpg",
                },
                new DisplayImage
                {
                    Src = "/images/3.jpg",
                },
            };

            ViewData["Title"] = "Необработанные файлы";

            return View("StandartView", data);
        }

        public IActionResult Test()
        {
            return View();
        }

        [ResponseCache(Duration = 0, Location = ResponseCacheLocation.None, NoStore = true)]
        public IActionResult Error()
        {
            return View(new ErrorViewModel { RequestId = Activity.Current?.Id ?? HttpContext.TraceIdentifier });
        }
    }
}
