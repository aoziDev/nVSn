strings =

exports.getString = (lang, code) ->
  table = strings[code]
  if table and table.message
    if lang
      description = table[lang]
      description = table[lang.substr(0, 2)] if not description and lang.length > 2
    description = table.en if not description
    return description
  return code

makeError = (code, message, description) ->
  e = new Error message
  e.code = code
  e.description = description
  return e

exports.getError = (lang, code) ->
  table = strings[code]
  if table and table.message
    if lang
      description = table[lang]
      description = table[lang.substr(0, 2)] if not description and lang.length > 2
    description = table.en if not description
    return makeError code, table.message, description
  return makeError code, code, ''
