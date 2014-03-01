CONFIG = require 'config'
crypto = require 'crypto'
mongoose = require 'mongoose'

db = mongoose.connect CONFIG.db_uri
UserModel = ->

defineModels = ->
	Schema = mongoose.Schema
	ObjectId = Schema.ObjectId

	#Model: User
	User = new Schema
		'email': type:String, index: unique: true
		'salt' : String
		'hashed_password': String
		
	User.virtual('password')
		.set (password) ->
			@._password = password
			@.salt = do @.makeSalt
			@.hashed_password = @.encryptPassword password
		.get ->
			@._password

	User.method 'makeSalt', ->
		Math.round (new Date().valueOf() * Math.random()) + '';

	User.method 'authenticate', (password) ->
		@.hashed_password is @.encryptPassword(password) 
					
 
	User.method 'encryptPassword', (password) ->
		crypto.createHmac('sha1', @.salt).update(password).digest 'hex'

	User.pre 'save', (next) ->
		do next					

	UserModel = mongoose.model 'User', User

do defineModels

exports.addUser = (req, res) ->
	user = new UserModel()
	user.email = req.body.email
	user.password = req.body.password
	UserModel.findOne email: user.email, (err, _user) ->
		console.log _user
		(user.save () ->) if not _user and not err

exports.login = (req, res) ->
	UserModel.findOne email: req.body.email, (err, _user) ->
		if _user and _user.authenticate req.body.password
			console.log "session.login_id : #{req.session.login_id}"
			console.log 'sucess login'
			console.log "_user.email : #{_user.email}"
			req.session.login_id = _user.email
			console.log "after set req.session.login_id : #{req.session.login_id}"
		else
			console.log 'fail login'

exports.logout = (req, res) ->
	console.log 'before => ' + req.session.login_id
	delete req.session?.login_id
	console.log 'after => ' + req.session.login_id
	console.log 'logout'
