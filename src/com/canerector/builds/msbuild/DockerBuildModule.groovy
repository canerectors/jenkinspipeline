package com.canerector.builds.msbuild

@groovy.transform.InheritConstructors
class DockerBuildModule extends BuildModuleBase {
		
	def performBuildInternal(){
	
		pipeline.node{
			pipeline.ws('c:\\' + projectName + '_' + branch){
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
		
	}
	
	def publish(){
	
	}
}