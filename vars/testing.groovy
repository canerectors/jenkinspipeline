def runTests(testProjectFolder){
	
	def success = true
	
	try {
		testDllFile = testProjectFolder + '\\bin\\Release\\net461\\' + testProjectFolder + '.dll'
		testProjectFile = testProjectFolder + '\\' + testProjectFolder + '.csproj'
		bat 'dotnet build -c Release ' + testProjectFile + ' /P:GenerateAssemblyInfo=false'
		bat 'c:\\packages\\xunit.runner.console\\2.2.0\\tools\\xunit.console.exe ' + testDllFile + ' -xml xunit.xml'
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