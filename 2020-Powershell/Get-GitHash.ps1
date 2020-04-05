function Get-GitHash { 
    <#
        .SYNOPSIS
            Calculates the Git hash on a given file.

        .DESCRIPTION
            Calculates the Git hash on a given file.

        .PARAMETER Path
            File or files that will be scanned for hashes.

        .OUTPUTS
            System.IO.FileInfo.Hash

        .EXAMPLE
          Get-FileHash -Path file1.txt
 
          Hash                                     Path                    Algorithm
          ----                                     ----                    ---------
          6a3acd0495448649d5b382150fef780a900562d7 C:\file1.txt            GitSHA1      

        .EXAMPLE
          Get-ChildItem -Filter '*.txt' | Get-GitHash

          Hash                                     Path                    Algorithm
          ----                                     ----                    ---------
          6a3acd0495448649d5b382150fef780a900562d7 C:\file1.txt            GitSHA1
          ebf4a6068f1c4176bf8db06771445b4d994f2199 C:\file2.txt            GitSHA1
    #>
    [CmdletBinding()]
    Param(
        [Parameter(Position = 0, Mandatory, ValueFromPipelineByPropertyName, ValueFromPipeline)]
        [Alias("PSPath", "FullName")]
        [ValidateScript( {
                foreach ($item in $_) {
                    if (-not (Test-Path -Path $item -PathType Leaf )) { 
                        throw "Path $item is not a file." 
                    }
                    $true 
                }
            }   
        )]
        [string[]] $Path
    )
    Begin {
        $chunkSizeInKBytes = 64
        $type = 'SHA1'
    }
    Process {  
        foreach ($item in $Path) { 
            try {

                # init
                $hasher = [Security.Cryptography.HashAlgorithm]::Create($type)
                $buffer = [System.Byte[]]::new($chunkSizeInKBytes * 1024)

                $file = Get-Item -Path $item -ErrorAction Stop

                # construct git header and calculate sha
                $header = [System.Text.Encoding]::UTF8.GetBytes("blob $($file.Length)`0")
                [void] $hasher.TransformBlock($header, 0, $header.Length, $header, 0)
                
                # read file and calculate sha
                $stream = ([IO.StreamReader]$file.FullName).BaseStream
                do {
                    $bytesRead = $stream.Read($buffer, 0, $buffer.Length)
                    [void] $hasher.TransformBlock($buffer, 0, $bytesRead, $buffer, 0)
                } while ($bytesRead -eq $buffer.Length)
                
                # empty write to mark the end
                $hasher.TransformFinalBlock($buffer, 0, 0);

                # return in format of Get-FileHash
                $result = [PSCustomObject]@{
                    Hash      = [string] ( -join ($hasher.Hash | ForEach-Object -Process { '{0:x2}' -f $_ }))
                    Path      = $file.FullName
                    Algorithm = "Git$type"
                }
                [void] $result.pstypenames.insert(0, 'System.IO.FileInfo.Hash')
                $result
            }
            catch {
                $PSCmdlet.WriteError($_)
            }
            finally {
                if ($stream) {
                    $stream.Close()
                }
            }
        }
    }
}