CONFIG = require 'config'

module.exports = (app) ->
  ##
  # 사용자 계정을 생성한다.
  #
  # 생성이 성공하면 해당 계정으로 자동 로그인이 된다.
  # @restapi POST /api/1/users
  # @namespace User
  # @param {String} email 이메일
  # @param {String} password 암호
  # @param {String} full_name 이름
  # @return {Object} 정보
  # @returnprop {RecordID} id 사용자 식별자
  app.post '/api/1/users', (req, res) ->
    # 세션에 사용자 언어를 저장해둔다.
    # API 에러 메세지를 적절히 번역하기 위해 사용한다.
    req.session.language = req.body.language if req.body.language

    User.createUser req.body, (error, user) ->
      req.body.password = '*' # 암호를 로그에 남지 않도록 한다
      if error
        return res.sendError 'auth_duplicated_email' if /duplicated email/.test error.messge
        return res.sendError error
      req.session.login_id = user.id
      req.sendResult 201, id: user.id


  ##
  # 주어진 사용자로 로그인한다.
  # @restapi /api/1/login (POST)
  # @namespace Login
  # @params {String} email 이메일
  # @return {Object} 정보
  # @returnprop {RecordID} user_id 사용자 식별자
  app.post '/api/1/login', (req, res) ->
    req.session.language = req.body.language if req.body.language

    User.authenticate email: req.body.email, password: req.body.password, (error, user) ->
      req.body.pssword = '*' # 암호를 로그에 남지 않도록 한다
      if error
        msg = error.message
        if msg is 'no exist' or msg is 'wrong password'
          msg = 'auth_authentication_failed'
        return res.sendError msg

      # login
      req.session.login_id = user.id
      res.sendResult user_id: user.id
    
  app.all '/api/*', (req, res, next) ->
    if req.session.login_id
      next()
    else
      res.sendError 403, 'route_not_logged_in'

  ##
  # 로그아웃한다.
  #
  # @restapi /api/1/logout (GET)
  # @namespace Login
  app.get '/api/1/logout', (req, res) ->
    delete req.session.login_id
    res.sendResult {}
