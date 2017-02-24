def restore(){
	bat 'dotnet restore'
}

def publishPackage(packageName = '*.nupkg'){

	withCredentials([string(credentialsId: 'nuget-api-key', variable: 'API_KEY')]) {
        bat 'del *.symbols.nupkg && nuget push ' + packageName + ' -Source https://www.myget.org/F/canerectors/api/v2/package -ApiKey  %API_KEY% -NonInteractive'                
    }
}

def publishSymbols(packageName = '*symbols.nupkg') {

	withCredentials([string(credentialsId: 'nuget-api-key', variable: 'API_KEY')]) {
        bat 'nuget push ' + packageName + ' -Source https://www.myget.org/F/canerectors/symbols/api/v2/package -ApiKey %API_KEY% -NonInteractive'               
    }
}

def pack(projectName) {
	bat 'dotnet pack ' + projectName + ' -o .. -c Release --include-source --no-build /P:GenerateAssemblyInfo=false'
}

def getFeedUrl(projectName){
	return 'https://www.myget.org/feed/canerectors/package/nuget/' + projectName
}

return this;