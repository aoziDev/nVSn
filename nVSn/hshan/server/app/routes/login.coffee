module.exports = (app) ->
	UserModel = app.User
	user = new UserModel()

	app.post '/signup', (req, res) ->
		user.email = req.body.email
		user.password = req.body.password
		UserModel.findOne email: user.email, (err, _user) ->
			console.log _user
			(user.save () ->) if not _user and not err
		res.send ''

	app.get '/login', (req, res) ->
		UserModel.findOne email: req.body.email, (err, _user) ->
			if _user and _user.authenticate req.body.password
				req.session.login_id = user.email
			else
				console.log 'fail login'
		res.send ''

	app.get '/logout', (req, res) ->
		delete req.session?.login_id
		res.send ''	
	
	
