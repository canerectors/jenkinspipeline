package com.canerector

abstract class BuildModuleBase{

	def pipeline
	
	BuildModuleBase(pipeline){ this.pipeline = pipeline }

	abstract def performBuild()
	
	@NonCPS
	def sendSlackMessage(message, color = 'good', channel = '#builds'){
		pipeline.slack.sendMessage(message, color, channel)
	}
}