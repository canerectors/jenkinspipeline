package com.canerector


@groovy.transform.InheritConstructors
class DockerBuildModule extends BuildModuleBase {
		
	def performBuildInternal(){
	
		pipeline.node{
			sendSlackMessage('building...')
			checkout()
		}	
		
	}
}