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
		pipeline.stage('Publish to Docker Registry') {

			pipeline.bat 'cd ' + projectName + ' && dotnet publish -o publish_output --configuration Release'
			
			def projectShortName = projectName.replace('CanErectors.', '').toLowerCase()
			
			def imageName = getImageName(projectShortName, version)
			
			buildAndPush(projectShortName, projectName + '\\publish_output\\.')

			def slackFormattedRegistryUrl = pipeline.slack.getMessageStringForUrl(getRegistryUrl(projectShortName), imageName)
				
			sendSlackMessage('Docker Image: ' + slackFormattedRegistryUrl + ' pushed to registry.')
		}
	}
	
	def buildAndPush(projectShortName, dockerFilePath){
		def dockerHost = pipeline.env.DOCKER_HOST
		
		pipeline.withCredentials([pipeline.usernamePassword(credentialsId: 'docker_hub', passwordVariable: 'PASSWORD', usernameVariable: 'USER_NAME')]) {
			pipeline.bat 'docker login -u ' + "${pipeline.USER_NAME}" + ' -p ' + "${pipeline.PASSWORD}"
		}	
		
		def dockerCommand = 'docker -H ' + dockerHost

		def imageName = getImageName(projectShortName, version)
		def latestImageName = getImageName(projectShortName, branch)
		
		pipeline.bat dockerCommand + ' build -t ' + imageName + ' -t ' + latestImageName + ' ' + dockerFilePath
		pipeline.bat dockerCommand + ' push ' + imageName
		pipeline.bat dockerCommand + ' push ' + latestImageName
	}
	
	def getImageName(imageName, tag = 'latest'){
		return 'registry.recursive.co/canerectors/' + imageName + ':' + tag
	}
	
	def getRegistryUrl(imageName){
		return 'https://registry.recursive.co:444/repository/canerectors/' + imageName
	}
}