package com.canerector.builds.msbuild

def performBuild(){

	def stage = new com.canerector.builds.msbuild.MsbuildStages()	
	def dockerStage = new com.canerector.builds.msbuild.DockerStages()	

	def cleanJobName = "${JOB_NAME}".replace('canerectors/', '')
	
	node{
		ws ('c:\\' + cleanJobName){
		
			stage.clean()
		
			stage.checkout()
			stage.nugetRestore()
			stage.version()
			
			version = versioning.getVersionFromBuildOutput()
			
			stage.build()
			stage.tests()
			
			dockerStage.publish()
		}
	}
}
