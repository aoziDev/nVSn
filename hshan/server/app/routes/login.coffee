module.exports = (app) ->
	UserModel = app.User
	user = new UserModel()

	app.post '/signup', (req, res) ->
		user.email = req.body.email
		user.password = req.body.password
		UserModel.findOne email: user.email, (err, _user) ->
			res.send status:400, message:'Error' if err
			(user.save () ->) if not _user

		res.send status:200, message:'Sucess signup' 

	app.post '/login', (req, res) ->
		UserModel.findOne email: req.body.email, (err, _user) ->
			res.send status:400, message:'Error' if err

			if _user and _user.authenticate req.body.password
				req.session.login_id = _user.email
				res.send status:200, message:'Success to login'
			else
				res.send status:401, message:'Unauthoized'

	app.get '/logout', (req, res) ->
		delete req.session?.login_id
		res.send status:200, message:'Success logout'	
	
	app.get '/sessionInfo', (req, res) ->
		login_id = req.session.login_id
		console.log "login_id #{login_id}"
		if login_id
			res.send status:200, message:'Valid session.', user_id:login_id 
		else 
  			res.send status:400, message:'Invalid session.'
	 

