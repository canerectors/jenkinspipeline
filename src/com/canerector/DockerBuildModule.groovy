package com.canerector

class DockerBuildModule {
		
	def pipeline
	
	def performBuild(){
	
		stages = new MsbuildStages()	

		stages.checkout()
	}
}