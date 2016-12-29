package com.canerector.builds.msbuild

def context 

DockerStages(buildContext) {this.context = buildContext}

def publish(){
	
	stage('Publish to Docker Registry') {

		bat 'cd ' + projectName + ' && dotnet publish -o publish_output --configuration Release'
		
		def projectShortName = projectName.replace('CanErectors.', '').toLowerCase()
		
		def imageName = docker.getImageName(projectShortName, version)
		
		docker.publish(imageName, projectName + '\\publish_output\\.')

		slackFormattedRegistryUrl = slack.getMessageStringForUrl(docker.getRegistryUrl(projectShortName), imageName)
			
		sendMessage('Docker Image: ' + slackFormattedRegistryUrl + ' pushed to registry.')
	}	
}