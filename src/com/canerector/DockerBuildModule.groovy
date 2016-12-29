package com.canerector

class DockerBuildModule {
		
	def pipeline
	
	DockerBuildModule(pipeline){ this.pipeline = pipeline }
	
	def performBuild(){
	
		def stages = new com.canerector.MsbuildStages()	

		node{
			stages.checkout()
		}
	}
}