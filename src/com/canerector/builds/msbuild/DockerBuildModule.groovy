package com.canerector.builds.msbuild

@groovy.transform.InheritConstructors
class DockerBuildModule extends BuildModuleBase {
		
	def performBuildInternal(){
	
		pipeline.node{
			pipeline.ws('c:\\' + projectName + '_' + branch){
				clean()
				checkout()
				nugetRestore()
				version()

				build()
				tests()
				
				sendSlackMessage('project: ' + slackFormattedGitHubUrl + " version: *" + version + "* built successfully.")
				
				publish()
			}
		}
		
	}
	
	def publish(){
		stage('Publish to Docker Registry') {

			pipeline.bat 'cd ' + projectName + ' && dotnet publish -o publish_output --configuration Release'
			
			def projectShortName = projectName.replace('CanErectors.', '').toLowerCase()
			
			imageName = pipeline.docker.getImageName(projectShortName, version)
			
			pipeline.docker.publish(imageName, projectName + '\\publish_output\\.')

			slackFormattedRegistryUrl = pipeline.slack.getMessageStringForUrl(pipeline.docker.getRegistryUrl(projectShortName), imageName)
				
			sendSlackMessage('Docker Image: ' + slackFormattedRegistryUrl + ' pushed to registry.')
		}							
	}
}