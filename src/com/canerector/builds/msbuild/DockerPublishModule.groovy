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

				build()
				tests()
				
				sendSlackMessage('project: ' + slackFormattedGitHubUrl + " version: *" + version + "* built successfully.")
				
				publish()
			}
		}		
	}
	
	def publish(){
		pipeline.stage('Publish') {

			pipeline.bat 'cd ' + buildProject + ' && dotnet publish -o publish_output --configuration Release /p:GenerateAssemblyInfo=false && copy ..\\Dockerfile publish_output'
			
			def projectShortName = projectName.replace('CanErectors.', '').toLowerCase()
			
			def imageName = pipeline.docker.getImageFullName(projectShortName, version)
			def imageLatestName = pipeline.docker.getImageFullName(projectShortName, branch)
			
			def dockerContextPath = buildProject + '\\publish_output\\.'
			
			pipeline.docker.build(imageName, dockerContextPath)
			
			pipeline.docker.tag(imageName, imageLatestName)
			
			pipeline.docker.publish(imageName)
			pipeline.docker.publish(imageLatestName)
			
			pipeline.docker.delete(imageName)
			pipeline.docker.delete(imageLatestName)
			
			//buildAndPush(projectShortName, dockerContextPath)
			
			def slackFormattedRegistryUrl = pipeline.slack.getMessageStringForUrl(pipeline.docker.getRegistryUrl(projectShortName), imageName)
				
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