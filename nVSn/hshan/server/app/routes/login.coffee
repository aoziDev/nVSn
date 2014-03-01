models = require '../modules/models' 

module.exports = (app) ->
	app.get '/login', (req, res) ->
		models.login(req, res)
		res.send 'login' 

	app.post '/signup', (req, res) ->
		models.addUser(req, res)	
		res.send 'signup'

	app.get '/logout', (req, res) ->
		models.logout(req, res)
		res.send 'logout'
