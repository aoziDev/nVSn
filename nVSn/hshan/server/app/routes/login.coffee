module.exports = (app) ->
	UserModel = app.User
	user = new UserModel()

	app.post '/signup', (req, res) ->
		user.email = req.body.email
		user.password = req.body.password
		UserModel.findOne email: user.email, (err, _user) ->
			console.log _user
			(user.save () ->) if not _user and not err
		res.send 'signup'

	app.post '/login', (req, res) ->
		UserModel.findOne email: req.body.email, (err, _user) ->
			if _user and _user.authenticate req.body.password
				req.session.login_id = user.email
				result = 'sucess'
				console.log "login success result = #{result}"
			else
				result = 'fail'
				console.log "login fail result = #{result}"
				console.log 'fail login'

			console.log "before return = #{result}"
			res.send "{result:#{result}}" 

	app.get '/logout', (req, res) ->
		delete req.session?.login_id
		res.send 'logout'	
	
	
