CONFIG = require 'config'
crypto = require 'crypto'
mongoose = require 'mongoose'

db = mongoose.connect CONFIG.db_uri

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

	User.method 'encryptPassword', (password) ->
		crypto.createHmac('sha1', @.salt).update(password).digest 'hex'

	User.pre 'save', (next) ->
		console.log 'pre save..'
		do next					

	mongoose.model('User', User)

do defineModels

exports.addUser = (req, res) ->
	User = mongoose.model('User')
	console.log req.body
	user = new User()
	user.email = req.body.email
	user.password = req.body.password
	user.save (err) ->
