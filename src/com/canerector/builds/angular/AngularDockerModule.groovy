package com.canerector.builds.angular

@groovy.transform.InheritConstructors
class AngularDockerModule extends com.canerector.builds.DockerBuildModuleBase {
	
	def performBuildInternal(){
	
		def wsName = 'c:\\' + projectName + '_' + branch
	
		pipeline.node{
			pipeline.ws(wsName){
				clean()
				checkout()
				
				version(false)

				build()
								
				sendSlackMessage('project: ' + slackFormattedGitHubUrl + " version: *" + version + "* built successfully.")
			}
		}		
	}
}