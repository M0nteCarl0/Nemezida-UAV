namespace Nemezida.Web.Controllers
{
    using Microsoft.AspNetCore.Hosting;
    using Microsoft.AspNetCore.Http;
    using Microsoft.AspNetCore.Mvc;
    using Microsoft.Extensions.Configuration;

    using Nemezida.Web.Impl;

    using System.IO;

    [Route("api/uav")]
    [ApiController]
    public class UAVController : ControllerBase
    {
        private readonly IWebHostEnvironment _appEnvironment;

        private readonly IConfiguration _configuration;

        public UAVController(IWebHostEnvironment appEnvironment, IConfiguration configuration)
        {
            this._appEnvironment = appEnvironment;
            this._configuration = configuration;
        }

        [HttpPost]
        [Route("MARK_IMG")]
        public async System.Threading.Tasks.Task<IActionResult> ProcessImgAsync(IFormFile image)
        {
            if (image == null || image.Length == 0)
            {
                return this.BadRequest(new { Status = 400, Description = "Empty file" });
            }

            if (!image.ContentType.Contains("image"))
            {
                return this.BadRequest(new { Status = 400, Description = "Invalid file type" });
            }

            var scriptName = this._configuration["MLScriptName"] + ".py";
            var rootDir = this._configuration["MLRoot"];

            if (!System.IO.File.Exists(Path.Combine(rootDir, scriptName)))
            {
                return this.BadRequest(new { Status = 500, Description = "На сервере указан некорректный путь к скрипту", ScriptName = scriptName, Root = rootDir });
            }

            try
            {
                //var options = new JsonSerializerOptions
                //{
                //    PropertyNameCaseInsensitive = true,
                //};

                //var markupItems = JsonSerializer.Deserialize<MarkupItem[]>(markup, options);
                //var imageStream = image.OpenReadStream();
                var root = Path.Combine(_appEnvironment.WebRootPath, "UploadedFIles");
                Directory.CreateDirectory(root);

                var filePath = Path.Combine(root, image.FileName);

                if (System.IO.File.Exists(filePath))
                {
                    System.IO.File.Delete(filePath);
                }

                var fileStream = new FileStream(filePath, FileMode.Create);
                await image.CopyToAsync(fileStream);
                fileStream.Flush();
                fileStream.Dispose();

                var resultCode = PythonScriptRunner.Run(
                    scriptName, 
                    out var result, 
                    filePath,
                    rootDir);

                return resultCode > 0
                    ? (IActionResult)this.BadRequest(new { Status = 500, Description = result })
                    : this.Ok(new { Status = 200, Result = result });
            }
            catch (System.Exception e)
            {
                return this.BadRequest(new { Status = 500, Description = e.GetType().Name + ": " + e.Message });
            }
        }



        [Route("call_script")]
        public async System.Threading.Tasks.Task<IActionResult> CallScript(string name, string args)
        {
            var scriptName = name + ".py";
            var rootDir = this._configuration["MLRoot"];

            if (!System.IO.File.Exists(Path.Combine(rootDir, scriptName)))
            {
                return this.BadRequest(new { Status = 500, Description = "Указано несущестующее имя скрипта", ScriptName = scriptName, Root = rootDir });
            }

            try
            {
                var resultCode = PythonScriptRunner.Run(
                    scriptName,
                    out var result,
                    args,
                    rootDir);

                return resultCode > 0
                    ? (IActionResult)this.BadRequest(new { Status = 500, Description = result })
                    : this.Ok(new { Status = 200, Result = result });
            }
            catch (System.Exception e)
            {
                return this.BadRequest(new { Status = 500, Description = e.GetType().Name + ": " + e.Message });
            }
        }
    }
}
