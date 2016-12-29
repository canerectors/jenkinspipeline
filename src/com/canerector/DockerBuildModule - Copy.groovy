package com.canerector


@groovy.transform.InheritConstructors
class DockerBuildModule1 extends BuildModuleBase {
		
	def performBuildInternal(){
	
		pipeline.node{
			sendSlackMessage('building...')
			checkout()
		}	
		
	}
}