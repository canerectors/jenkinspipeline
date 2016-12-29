package com.canerector.builds.msbuild

@groovy.transform.InheritConstructors
class DockerBuildModule extends BuildModuleBase {
		
	def performBuildInternal(){
	
		pipeline.node{
			clean()
			checkout()
			nugetRestore()
			version()
			
			//version = versioning.getVersionFromBuildOutput()
			
			build()
			tests()
			
			publish()
		}	
		
	}
	
	def publish(){
	
	}
}