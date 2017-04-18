package com.canerector.builds

abstract class DockerBuildModuleBase extends BuildModuleBase {

	
	def build(){		
	
		pipeline.stage('Build') {
		
			def imageName = projectShortName + '_build:' + version
		
			pipeline.docker1.build(imageName, '.', 'Dockerfile.build')
		}
	}
}