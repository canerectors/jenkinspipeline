package com.canerector.builds.msbuild

def buildContext 

DockerStages(Map context) {this.buildContext = context}

def publish(){
	
	stage('Publish to Docker Registry') {

		bat 'cd ' + buildContext.projectName + ' && dotnet publish -o publish_output --configuration Release'
		
		def projectShortName = buildContext.projectName.replace('CanErectors.', '').toLowerCase()
		
		def imageName = docker.getImageName(projectShortName, version)
		
		docker.publish(imageName, buildContext.projectName + '\\publish_output\\.')

		slackFormattedRegistryUrl = slack.getMessageStringForUrl(docker.getRegistryUrl(projectShortName), imageName)
			
		sendMessage('Docker Image: ' + slackFormattedRegistryUrl + ' pushed to registry.')
	}	
}