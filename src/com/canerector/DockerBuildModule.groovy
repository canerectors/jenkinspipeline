package com.canerector

@groovy.transform.InheritConstructors
class DockerBuildModule extends BuildModuleBase {
		
	@NonCPS
	def performBuild(){
		sendSlackMessage('building...')
	}
}