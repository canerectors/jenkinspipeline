package com.canerector.builds.msbuild


@groovy.transform.InheritConstructors
class DockerBuildModule extends BuildModuleBase {
		
	def performBuildInternal(){
	
		pipeline.node{
			//sendSlackMessage('building...')
			checkout()
		}	
		
	}
}