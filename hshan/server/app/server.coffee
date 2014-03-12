cluster = require 'cluster'
domain = require 'domain'

log = (msg) ->
	console.log '[server]', msg

registerHandlers = ->
	cluster.on 'exit', (worker, code, signal) ->
		fork worker.exec

fork = (exec) ->
	cluster.setupMaster()
	cluster.settings.exec = exec
	worker = cluster.fork()
	worker.exec = exec

startWorkers = ->
	numCPUs = require('os').cpus().length
	numCPUs = 1 if single_process

	for i in [0...numCPUs]
		fork __dirname + '/app.coffee'

destroyWorkers = ->
	worker.destroy() for id, worker of cluster.workers

startWatch = ->
	ignoreDirectories = []
	extensions = ['.coffee']
	fs = require 'fs'
	basename = require('path').basename
	extname = require('path').extname

	watch = (file) ->
		return if extensions.indexOf(extname file) < 0
		log 'watching... ' + file
		fs.watchFile file, interval:100, (curr, prev) ->
			if curr.mtime > prev.mtime
				log 'changed - ' + file
				destroyWorkers()

	traverse = (file) ->
		fs.stat file, (err, stat) ->
			return if err
			if stat.isDirectory()
				return if ignoreDirectories.indexOf(basename file) >= 0
				fs.readdir file, (err, files) ->
					files.map((f) -> "#{file}/#{f}").forEach traverse
			else
				watch file

	traverse __dirname
	traverse __dirname + '/../config'

do_watch = true
single_process = true

do registerHandlers
do startWorkers
do startWatch if do_watch

