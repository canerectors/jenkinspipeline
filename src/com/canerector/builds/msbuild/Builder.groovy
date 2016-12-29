package com.canerector.builds.msbuild

projectName = ''
projectBranchName = ''
gitHubUrl = ''
slackFormattedGitHubUrl = ''
slackFormattedBuildUrl = ''

def buildWithModule(moduleName){

	def tokens = "${env.JOB_NAME}".tokenize('/')
	
	org = tokens[tokens.size()-3]
	projectName = tokens[tokens.size()-2]
	branch = tokens[tokens.size()-1]
	
	projectBranchName = projectName + ':' + branch
	gitHubUrl = github.getProjectUrl(projectName, branch)
	slackFormattedGitHubUrl = slack.getMessageStringForUrl(gitHubUrl, projectBranchName)
	slackFormattedBuildUrl = slack.getMessageStringForUrl(env.BUILD_URL, 'Build #' + env.BUILD_NUMBER)
		
	timestamps{
		try{
			sendSlackMessage('started for project: ' + slackFormattedGitHubUrl)
			
			Object buildModule

			switch (moduleName){
				case 'dockerpublish' :
					buildModule = new com.canerector.builds.msbuild.DockerBuildModule()
			}
			
			def buildContext = [projectName:projectName, gitHubUrl:gitHubUrl]

			buildModule.performBuild(buildContext)
		}
		catch(err){
	
			print err
	
			consoleUrl = slack.getMessageStringForUrl(env.BUILD_URL + 'console', 'Build Log.')		
	
			sendSlackMessage('for project: ' + slackFormattedGitHubUrl + ' failed. See ' + consoleUrl, 'danger')		
	
			currentBuild.result = 'FAILURE'
		}
	}
}

def sendSlackMessage(message, color = 'good', channel = '#builds'){
	echo slackFormattedBuildUrl + ' ' + message
	
	//pipeline.slack.sendMessage(message, color, channel)
	
	//pipeline.echo "HELLO"
}