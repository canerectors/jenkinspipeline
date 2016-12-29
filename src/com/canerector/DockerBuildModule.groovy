package com.canerector


	def performBuild(){
	
		def stages = new com.canerector.MsbuildStages()	

		node{
			stages.checkout()
		}
	}
