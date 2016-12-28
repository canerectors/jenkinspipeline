def runTests(testProjectFolder){
	
	def success = true
	
	try {
		bat 'dotnet test ' + testProjectFolder + ' -xml xunit.xml'
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