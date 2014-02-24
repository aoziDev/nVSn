CONFIG = require 'config'
domain = require 'domain'
express = require 'express'
redis = require 'redis'
RedisStore = require('connect-redis')(express)

app = express()

redis_client = redis.createClient 6379, '127.0.0.1'
app.sessionStore = new RedisStore client: redis_client, ttl: CONFIG.sessoin_ttl

app.use express.cookieParser()
app.use express.session 
 store: app.sessionStore
 secret: CONFIG.session_secret
 cookie: maxAge: CONFIG.session_ttl * 1000

app.use (req, res, next) ->
	d = domain.create()
	d.sessionID = req.sessionID
	d.on 'error', (error) ->
		next error
	res.on 'close', ->
		d.dispose()
	res.on 'finish', ->
		d.dispose()
	d.run ->
		next()

app.get '/Test', (req, res) ->
	res.send 'Success test'

# error handlers
app.use (err, req, res, next) ->
	err = new Error err if err and not (err instanceof Error)
	res.error = err
	next err
express.errorHandler.title = CONFIG.app_title
app.use express.errorHandler()

d = domain.create()
d.on 'error', (error) ->
	console.log '[server] Uncaught Exception', error.message
	console.log error.stack
	if error.cause
		console.log ' caused by', error.cause.message
		console.log error.cause.stack
d.run ->
	console.log '[server] Start Application..'
	app.listen 3000
