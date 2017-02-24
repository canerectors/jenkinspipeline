@NonCPS
def getVersionFromBuildOutput() {
    def matcher = manager.getLogMatcher(".*\"SemVer\":(.*)\$") //manager.getLogMatcher(".*Setting version to: (.*)\$")

	if (matcher?.matches()) {
        def match = matcher.group(1).toString().replace('\"','').replace(',','')
		bat '@echo Found version: ' + match
        return match
    }
	else
		bat '@echo No Version found in build output.'
	
	//def matches = manager.build.logFile.text =~ ".*Setting version to: (.*)\$"
	
    //if (matches.length > 0) {
    //    def match = group(1).toString()
    //    return match;
    //}
}

def emitGitVersionConfigFile()
{
	bat '@echo mode: Mainline > GitVersion.yml && echo branches: {} >> GitVersion.yml && echo ignore: >> GitVersion.yml && echo   sha: [] >> GitVersion.yml'
}

return this;