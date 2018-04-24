"use strict";

//include
const https = require('https');
const express = require('express');
const app = express();
const path = require('path');
const formidable = require('formidable');
const fs = require('fs');
const basicAuth = require('express-basic-auth')
const winston = require('winston')
const requestLogger = require('express-request-logs');
const dateFormat = require('dateformat');


//config
const HTTPS_PORT = 3000


//logger
const winstonLogger = new (winston.Logger)({ transports: [ new (winston.transports.Console)() ] });
winston.level = process.env.NODE_LOG_LEVEL   || "info"

app.use(requestLogger.create(winstonLogger, {
	plugins: {
    timestamp: ({req, res, startTime}) => dateFormat(new Date(), "yyyy-mm-dd HH:MM:ss"),
    proto: ({req, res, startTime}) => req.protocol.toUpperCase(),
    code: ({req, res, startTime}) => res.statusCode
	},
	format: ({timestamp, proto, user, remoteIP, method, code, url, responseTime}) =>
		`${timestamp} - ${proto} - ${remoteIP.replace(/::ffff:/i, '')} ${method} ${code} ${url} took=${responseTime}`
}));


//create uploads dir if not exists
if (!fs.existsSync(path.join(__dirname, 'uploads'))){
    fs.mkdirSync(path.join(__dirname, 'uploads'));
}

//https settings
let options = {
	key: fs.readFileSync(path.join(__dirname, 'keys/key.pem')),
	cert: fs.readFileSync(path.join(__dirname, 'keys/cert.pem'))
};


//basic auth
app.use(basicAuth({
  //  users: { 'userA': 'pass1' },
  challenge: true,
  realm: 'TestSite',
  authorizer: myAsyncAuthorizer,
  authorizeAsync: true
}))


function myAsyncAuthorizer(username, password, cb) {
  if(username == 'userA' && password == 'pass1'
   || username == 'userB' && password == 'pass2'
  )
      return cb(null, true)
  else{
      winston.warn('Username used: ' + username);
      return cb(null, false)     
  }
}


//remove header x-powered-by
app.disable('x-powered-by');

//js and css
app.use(express.static(path.join(__dirname, 'public')));


//root
app.get('/', function(req, res){
  res.sendStatus(200);
});

//home
app.get('/login', function(req, res){
  winston.info(req.headers['x-forwarded-for'] + " " + req.ip + " " + req.connection.remoteAddress)
  res.sendFile(path.join(__dirname, 'views/index.html'));
});

//upload
app.post('/upload', function(req, res){

  // create an incoming form object
  let form = new formidable.IncomingForm();

  // specify that we want to allow the user to upload multiple files in a single request
  form.multiples = true;

  // store all uploads in the /uploads directory
  form.uploadDir = path.join(__dirname, '/uploads');

  // every time a file has been uploaded successfully, rename it to it's orignal name with timestamp
  form.on('file', function(field, file) {
    fs.rename(file.path, path.join(form.uploadDir, dateFormat(new Date(), "yyyymmddhhMMss_") + file.name), (err) => { 
      if (err){
        winston.error('Rename error: \n' + err);
      }
    });
  });


  // log any errors that occur
  form.on('error', function(err) {
    winston.error('An error has occured: \n' + err);
  });

  // once all the files have been uploaded, send a response to the client
  form.on('end', function() {
    res.end('success');
  });

  // parse the incoming request containing the form data
  form.parse(req);

});


let serverHttps = https.createServer(options, app);

serverHttps.listen(HTTPS_PORT, function(){
  winston.info('Server listening on port ' + HTTPS_PORT);  
});
