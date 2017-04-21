//renamed docker1.groovy to avoid conflicting with built-in jenkins docker plugin

def build(imageName, dockerFilePath, dockerfile = null){
	dockerHost = "${env.DOCKER_HOST}"

	dockerCommand = 'docker -H ' + dockerHost + ' build '
	
	if(dockerfile){
		dockerCommand = dockerCommand + '-f ' + dockerfile + ' '
	}
	
	bat dockerCommand + '-t ' + imageName + ' ' + dockerFilePath
}

def publish(imageName){
	dockerHost = "${env.DOCKER_HOST}"

	login()

	dockerCommand = 'docker -H ' + dockerHost

	bat dockerCommand + ' push ' + imageName
}

def run(imageName, runArgs = "", command = ""){
	dockerHost = "${env.DOCKER_HOST}"

	dockerCommand = 'docker -H ' + dockerHost

	bat dockerCommand + ' run --rm ' + runArgs + ' ' + imageName + ' ' + command
}

def delete(imageName){
	dockerHost = "${env.DOCKER_HOST}"

	dockerCommand = 'docker -H ' + dockerHost

	bat dockerCommand + ' rmi ' + imageName
}

def tag(imageName, tagName){
	dockerHost = "${env.DOCKER_HOST}"

	dockerCommand = 'docker -H ' + dockerHost

	bat dockerCommand + ' tag ' + imageName + ' ' + tagName
}

def login(){
	withCredentials([usernamePassword(credentialsId: 'docker_hub', passwordVariable: 'PASSWORD', usernameVariable: 'USER_NAME')]) {
		bat 'docker login -u ' + "${USER_NAME}" + ' -p ' + "${PASSWORD}"
	}
		
	withCredentials([usernamePassword(credentialsId: 'docker_canerectors_registry', passwordVariable: 'PASSWORD', usernameVariable: 'USER_NAME')]) {
		bat 'docker login -u ' + "${USER_NAME}" + ' -p ' + "${PASSWORD} registry.recursive.co"
	}	
}

def getImageFullName(imageName, tag = 'latest'){
		return 'registry.recursive.co/canerectors/' + imageName + ':' + tag
	}

def getRegistryUrl(imageName){
	return 'https://portus.recursive.co'
}

return this;