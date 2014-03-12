##
# DB Model
# @module model


CONFIG = require 'config'

cormo = require 'cormo'
Connection = cormo.Connection
database_type = CONFIG.database.type
connection = new Connection database_type, CONFIG.database

connection.once 'connected', ->
  client = connection._adapter._client
  client.serverConfig.on 'error', (error) ->
    console.log '[server] Connection error by', error
    process.exit 0
  client.serverConfig.on 'timeout', (error) ->
    console.log '[server] Connection timeout by', error
    process.exit 0

require './user'

module.exports = connection
