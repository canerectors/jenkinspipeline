package com.canerector.builds.msbuild

def performBuild(){

	def stages = new com.canerector.builds.msbuild.MsbuildStages()	

	def cleanJobName = "${JOB_NAME}".replace('canerectors/', '')
	
	node{
		ws ('c:\\' + cleanJobName){
			stages.checkout()
			stages.nugetRestore()
			stages.version()
		}
	}
}
