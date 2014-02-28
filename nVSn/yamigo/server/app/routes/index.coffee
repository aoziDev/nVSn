CONFIG = require 'config'

models = require '../models'
User = models.User

_getConnectionState = ->
  try
    client = models._adapter._client
    connections = client.serverConfig.allRawConnections()
    return {
      poolState: client.serverConfig.connectionPool._poolState
      connected: connections.map (c) -> c.connected
    }

##
# Routes for Express
# @module routes
module.exports = (app) ->
  app.use (req, res, next) ->
    origin = req.get 'Origin'
    if origin is 'http://localhost:9000' or origin is 'http://127.0.0.1:9000' or origin is 'http://10signals.croquis.com'
      res.set 'Access-Control-Allow-Origin', origin
      res.set 'Access-Control-Allow-Credentials', true
      res.set 'Access-Control-Allow-Headers', 'X-Requested-With, Content-Type'
      res.set 'Access-Control-Allow-Methods', 'GET, POST, PUT, DELETE'
      if req.method is 'OPTIONS'
        req.skip_logging = true
        return res.send {}
    next()

  app.get '/api/check', (req, rest) ->
    req.skip_logging = true
    data = 
      memory: process.memoryUsage()
      connection: _getConnectionState()
    if req.query.skip_db
      res.send data
    else
      User.where(email: 'never_exist').select('').exec (error, users) ->
        data.db = not error and users and users.length is 0
        code = if data.db then 200 else 400
        res.send code, data

  require('./login')(app)

  # send 404 error for unknown url
  app.all '*', (req, res) ->
    res.sendError 404, 'route_missing_api'
