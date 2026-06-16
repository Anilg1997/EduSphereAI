$mongodPath = "C:\Program Files\MongoDB\Server\8.3\bin\mongod.exe"
$dataPath = "C:\Users\Lenovo\mongodb-data"
$logPath = "$dataPath\mongod.log"

Write-Output "Starting MongoDB from: $mongodPath"
Write-Output "Data directory: $dataPath"

$process = Start-Process -FilePath $mongodPath -ArgumentList "--dbpath $dataPath --logpath $logPath --port 27017" -WindowStyle Hidden -PassThru

Start-Sleep -Seconds 3

$running = (-not $process.HasExited)
Write-Output "MongoDB running: $running"

if ($process.HasExited) {
    Write-Output "Exit code: $($process.ExitCode)"
} else {
    Write-Output "PID: $($process.Id)"
}

# Verify port
$connection = Test-NetConnection -ComputerName localhost -Port 27017 -WarningAction SilentlyContinue
Write-Output "Port 27017 open: $($connection.TcpTestSucceeded)"
