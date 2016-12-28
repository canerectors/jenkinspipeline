@NonCPS
def sendMessage(message, color = 'good', channel = '#builds') {
    node{
		withCredentials([string(credentialsId: 'slack-key', variable: 'TOKEN')]) {            
			slackSend channel: channel, color: color, message: message, teamDomain: 'canerectors', token: "${env.TOKEN}"
		}
	}
}

def getMessageStringForUrl(url, message){
	return '<' + url + '|' + message + '>'
}

return this;