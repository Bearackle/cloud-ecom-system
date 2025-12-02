# Apply-WithParams.ps1
param(
    [Parameter(Mandatory=$true)]
    [string]$Version,

    [string]$TemplatesPath = "k8s-scripts",
    [string]$TempPath = "temp"
)

# Tạo thư mục temp
if (-not (Test-Path $TempPath)) {
    New-Item -ItemType Directory -Path $TempPath | Out-Null
}

Write-Host "`n🔄 Applying services with VERSION=$Version`n" -ForegroundColor Cyan

# Process all template files
Get-ChildItem -Path $TemplatesPath -Filter "*-template.yaml" | ForEach-Object {
    $templateFile = $_.FullName
    $outputFile = Join-Path $TempPath $_.Name.Replace("-template", "")

    Write-Host "Processing: $($_.Name)" -ForegroundColor Yellow

    # Read and replace
    $content = Get-Content $templateFile -Raw
    $content = $content -replace '\$\{VERSION\}', $Version

    # Write to temp file
    $content | Out-File -FilePath $outputFile -Encoding utf8

    # Apply
    kubectl apply -f $outputFile

    if ($LASTEXITCODE -eq 0) {
        Write-Host "  ✅ Applied successfully`n" -ForegroundColor Green
    } else {
        Write-Host "  ❌ Failed`n" -ForegroundColor Red
    }
}

# Cleanup
Remove-Item -Path $TempPath -Recurse -Force

Write-Host "✅ Done!`n" -ForegroundColor Green