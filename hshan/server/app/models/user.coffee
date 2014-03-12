crypto = require 'crypto'

defineModel = (mongoose, fn) ->
	Schema = mongoose.Schema
	ObjectId = Schema.ObjectId

	#Model: User
	User = new Schema
		'email': type:String, index: unique: true
		'salt' : String
		'hashed_password': String
		
	User.virtual('password')
		.set (password) ->
			@_password = password
			@salt = do @.makeSalt
			@hashed_password = @.encryptPassword password
		.get ->
			@_password

	User.method 'makeSalt', ->
		Math.round (new Date().valueOf() * Math.random()) + '';

	User.method 'authenticate', (password) ->
		@hashed_password is @encryptPassword(password) 
					
 
	User.method 'encryptPassword', (password) ->
		crypto.createHmac('sha1', @salt).update(password).digest 'hex'

	User.pre 'save', (next) ->
		do next					

	mongoose.model('User', User)

	do fn

exports.defineModel = defineModel
