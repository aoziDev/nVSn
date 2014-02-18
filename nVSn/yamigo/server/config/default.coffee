path = require 'path'

module.exports =
  project_root: path.resolve __dirname, '..'

  app_title: 'nVSn'
  
  log4js_config:
    appenders: [ {
      type: 'console'
    }, {
      type: 'log4js-appender-mongodb'
      database: 'nVSn'
    } ]
    replaceConsole: false

  session_ttl: 86400
  session_secret: 'nVSn'
