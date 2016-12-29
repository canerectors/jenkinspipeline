package com.canerector.builds.msbuild

def clean(){
	stage('Clean') {
		deleteDir()
	}
}

def checkout(){
	
	stage('Checkout') {
		checkout scm
			
		bat 'git submodule update --init --recursive'								
			
		bat 'git checkout %BRANCH_NAME% && git pull'
		bat 'git remote remove origin1'  //this is for gitversion. it can't handle more than one remote
	}
}

def nugetRestore(){
	stage('Nuget Restore') {
		bat 'dotnet restore'
	}
}

def version(){
	stage('Apply Versioning') {
		versioning.emitGitVersionConfigFile()
			
		bat 'cd ' + projectName + ' && dotnet setversion'
	}
}

def build(){
	stage('Build') {
		bat 'cd ' + projectName + ' && dotnet build --no-incremental -c Release'
	}
}

def tests(){
	def testFolder = projectName + '.Tests'
	
	def hasTests = fileExists(testFolder)	
			
	if(hasTests){
		stage('Tests'){
                
			success = testing.runTests(testFolder)
			 
			if (!success){
				sendMessage('Testing failed for: ' + slackFormattedGitHubUrl + ' version: ' + version, 'danger')
				bat 'exit 1'
			}			
		}
	}
	else
		echo 'No tests found.'
}