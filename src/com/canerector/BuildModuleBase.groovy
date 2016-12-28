package com.canerector

abstract class BuildModuleBase{

	def pipeline
	
	BuildModuleBase(pipeline){ this.pipeline = pipeline }

	abstract def performBuild()
	
	
	def sendSlackMessage(message, color = 'good', channel = '#builds'){
		pipeline.echo "HELLO"
		
		pipeline.slack1.sendMessage(message, color, channel)
		
		pipeline.echo "HELLO"
	}
}