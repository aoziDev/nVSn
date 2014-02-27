models = require '../modules/models' 

module.exports = (app) ->
	app.get '/login', (req, res) ->
		result = {}
		result.session = req.session
		res.send result 

	app.post '/signup', (req, res) ->
		models.addUser(req, res)	
		res.send 'signup'

