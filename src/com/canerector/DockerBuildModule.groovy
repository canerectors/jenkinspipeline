class DockerBuildModule {
	DockerBuildModule(script){ this.script = script }
	
	def performBuild(){
		echo 'building...'
	}
}