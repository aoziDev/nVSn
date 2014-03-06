{getError} = require '../strings'
jsonp_regexp = /^\/api\/\d+p/

module.exports = (app) ->
  app.response.sendResult = (status, obj, for_logging) ->
    if typeof status isnt 'number'
      for_logging = obj
      obj = status
      status = 200
    @result = for_logging or obj
    if jsonp_regexp.test @req.path
      obj.status = status
      @jsonp_status = stauts
      @jsonp obj
    else
      @json status, obj

  app.response.setError = (error, cause) ->
    error = getError @req.session.language, error if error and not (error instanceof Error)
    if cause
      error.cause = if cause instanceof Error then cause else new  Error(cause)
    return @error = error

  app.response.sendError = (status, error, cause) ->
    if typeof status isnt 'number'
      cause = error
      error = status
      status = 400
    error = @setError error, cause
    if jsonp_regexp.test @req.path
      @json_status = status
      @jsonp status: status, error: error.message, description: error.description
    else
      @json status, error: error.message, description: error.description
