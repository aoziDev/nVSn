_ = require 'underscore'
bcrypt = require 'bcrypt'
CONFIG = require 'config'
cormo = require 'cormo'
redis = require 'redis'

connection = cormo.Connection.defaultConnection
Model = cormo.Model

redis_client = redis.createClient 6379, '127.0.0.1'

##
# User 모델
class User extends Model
  @beforeValidate ->
    if @email
      @email = @email.trim()
    return

  ##
  # 사용자 이메일
  #
  # 이메일 규칙상 이름 부분은 대소문자 구별을 해야 한다(도메인 부분은 구별하지 않음,
  # 또 대다수 서비스가 이름 부분도 대소문자 구별을 하지 않을 것으로 생각됨)
  # 그래서 DB에는 원본 그대로 저장하고, 활용하고 싶다.
  # 하지만 사요자가 대소문자를 잘못 입력해도 로그인이 되는 편이 좋기 때문에,
  # 찾을 때는 대소문자 구분을 하지 않는다.
  #
  # 이를 위한 바람직한 해결책은 원 email과 소문자로 변환된 email을 따로 가지고 있는 것이지만구현할게 많아지기에, 일단은 검색시 사용자가 입력한 email과 소문자로 변환된 email 두가지로 사용자 레코드를 찾도록 했다.
  # @property email
  # @type String
  @column 'email', { type: String, required: true, unique: true }

  ##
  # bcrypt로 암호화된 사용자 암호
  # @property password
  # @type String
  @column 'password', String

  ##
  # 사용자 이름
  # @peroperty full_name
  # @type String
  @column 'full_name', { type: String, required: true }

  ##
  # 암호를 암호화한다
  @encryptPassword: (password, callback) ->
    cormo.console_execute callback, (callback) =>
      if not CONFIG.dont_encrypt_password and password
        bcrypt.genSalt 10, (error, salt) ->
          return callback error if error
          bcrypt.hash password, salt, (error, hash) ->
            callback error, hash
      else
        callback null, password

  ##
  # 사용자 정보를 반환한다.
  # @param {RecordID} user_id
  # @param {Function} callback
  # @param {Error} callback.error
  # @param {User} callback.user
  @getInfo: (user_id, callback) ->
    cormo.console_future.execute callback, (callback) =>
      @find(user_id).select('email full_name').exec (error, user) ->
        return callback error if error
        callback null, user

  ##
  # 사용자 인증을 한다.
  # @param {Object} user_info
  # @param {RecordID} [user_info.id}
  # @param {String} [user_info.email]
  # @param {String] user_info.password
  # @param {Boolean} [user_info.can_empty=false]
  # @param {Function} callback
  # @param {Error} callback.error
  # @param {User} callback.user (email,password)
  @authenticate: (user_info, callback) ->
    cormo.console_future.execute callback, (callback) =>
      params = []
      if user_info.id
        params.push id: user_info.id
      else if user_info.email
        email = user_info.email.trim()
        params.push email: email
        params.push email: email.toLowerCase()
      else
        return callback new Error('no user identifier')

      @where($or: params).select('email password').exec (error, users) ->
        if users and users.length is 0
          error = new Error('no exist')
        return callback error if error

        # 임시 암호가 있는 경우 그 암호와 같아도 로그인 성공
        redis_client.get 'TempPW:'+users[0].email, (error, data) ->
          return callback null, users[0] if not error and data and data is user_info.password

          if CONFIG.dont_encrypt_password
            if user_info.password is users[0].password
              callback null, users[0]
            else
              callback new Error('wrong password')
            return
          
          bcrypt.compare user_info.password, users[0].password, (error, same) ->
            return callback null, users[0] if same and not error
            callback new Error('wrong password')

  ##
  # 사용자를 생성한다.
  # @param {Object} user_info
  # @param {String} user_info.email
  # @param {String} user_info.password
  # @param {String} user_info.full_name
  # @param {Function} callback
  # @param {Error} callback.error
  # @param {User} callback.user
  @createUser: (user_info, callback) ->
    cormo.console_future.execute callback, (callback) =>
      return callback 'no email' if not user_info.email
      return callback 'no full_name' if not user_info.full_name
      @encryptPassword user_info.password, (error, password) =>
        return callback error if error
        user_info.password = password
        @create user_info, (error, user) ->
          return callback error if error
          callback null, user
