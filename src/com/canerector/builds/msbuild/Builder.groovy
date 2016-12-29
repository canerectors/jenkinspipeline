package com.canerector.builds.msbuild

projectName = ''
projectBranchName = ''
gitHubUrl = ''
slackFormattedGitHubUrl = ''
slackFormattedBuildUrl = ''

def buildWithModule(moduleName){

	projectName = env.JOB_NAME.replace('canerectors/', '').replace('/' + env.BRANCH_NAME, '')
	projectBranchName = projectName + ':' + env.BRANCH_NAME
	gitHubUrl = github.getProjectUrl(projectName, env.BRANCH_NAME)
	slackFormattedGitHubUrl = slack.getMessageStringForUrl(gitHubUrl, projectBranchName)
	slackFormattedBuildUrl = slack.getMessageStringForUrl(env.BUILD_URL, 'Build #' + env.BUILD_NUMBER)
		
	timestamps{
		try{
			sendSlackMessage('started for project: ' + slackFormattedGitHubUrl)
			
			Object buildModule

			switch (moduleName){
				case 'dockerpublish' :
					buildModule = dockerModule
			}

			buildModule.performBuild()
		}
		catch(err){
	
			print err
	
			consoleUrl = slack.getMessageStringForUrl(env.BUILD_URL + 'console', 'Build Log.')		
	
			sendSlackMessage('for project: ' + slackFormattedGitHubUrl + ' failed. See ' + consoleUrl, 'danger')		
	
			currentBuild.result = 'FAILURE'
		}
	}
}