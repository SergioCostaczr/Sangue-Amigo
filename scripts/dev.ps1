$ErrorActionPreference = "Stop"

$root = Split-Path -Parent $PSScriptRoot
$backendPath = Join-Path $root "backend"
$frontendPath = Join-Path $root "frontend"
$logPath = Join-Path $backendPath "target\dev-backend.log"

$processInfo = New-Object System.Diagnostics.ProcessStartInfo
$processInfo.FileName = "cmd.exe"
$processInfo.Arguments = "/c mvnw.cmd spring-boot:run > target\dev-backend.log 2>&1"
$processInfo.WorkingDirectory = $backendPath
$processInfo.UseShellExecute = $true
$processInfo.WindowStyle = [System.Diagnostics.ProcessWindowStyle]::Hidden

$backend = [System.Diagnostics.Process]::Start($processInfo)
Start-Sleep -Seconds 2

if ($backend.HasExited) {
    if (Test-Path $logPath) {
        Get-Content $logPath
    }
    throw "O backend nao iniciou. Consulte backend/target/dev-backend.log."
}

Write-Host "Backend iniciado. Log: backend/target/dev-backend.log"

try {
    npm --prefix $frontendPath run dev
}
finally {
    if (-not $backend.HasExited) {
        taskkill /PID $backend.Id /T /F | Out-Null
    }
}
