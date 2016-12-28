@groovy.transform.InheritConstructors
class DockerBuildModule extends BuildModuleBase {
	//DockerBuildModule(script){ this.script = script }
	
	def performBuild(){
		echo 'building...'
	}
}