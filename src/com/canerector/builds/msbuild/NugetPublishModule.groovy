package com.canerector.builds.msbuild

@groovy.transform.InheritConstructors
class NugetPublishModule extends BuildModuleBase {
		
	def performBuildInternal(){
	
		def wsName = 'c:\\' + projectName + '_' + branch
	
		pipeline.node{
			pipeline.ws(wsName){
				clean()
				checkout()
				nugetRestore()
				version()

				build()
				tests()
				
				sendSlackMessage('project: ' + slackFormattedGitHubUrl + " version: *" + version + "* built successfully.")
			}
		}
		
		def userInput = true
		
		if (branch != 'master') {
			try{
				pipeline.timeout(time: 1, unit: 'MINUTES') { 
					userInput = pipeline.input(message: 'Deploy Prerelease version to NuGet?')

					userInput = true
				}    	
			}
			catch(err){ // timeout or user input aborted			
				userInput = false
			}
		}
		
		if (branch == 'master' || userInput) {
			pipeline.node{
				pipeline.ws(wsName){
					publish()
					
					def feedUrl = pipeline.nuget.getFeedUrl(projectName)
					def formattedText = pipeline.slack.getMessageStringForUrl(feedUrl ,'deployed to MyGet')
				
					sendSlackMessage(slackFormattedGitHubUrl + " version: *" + version + "* " + formattedText)
				}
			}
		}		
	}
	
	def publish(){
	
		pipeline.stage('Build Nuget Package') {
			//pipeline.bat 'del ' + projectName + '\\bin /s /q > NUL && del ' + projectName + '\\obj /s /q > NUL'
			
			pipeline.nuget.pack(projectName)
		}
		
		//pipeline.stage('Publish Symbols') {
		//	pipeline.nuget.publishSymbols()
		//}
		
		pipeline.stage('Nuget Publish') {
			pipeline.nuget.publishPackage()				
		}
	}
}