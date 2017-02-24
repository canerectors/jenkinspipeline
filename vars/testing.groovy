def runTests(testProjectFolder){
	
	def success = true
	
	try {
		testProjectFile = testProjectFolder + '\\' + testProjectFolder + '.csproj'
		bat 'dotnet test ' + testProjectFile + ' /P:GenerateAssemblyInfo=false' // + ' -xml xunit.xml'
	} catch (Exception err) {
		echo "Caught exception: ${err}"
		success = false
	}
	
	step([$class: 'XUnitBuilder',
		thresholds: [
			[$class: 'FailedThreshold', failureThreshold: '0']
		],
		tools: [
			[$class: 'XUnitDotNetTestType', pattern: 'xunit.xml']
		]
	])
	
	return success;
}


return this;