package com.canerector

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