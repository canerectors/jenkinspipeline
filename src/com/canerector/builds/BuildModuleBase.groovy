package com.canerector.builds

abstract class BuildModuleBase implements Serializable {
	
	def pipeline

	BuildModuleBase(pipeline){
		this.pipeline = pipeline		
	}

	def org
	def branch
	def projectName
	def projectShortName
	def projectBranchName
	def gitHubUrl
	def slackFormattedGitHubUrl
	def slackFormattedBuildUrl
	def consoleUrl
	def buildProject
	
	def version
	
	def performBuild(){
		performBuild(null)
	}
	
	def performBuild(projectToBuild){
	
		def tokens = pipeline.env.JOB_NAME.tokenize('/')
		
		org = tokens[tokens.size()-3]
		projectName = tokens[tokens.size()-2]
		branch = tokens[tokens.size()-1]
		
		if(projectToBuild != null)
		{			
			buildProject = projectToBuild.replace('${PROJECT_NAME}', projectName)
		}
		else
		{
			buildProject = projectName
		}
		
		projectBranchName = projectName + ':' + branch
		gitHubUrl = pipeline.github.getProjectUrl(projectName, branch)
		slackFormattedGitHubUrl = pipeline.slack.getMessageStringForUrl(gitHubUrl, projectBranchName)
		slackFormattedBuildUrl = pipeline.slack.getMessageStringForUrl(pipeline.env.BUILD_URL, 'Build #' + pipeline.env.BUILD_NUMBER)
		consoleUrl = pipeline.slack.getMessageStringForUrl(pipeline.env.BUILD_URL + 'console', 'View Build Log.')
		projectShortName = projectName.replace('CanErectors.', '').toLowerCase()
		
		pipeline.timestamps{
			try{
				sendSlackMessage('started for project: ' + slackFormattedGitHubUrl + ' ' + consoleUrl)		
				pipeline.echo pipeline.build.getChangeString()
				performBuildInternal()
				
				pipeline.manager.addShortText("v" + version)	
			}
			catch(err){

				sendSlackMessage('for project: ' + slackFormattedGitHubUrl + ' failed. ' + consoleUrl, 'danger')		
		
				pipeline.currentBuild.result = 'FAILURE'
				
				throw err
			}
		}
	}
	
	abstract def performBuildInternal()
	
	def clean(){
	
		pipeline.stage('Clean') {
			pipeline.deleteDir()
		}
	}
	
	def checkout(){
	
		pipeline.stage('Checkout') {
			pipeline.checkout pipeline.scm
			
			pipeline.bat 'git submodule update --init --recursive'								
			
			pipeline.bat 'git checkout %BRANCH_NAME% && git pull'
			pipeline.bat 'git remote remove origin1'  //this is for gitversion. it can't handle more than one remote
		}
	}

	def version(updateAssemblyInfo = true){
		pipeline.stage('Versioning') {
			pipeline.versioning.emitGitVersionConfigFile()
				
			def versionCommand = 'cd ' + projectName + ' && gitversion'
			
			if(updateAssemblyInfo){
				versionCommand = versionCommand + ' /updateassemblyinfo'
			}				
			
			pipeline.bat versionCommand
		}
		
		version = pipeline.versioning.getVersionFromBuildOutput()
	}
	
	def sendSlackMessage(message, color = 'good', channel = '#builds'){
		pipeline.node{
			pipeline.slack.sendMessage(slackFormattedBuildUrl + ' ' + message, color, channel)
		}
	}
}