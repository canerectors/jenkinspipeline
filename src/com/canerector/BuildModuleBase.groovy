abstract class BuildModuleBase{

	def script
	
	BuildModuleBase(script){ this.script = script }

	abstract def performBuild()
	
	def sendSlackMessage(message, color = 'good'){
	
	}
}