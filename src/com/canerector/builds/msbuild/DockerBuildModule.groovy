package com.canerector.builds.msbuild

def performBuild(buildContext){

	echo 'Inside Module Project Name: ' + buildContext.projectName

	def stage = new com.canerector.builds.msbuild.MsbuildStages(buildContext)
echo 'Inside Module Project Name1: ' + buildContext.projectName	
	def dockerStage = new com.canerector.builds.msbuild.DockerStages(buildContext)	
	echo 'Inside Module Project Name2: ' + buildContext.projectName

	def cleanJobName = "${JOB_NAME}".replace('canerectors/', '')
	
	echo 'Inside Module Project Name1: ' + buildContext.projectName
	
	node{
		ws ('c:\\' + cleanJobName){
		
			stage.clean()
		
			stage.checkout()
			stage.nugetRestore()
			stage.version()
			
			buildContext.version = versioning.getVersionFromBuildOutput()
			
			stage.build()
			stage.tests()
			
			dockerStage.publish()
		}
	}
}
