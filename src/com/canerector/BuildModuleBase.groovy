package com.canerector

abstract class BuildModuleBase{

	def pipeline
	
	BuildModuleBase(pipeline){ this.pipeline = pipeline }
	
	def projectName
	def projectBranchName
	def gitHubUrl
	def slackFormattedGitHubUrl
	def slackFormattedBuildUrl

	def performBuild(){
	
		projectName = "${JOB_NAME}".replace('canerectors/', '').replace("/${BRANCH_NAME}", '')
		projectBranchName = projectName + ':' + "${BRANCH_NAME}";
		gitHubUrl = pipeline.github.getProjectUrl(projectName, "${BRANCH_NAME}")
		slackFormattedGitHubUrl = pipeline.slack.getMessageStringForUrl(gitHubUrl, projectBranchName)
		slackFormattedBuildUrl = pipeline.slack.getMessageStringForUrl("${BUILD_URL}", 'Build #' + "${BUILD_NUMBER}")
		
		timestamps{
			try{
				sendSlackMessage('started for project: ' + slackFormattedGitHubUrl)		
	
				performBuildInternal()
			}
			catch(err){
		
				print err
		
				consoleUrl = pipeline.slack.getMessageStringForUrl("${BUILD_URL}console", 'Build Log.')		
		
				sendSlackMessage('for project: ' + slackFormattedGitHubUrl + ' failed. See ' + consoleUrl, 'danger')		
		
				currentBuild.result = 'FAILURE'
			}
		}
	}
	
	abstract def performBuildInternal()
	
	def checkout(){
		stage('Checkout') {
			pipeline.checkout scm
			
			pipeline.bat 'git submodule update --init --recursive'
            
			//print 'Project Folder: ' + projectName							
			
			pipeline.bat 'git checkout %BRANCH_NAME% && git pull'
			pipeline.bat 'git remote remove origin1'  //this is for gitversion. it can't handle more than one remote
		}
	}
	
	def sendSlackMessage(message, color = 'good', channel = '#builds'){
		pipeline.echo message
		
		//pipeline.slack.sendMessage(message, color, channel)
		
		//pipeline.echo "HELLO"
	}
}