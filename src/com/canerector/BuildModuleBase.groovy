package com.canerector

abstract class BuildModuleBase{

	def pipeline
	
	BuildModuleBase(pipeline){ this.pipeline = pipeline }

	abstract def performBuild()
	
	
	def sendSlackMessage(message, color = 'good', channel = '#builds'){
		pipeline.echo "HELLO"
		//pipeline.slack.sendMessage(message, color, channel)
	}
}