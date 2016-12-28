package com.canerector

@groovy.transform.InheritConstructors
class DockerBuildModule extends BuildModuleBase {
	//DockerBuildModule(script){ this.script = script }
	
	def performBuild(){
		script.echo 'building...'
	}
}