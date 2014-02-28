process.env.NODE_CONFIG_DIR = __dirname + '/../config'

CONFIG = require 'config'
domain = require 'domain'
express = require 'express'
http = require 'http'
redis = require 'redis'
RedisStore = require('connect-redis')(express)

app = express()
server = http.createServer app

app.set 'trust proxy', true

redis_client = redis.createClient 6379, '127.0.0.1'
app.sessionStore = new RedisStore client: redis_client, ttl: CONFIG.session_ttl

app.use express.json()
app.use express.urlencoded()
app.use express.methodOverride()
app.use express.cookieParser()

app.use express.session store: app.sessionStore, secret: CONFIG.session_secret, cookie: maxAge: CONFIG.session_ttl * 1000

if global.public_html_directory
  public_html_directory = global.public_html_directory
else if CONFIG.public_html_directory
  public_html_directory = CONFIG.public_html_directory
if public_html_directory
  console.log '[server] Serving static files in ' + public_html_directory + '...'
  app.use express.static public_html_directory

# 라우트 처리 중 발생한 예외를 가로채기 위한 middleware
# RedisClient에 stream이 있어서 그 다음에 위치해야 기대한 대로 동작한다.
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

require('./routes')(app)

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
    console.log ' cuased by', error.cause.message
    console.log error.cause.stack
d.run ->
  console.log '[server] Start Application...'
  server.listen 3000
