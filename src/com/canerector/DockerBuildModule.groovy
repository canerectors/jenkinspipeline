package com.canerector

class DockerBuildModule {
		
	def pipeline
	
	DockerBuildModule(pipeline){ this.pipeline = pipeline }
	
	def performBuild(){
	
		stages = new MsbuildStages()	

		stages.checkout()
	}
}