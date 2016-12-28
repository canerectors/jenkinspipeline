package com.canerector

@NonCPS
@groovy.transform.InheritConstructors
class DockerBuildModule extends BuildModuleBase {
		
	@NonCPS
	def performBuildInternal(){
	
		node{
			sendSlackMessage('building...')
			checkout()
		}
		
		
	}
}