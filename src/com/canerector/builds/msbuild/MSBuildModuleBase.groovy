package com.canerector.builds.msbuild

@groovy.transform.InheritConstructors
abstract class MSBuildModuleBase extends com.canerector.builds.BuildModuleBase {
	
		
	def nugetRestore(){
		pipeline.stage('Nuget Restore') {
			pipeline.nuget.restore()
		}
	}
	
	def build(){
		pipeline.stage('Build') {
		//	pipeline.bat 'cd ' + buildProject + ' && dotnet build --no-incremental -c Release /p:GenerateAssemblyInfo=false /p:DebugType=pdbonly /p:Optimize=true'
		
			pipeline.bat 'cd ' + buildProject + ' && dotnet publish -o publish_output --configuration Release /p:GenerateAssemblyInfo=false /p:DebugType=pdbonly'
					
		}
	}
	
	def tests(){
		def testFolder = projectName + '.Tests'
		
		def hasTests = pipeline.fileExists(testFolder)	
				
		if(hasTests){
			pipeline.stage('Tests'){
					
				def success = pipeline.testing.runTests(testFolder)
				 
				if (!success){
					sendSlackMessage('Testing failed for: ' + slackFormattedGitHubUrl + ' version: ' + version, 'danger')
					pipeline.bat 'exit 1'
				}			
			}
		}
		else
			pipeline.echo 'No tests found.'
	}
	
	def symbols(){
		pipeline.stage('Symbols') {
			pipeline.bat 'cd ' + buildProject + '\\publish_output && powershell -noprofile -command Get-Symbols.ps1 c:/symbols' + ' ' + pipeline.nuget.getSymbolFeedUrl()
		}
	}
}