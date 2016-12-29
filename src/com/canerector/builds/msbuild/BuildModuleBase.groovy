package com.canerector.builds.msbuild

abstract class BuildModuleBase  implements Serializable {
	
	def pipeline
	
	BuildModuleBase(pipeline){ this.pipeline = pipeline }
	
	def projectName
	def projectBranchName
	def gitHubUrl
	def slackFormattedGitHubUrl
	def slackFormattedBuildUrl
	
	def version
	
	def performBuild(){
	
		projectName = pipeline.env.JOB_NAME.replace('canerectors/', '').replace('/' + pipeline.env.BRANCH_NAME, '')
		projectBranchName = projectName + ':' + pipeline.env.BRANCH_NAME
		gitHubUrl = pipeline.github.getProjectUrl(projectName, pipeline.env.BRANCH_NAME)
		slackFormattedGitHubUrl = pipeline.slack.getMessageStringForUrl(gitHubUrl, projectBranchName)
		slackFormattedBuildUrl = pipeline.slack.getMessageStringForUrl(pipeline.env.BUILD_URL, 'Build #' + pipeline.env.BUILD_NUMBER)
		
		pipeline.timestamps{
			try{
				sendSlackMessage('started for project: ' + slackFormattedGitHubUrl)		
	
				performBuildInternal()
			}
			catch(err){
		
				pipeline.echo err
		
				def consoleUrl = pipeline.slack.getMessageStringForUrl(pipeline.env.BUILD_URL + 'console', 'Build Log.')		
		
				sendSlackMessage('for project: ' + slackFormattedGitHubUrl + ' failed. See ' + consoleUrl, 'danger')		
		
				pipeline.currentBuild.result = 'FAILURE'
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

	def nugetRestore(){
		pipeline.stage('Nuget Restore') {
			pipeline.bat 'dotnet restore'
		}
	}
	
	def version(){
		pipeline.stage('Apply Versioning') {
			pipeline.versioning.emitGitVersionConfigFile()
				
			pipeline.bat 'cd ' + projectName + ' && dotnet setversion'
		}
		
		version = pipeline.versioning.getVersionFromBuildOutput()
	}
	
	def build(){
		pipeline.stage('Build') {
			pipeline.bat 'cd ' + projectName + ' && dotnet build --no-incremental -c Release'
		}
	}
	
	def tests(){
		def testFolder = projectName + '.Tests'
		
		def hasTests = fileExists(testFolder)	
				
		if(hasTests){
			pipeline.stage('Tests'){
					
				success = pipeline.testing.runTests(testFolder)
				 
				if (!success){
					sendMessage('Testing failed for: ' + slackFormattedGitHubUrl + ' version: ' + version, 'danger')
					bat 'exit 1'
				}			
			}
		}
		else
			echo 'No tests found.'
	}

	def sendSlackMessage(message, color = 'good', channel = '#builds'){
		pipeline.node{
			pipeline.slack.sendMessage(slackFormattedBuildUrl + ' ' + message, color, channel)
		}
	}
}