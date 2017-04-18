package com.canerector.builds

@groovy.transform.InheritConstructors
abstract class DockerBuildModuleBase extends BuildModuleBase {

	
	def build(){		
	
		pipeline.stage('Build') {
		
			def imageName = projectShortName + '_build:' + version
		
			pipeline.docker1.build(imageName, '.', 'Dockerfile.build')
			
			pipeline.docker1.delete(imageName)
		}
	}
}