def restore(){
	bat 'dotnet restore'
}

def publishPackage(packageName = '*.nupkg'){

	withCredentials([string(credentialsId: 'nuget-api-key', variable: 'API_KEY')]) {
        bat 'dotnet nuget push ' + packageName + ' --source https://www.myget.org/F/canerectors/api/v2/package --api-key %API_KEY% --symbols-source https://www.myget.org/F/canerectors/symbols/api/v2/package'                
    }
}

def publishSymbols(packageName = '*symbols.nupkg') {

	withCredentials([string(credentialsId: 'nuget-api-key', variable: 'API_KEY')]) {
        bat 'nuget push ' + packageName + ' --source https://www.myget.org/F/canerectors/symbols/api/v2/package --api-key %API_KEY% -NonInteractive'               
    }
}

def pack(projectName) {
	bat 'dotnet pack ' + projectName + ' -o .. -c Release --include-source --no-build /P:GenerateAssemblyInfo=false'
}

def getFeedUrl(projectName){
	return 'https://www.myget.org/feed/canerectors/package/nuget/' + projectName
}

return this;