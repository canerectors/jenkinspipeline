package com.canerector

def checkout(){
	
	stage('Checkout') {
		checkout scm
			
		//pipeline.bat 'git submodule update --init --recursive'
            
								
			
		//pipeline.bat 'git checkout %BRANCH_NAME% && git pull'
		//pipeline.bat 'git remote remove origin1'  //this is for gitversion. it can't handle more than one remote
	}
}