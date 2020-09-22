namespace Nemezida.Web.Impl
{
    using System.Diagnostics;

    public static class PythonScriptRunner
    {
        public static int Run(string cmd, out string result, string args = null, string workingDir = null)
        {
            var pythonArgs = string.IsNullOrEmpty(args)
                ? cmd
                : cmd + " " + args;

            var pi = new ProcessStartInfo("python", pythonArgs)
            {
                UseShellExecute = false,
                RedirectStandardOutput = true,
                RedirectStandardError = true
            };

            if (!string.IsNullOrEmpty(workingDir))
            {
                pi.WorkingDirectory = workingDir;
            }

            using var process = Process.Start(pi);
            using var outputReader = process.StandardOutput;
            using var errorReader = process.StandardError;

            
            var errors = errorReader.ReadToEnd();
            result = !string.IsNullOrEmpty(errors) 
                ? errors 
                : outputReader.ReadToEnd();

            return process.ExitCode;
        }
    }
}
