package com.canerector

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