@NonCPS
def getVersionFromBuildOutput() {
    def matcher = manager.getLogMatcher(".*Setting version to: (.*)\$")

	if (matcher?.matches()) {
        def match = matcher.group(1).toString()
        return match;
    }
	
	//def matches = manager.build.logFile.text =~ ".*Setting version to: (.*)\$"
	
    //if (matches.length > 0) {
    //    def match = group(1).toString()
    //    return match;
    //}
}

def emitGitVersionConfigFile()
{
	bat 'echo mode: Mainline > GitVersion.yml'
    bat 'echo branches: {} >> GitVersion.yml'
    bat 'echo ignore: >> GitVersion.yml'
    bat 'echo   sha: [] >> GitVersion.yml'
}

return this;