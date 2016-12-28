package com.canerector

@groovy.transform.InheritConstructors
class DockerBuildModule extends BuildModuleBase {
		
	
	def performBuildInternal(){
	
		node{
			sendSlackMessage('building...')
			checkout()
		}
		
		
	}
}