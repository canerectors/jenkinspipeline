package com.canerector.builds.msbuild

def performBuild(buildContext){

	def stage = new com.canerector.builds.msbuild.MsbuildStages(buildContext)	
	def dockerStage = new com.canerector.builds.msbuild.DockerStages(buildContext)	

	def cleanJobName = "${JOB_NAME}".replace('canerectors/', '')
	
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
