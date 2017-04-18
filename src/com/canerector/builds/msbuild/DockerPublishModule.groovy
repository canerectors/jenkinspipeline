package com.canerector.builds.msbuild

@groovy.transform.InheritConstructors
class DockerPublishModule extends MSBuildModuleBase {
		
	def performBuildInternal(){
	
		pipeline.node{
			pipeline.ws('c:\\' + projectName + '_' + branch){
				clean()
				checkout()
				nugetRestore()
				version()

				//build()
				//tests()
				
				sendSlackMessage('project: ' + slackFormattedGitHubUrl + " version: *" + version + "* built successfully.")
				
				publish()
			}
		}		
	}
	
	def publish(){
		pipeline.stage('Publish') {

			pipeline.bat 'cd ' + buildProject + ' && dotnet publish -o publish_output --configuration Release /p:GenerateAssemblyInfo=false && copy ..\\Dockerfile publish_output'
			
			def projectShortName = projectName.replace('CanErectors.', '').toLowerCase()
			
			def imageName = pipeline.docker1.getImageFullName(projectShortName, version)
			def imageLatestName = pipeline.docker1.getImageFullName(projectShortName, branch)
			
			def dockerContextPath = buildProject + '\\publish_output\\.'
			
			pipeline.docker1.build(imageName, dockerContextPath)
			
			pipeline.docker1.tag(imageName, imageLatestName)
			
			pipeline.docker1.publish(imageName)
			pipeline.docker1.publish(imageLatestName)
			
			pipeline.docker1.delete(imageName)
			pipeline.docker1.delete(imageLatestName)
			
			//buildAndPush(projectShortName, dockerContextPath)
			
			def slackFormattedRegistryUrl = pipeline.slack.getMessageStringForUrl(pipeline.docker1.getRegistryUrl(projectShortName), imageName)
				
			sendSlackMessage('Docker Image: ' + slackFormattedRegistryUrl + ' pushed to registry.')
		}
	}
	
	def buildAndPush(projectShortName, dockerFilePath){
		def dockerHost = pipeline.env.DOCKER_HOST
		
		def dockerCommand = 'docker -H ' + dockerHost

		def imageName = getImageName(projectShortName, version)
		def latestImageName = getImageName(projectShortName, branch)
		
		pipeline.bat dockerCommand + ' build -t ' + imageName + ' -t ' + latestImageName + ' ' + dockerFilePath
		pipeline.bat dockerCommand + ' push ' + imageName
		pipeline.bat dockerCommand + ' push ' + latestImageName
		
		pipeline.bat dockerCommand + ' rmi ' + imageName
		pipeline.bat dockerCommand + ' rmi ' + latestImageName
	}
}