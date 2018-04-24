//VALID PATHS
var ROOT = '/';
var INDEX = '/index';

var ALL_RATINGS = '/ratings';
var ALL_SYSLANGUAGES = '/languages';
var ALL_COUNTRIES = '/countries';

var MAINMENU = '/menu';
var MENU_SUBITEMS_BYID = MAINMENU + '/:id';
var RATING_SCORES = ALL_RATINGS + '/:asocId';

var GET_FILE = '/file/:id';
var LOGOUT = '/logout';

var LOGIN = '/login';
var REGISTER = '/register';
var INSERT_RATING = '/insRating';
var INSERT_FEEDBACK = '/insFeedback';

var JSON_IN = '/jsonInput';

// CONFIG
var IMAGES_PATH = "C:\\xampp\\htdocs\\isisadm\\multimedia\\";
var HTTP_PORT = 3000;
var HTTPS_PORT = 3001;

// BEGIN MAIN
var express = require('express');
var fs = require('fs');
var app = express();
var mime = require('mime');
var https = require('https');
var http = require('http');

var options = {
	key : fs.readFileSync('keys/key.pem'),
	cert : fs.readFileSync('keys/cert.pem')
};

app.use(express.bodyParser());
app.use(express.cookieParser("noBetterSecretThanISISone"));
db = require('./src/db');

db.init(function(res) {

	if (!res) {
		process.exit(code = 1);
	}

});

crypt = require('./src/sec');

// Create an HTTP service.
http.createServer(app).listen(HTTP_PORT);
console.log('Listening on port ' + HTTP_PORT + '...');
// Create an HTTPS service identical to the HTTP service.
https.createServer(options, app).listen(HTTPS_PORT);
console.log('Listening on port ' + HTTPS_PORT + '...');

// END MAIN

// BEGIN METHODS

// GET METHODS

app.get(MENU_SUBITEMS_BYID, function(req, res) {

	db.getMenuSubitems(req.params.id, function(results) {
		sendToClient(res, results);
	});

});

app.get(RATING_SCORES, function(req, res) {

	db.getRatingScores(req.params.asocId, checkAccess(req), function(results) {
		sendToClient(res, results);
	});

});

app.get(MAINMENU, function(req, res) {
	db.getMenuSubitems(1, function(results) {
		sendToClient(res, results);
	});

});

app.get(ALL_RATINGS, function(req, res) {
	db.getRatings(function(results) {
		sendToClient(res, results);
	});

});

app.get(ALL_SYSLANGUAGES, function(req, res) {
	db.getSystemLanguages(function(results) {
		sendToClient(res, results);
	});

});

app.get(GET_FILE, function(req, res) {
	db.getFileInfo(req.params.id, function(result) {

		if (result.length != 1) {
			res.send(500);
			return;
		}

		var type = mime.lookup(result[0].PATH);
		var path = IMAGES_PATH + result[0].PATH;

		fs.exists(path, function(exists) {

			if (exists) {
				var img = fs.readFileSync(IMAGES_PATH + result[0].PATH);

				res.writeHead(200, {
					'Content-Type' : type
				});
				res.end(img, 'binary');
			} else {
				res.send(404);
			}
		});

	});
});

app.get(ALL_COUNTRIES, function(req, res) {
	db.getAllCountries(function(results) {
		sendToClient(res, results);
	});

});

app.get(ROOT, function(req, res) {

	help(function(cb) {
		sendToClient(res, cb);
	});

});

app.get(INDEX, function(req, res) {

	help(function(cb) {
		sendToClient(res, cb);
	});

});

app.get(LOGOUT, function(req, res) {
	res.clearCookie('userId');
	res.redirect('back');
});

// POST METHODS

app.post(LOGIN, function(req, res) {

	// validity
	var days = 7 * 86400;

	if (req.body.user && req.body.pass) {

		db.getValidUserId(req.body.user, req.body.pass, function(cb) {

			if (cb == 0) {
				res.send(401);
				return;

			} else {

				crypt.comparePassword(req.body.pass, cb.HASH, function(match) {

					if (match) {
						console.log('User ' + req.body.user
								+ ' auth successfully');

						res.cookie('userId', cb.USER_ID, {
							maxAge : days
						});

						res.send(200);
					} else {
						console.log('User ' + req.body.user + ' auth ERR');
						res.send(401);
					}
				});

			}

		});

	}

});

app.post(REGISTER, function(req, res) {

	if (req.body.user && req.body.pass && req.body.email && req.body.cntId
			&& req.body.lngId) {

		db.getValidUserId(req.body.user, req.body.pass, function(cb) {

			if (cb != 0) {
				res.send("Username taken");
				return;
			} else {

				crypt.generateCryptHash(req.body.pass, function(err, hash) {

					if (err) {
						cb(500);
						return;
					}

					db.insertUser(req.body.user, hash, req.body.fname,
							req.body.lname, req.body.email, req.body.cntId,
							req.body.lngId);

				});

			}

			res.send(200);
		});

	}

});

app.post(INSERT_RATING, function(req, res) {

	// login necessary module
	var userId = checkAccess(req);

	if (userId == 0) {
		res.send(401);
		return;
	}

	if (req.body.mrAscId && req.body.scoreId) {

		db.setRating(userId, req.body.mrAscId, req.body.scoreId, function(cb) {
			sendToClient(res, cb);
		});
	}

});

app.post(INSERT_FEEDBACK, function(req, res) {

	// login necessary module
	var userId = checkAccess(req);
	if (userId == 0) {
		res.send(401);
		return;
	}

	if (req.body.menuId && req.body.text) {

		db.setFeedback(userId, req.body.menuId, req.body.text, function(cb) {
			sendToClient(res, cb);
		});
	}

});

// JSON RECEIVER

// tmp printer
app.post(JSON_IN, function(req, res) {
	if (!req.is('json')) {
		console.log('Content-Type is not application/json');
		res.send('Content-Type is not application/json');
	} else {
		var data = req.body;
		console.log(data);
		res.send("Data Received");
	}
});

// MISC

sendToClient = function(con, res) {
	con.header('application/json');
	con.send(res);
};

checkAccess = function(req) {

	if (!req.cookies.userId) {
		console.log('Anonymos Access');
		return 0;
	} else {
		console.log('User with ID' + req.cookies.userId);
		return req.cookies.userId;
	}

};

help = function(cb) {

	var help = {};

	help.functions = {

		// GET
		"getMenuSubitem" : MENU_SUBITEMS_BYID,
		"getMainMenu" : MAINMENU,
		"getAllRatingMeasures" : ALL_RATINGS,
		"getRatingScore" : RATING_SCORES,
		"getAllSystemLanguages" : ALL_SYSLANGUAGES,
		"getFile" : GET_FILE,
		"getAllCountries" : ALL_COUNTRIES,
		"logout" : LOGOUT,

		// POST
		"login(user, pass), Logout only" : LOGIN,
		"register(user, pass, fname, lname, email, cntId, lngId), Logout only" : REGISTER,
		"insertFeedback(menuId, text), Login only" : INSERT_FEEDBACK,
		"insertRating(mrAscId, scoreId), Login only" : INSERT_RATING

	};

	db.getAllItemTypes(function(types) {
		help.elementTypes = types;
		cb(help);
	});
};

// TEST and TEMP METHODS

// app.get('/pg/:id', function(req, res) {
// db.getPage(req.params.id, function(results) {
// sendToClient(res, results);
// });
//
// });

 //login form, tmp
 app.get(LOGIN, function(req, res) {

 if (req.cookies.userId) {
 res.send('Hello ' + req.cookies.userId
 + '. Click to <a href="/logout">logout</a>!.');
 } else {
 res.send('<form method="post"><p>'
 + 'user: <input type="text" name="user" value="igeo"/> '
 + 'pass: <input type="text" name="pass" value="aaa"/> '
 + '<input type="submit" value="Submit"/>.</p></form>');
 }

 });

// // register form, tmp
// app.get(REGISTER, function(req, res) {
//
// if (req.cookies.userId) {
// res.send('Logout for new registration! ' + ''
// + '. Click to <a href="/logout">logout</a>!.');
// } else {
// res.send('<form method="post"><p>'
// + 'user: <input type="text" name="user" value="igeo2"/> '
// + 'pass: <input type="text" name="pass" value="aaa"/> '
// + 'pass: <input type="text" name="fname" value="ivan"/> '
// + 'pass: <input type="text" name="lname" value="georgiev"/> '
// + 'pass: <input type="text" name="cntId" value="1"/> '
// + 'pass: <input type="text" name="lngId" value="1"/> '
// + '<input type="submit" value="Submit"/>.</p></form>');
// }
//
// });
//
// app.get(INSERT_RATING, function(req, res) {
//
// res.send('<form method="post"><p>'
// + 'mrAscId: <input type="text" name="mrAscId" value="5"/> '
// + 'score: <input type="text" name="scoreId" value="5"/> '
// + '<input type="submit" value="Submit"/>.</p></form>');
//
// });
