def publish(imageName, dockerFilePath){
	dockerHost = "${env.DOCKER_HOST}"
	
	withCredentials([usernamePassword(credentialsId: 'docker_hub', passwordVariable: 'PASSWORD', usernameVariable: 'USER_NAME')]) {
		bat 'docker login -u ' + "${USER_NAME}" + ' -p ' + "${PASSWORD}"
	}	
	
	dockerCommand = 'docker -H ' + dockerHost

	bat dockerCommand + ' build -t ' + imageName + ' ' + dockerFilePath
	bat dockerCommand + ' push ' + imageName
}

def getImageName(projectName, tag = 'latest'){
	return 'registry.recursive.co/canerectors/' + projectName + ':' + tag
}

def getRegistryUrl(imageName){
	return 'https://registry.recursive.co:444/repository/canerectors/' + imageName
}

return this;