//constant types
var LANGUAGE = 'LANGUAGE';
var MENU = 'MENU';
var PAGE = 'PAGE';
var RATING = 'RATING_MEASURE';
var COUNTRY = 'COUNTRY';
var RATING_SCORE = 'RATING_SCORE';

// Error codes
var HTTP_NOT_FOUND = 404;
var HTTP_SERVER_ERROR = 500;

// MAIN BEGIN
var mysql = require('mysql');

var connection = mysql.createConnection({
	host : 'localhost',
	user : 'root',
	password : '',
	database : 'isis'
});
// MAIN END

// EXPORT INIT
exports.init = function(cb) {
	connection.connect(function(err) {
		if (!err) {
			console.log("Connected to MySQL");
			cb(true);
		} else if (err) {
			console.log(err);
			cb(false);
		}
	});
};

// FUNCTIONS
// private method to encapsulate error
execStatement = function(stm, params, cb) {

	// console.log(stm, params);

	connection.query(stm, params, function(err, results) {

		if (err) {
			console.log(err);
			// return HTTP_SERVER_ERROR
			results = HTTP_SERVER_ERROR;
		}
		if (cb) {
			cb(results);
		}
	});

};

// private methods
getPage = function(menuId, cb) {

	execStatement(
			'select ? as TYPE, ID, MENU_ID, PAGE_TEXT from pages where MENU_ID= ?',
			[ PAGE, menuId ],
			function(res) {

				// if no such page return HTTP_NOT_FOUND
				if (res.length == 0) {
					cb(HTTP_NOT_FOUND);
					return;
				}

				var pageId = res[0].ID;

				execStatement(
						'select PAGE_ID, LANGUAGE_ID, PAGE_TEXT from `transl_pages` '
								+ 'where PAGE_ID =? ',
						[ pageId ],
						function(translations) {

							execStatement(
									'select PAGE_ID, ID, EXTENSION, SIZE_IN_KB from `files` '
											+ 'where PAGE_ID =? ',
									[ pageId ],
									function(fls) {

										for (var i = 0; i < res.length; i++) {

											res[i].translations = [];
											res[i].files = [];

											for (var j = 0; j < translations.length; j++) {

												// if (val[j].PAGE_ID ==
												// res[i].ID) {

												delete translations[j].PAGE_ID;
												res[i].translations
														.push(translations[j]);
												// }
											}

											for (var j = 0; j < fls.length; j++) {

												// if (fls[j].PAGE_ID ==
												// res[i].ID) {

												delete fls[j].PAGE_ID;
												res[i].files.push(fls[j]);
												// }
											}
										}

										cb(res);

									});
						});
			});
};

// EXPORT STATEMENTS

// GET
exports.getMenuSubitems = function(id, cb) {

	execStatement(
			'select ? as TYPE, t1.ID as ID, t1.TITLE as TITLE from menus as t1 '
					+ 'inner join `menu_tree_relations` as t2 on t1.ID=t2.`MENU_ID` '
					+ 'where t2.`PARENT_MENU_ID`= ? ' + 'order by t1.ID',
			[ MENU, id ],
			function(res) {

				if (res.length > 0) {
					// the menu has submenus, get items
					execStatement(
							'select MENU_ID, LANGUAGE_ID, TITLE from `transl_menus` '
									+ 'where MENU_ID in (select menu_id from `menu_tree_relations` where parent_menu_id =?) '
									+ 'order by MENU_ID',
							[ id ],
							function(val) {

								execStatement(
										'select t2.MENU_ID as MENU_ID,t1.ID as ID, t2.ID as ASOC_ID from `rating_measures` as t1             '
												+ 'inner join `menu_rating_associations`  as t2 on t1.ID=t2.`RATING_MEASURE_ID` '
												+ 'inner join `menu_tree_relations` as t3 on t2.MENU_ID=t3.`MENU_ID`            '
												+ 'where t3.`PARENT_MENU_ID`=? order by MENU_ID                                                ',
										[ id ],
										function(rtn) {

											var j = 0;
											for (var i = 0; i < res.length; i++) {

												res[i].translations = [];

												for (var j = 0; j < val.length; j++) {

													if (val[j].MENU_ID == res[i].ID) {

														delete val[j].MENU_ID;
														res[i].translations
																.push(val[j]);
														// console.log(res[i]);
													}
												}

												res[i].ratings = [];

												for (var j = 0; j < rtn.length; j++) {

													if (rtn[j].MENU_ID == res[i].ID) {

														delete rtn[j].MENU_ID;
														res[i].ratings
																.push(rtn[j]);
														// console.log(res[i]);
													}
												}

											}

											cb(res);

										});

							});

				} else {
					// this is leaf menu, get the page
					getPage(id, cb);
				}
			});
};

exports.getRatings = function(cb) {

	execStatement(
			'select ? as TYPE, ID, NAME from rating_measures order by ID',
			[ RATING ], function(res) {

				execStatement(
						'select RATING_MEASURE_ID, LANGUAGE_ID, NAME from `transl_rating_measures` '
								+ 'order by RATING_MEASURE_ID', [], function(
								val) {

							for (var i = 0; i < res.length; i++) {

								res[i].translations = [];

								for (var j = 0; j < val.length; j++) {

									if (val[j].RATING_MEASURE_ID == res[i].ID) {

										delete val[j].RATING_MEASURE_ID;
										res[i].translations.push(val[j]);
										// console.log(res[i]);
									}
								}

							}

							cb(res);

						});
			});
};

exports.getRatingScores = function(asocId, userId, cb) {

	execStatement(
			'select ? as TYPE, MENU_RATING_ASSOC_ID, AVG(SCORE_ID) as SCORE, count(1) as VOTES_COUNT, '
					+ '(select MAX(SCORE_ID) from rating_records where USER_ID = ?  AND MENU_RATING_ASSOC_ID = ? ) as USER_VOTE '
					+ ' from rating_records where MENU_RATING_ASSOC_ID = ? ', [
					RATING_SCORE, userId, asocId, asocId ], cb);
};

exports.getSystemLanguages = function(cb) {

	execStatement(
			'select ? as TYPE, ID, NAME, ISPRIMARY from system_languages order by ISPRIMARY desc, ID',
			[ LANGUAGE ], cb);
};

exports.getFileInfo = function(id, cb) {

	execStatement('select PATH, EXTENSION from files where ID = ?', [ id ], cb);

};

exports.getValidUserId = function(user, pass, cb) {

	execStatement('select * from passwords as t1 '
			+ 'inner join users as t2 on t1.USER_ID = t2.ID '
			+ 'where t2.USERNAME = ?', [ user ], function(res) {

		if (res.length == 0) {
			cb(0);
			return;
		}

		var userId = res[0].USER_ID;
		var hash = res[0].HASH;

		cb({
			'USER_ID' : userId,
			'HASH' : hash
		});
	});

};

exports.getAllCountries = function(cb) {

	execStatement('select ? as TYPE, ID, NAME from countries', [ COUNTRY ],
			function(res) {

				execStatement(
						'select COUNTRY_ID, LANGUAGE_ID, NAME from `transl_countries` '
								+ 'order by COUNTRY_ID', [], function(val) {

							for (var i = 0; i < res.length; i++) {

								res[i].translations = [];

								for (var j = 0; j < val.length; j++) {

									if (val[j].COUNTRY_ID == res[i].ID) {

										delete val[j].COUNTRY_ID;
										res[i].translations.push(val[j]);
										// console.log(res[i]);
									}
									;
								}
								;

							}

							cb(res);

						});
			});

};

exports.getAllItemTypes = function(cb) {

	var elements = {
		1 : MENU,
		2 : PAGE,
		3 : RATING,
		4 : COUNTRY,
		5 : RATING_SCORE,
		6 : LANGUAGE
	};

	cb(elements);

};

exports.setRating = function(userId, menuRtAsocId, scoreId, cb) {

	execStatement(
			'delete from rating_records where USER_ID = ? and MENU_RATING_ASSOC_ID = ? ',
			[ userId, menuRtAsocId ], function(res) {

				execStatement(
						'insert into rating_records ( USER_ID, MENU_RATING_ASSOC_ID, SCORE_ID ) '
								+ ' values ( ? , ? , ? ) ', [ userId,
								menuRtAsocId, scoreId ], cb);

			});

};

exports.setFeedback = function(userId, menuId, text, cb) {

	execStatement('delete from feedbacks where USER_ID = ? and MENU_ID = ? ', [
			userId, menuId ], function(res) {

		execStatement('insert into feedbacks ( USER_ID, MENU_ID, FB_TEXT ) '
				+ ' values ( ? , ? , ? ) ', [ userId, menuId, text ], cb);

	});

};

// Include email
exports.insertUser = function(username, hash, fname, lname, email, cntId,
		lngId, cb) {

	execStatement(
			'insert into users ( USERNAME ,FIRST_NAME, LAST_NAME, EMAIL, COUNTRY_ID, PREF_LNG_ID ) '
					+ ' values ( ? , ? , ?, ?, ? , ?) ', [ username, fname,
					lname, email, cntId, lngId, cb ], function(res) {

				if (res == HTTP_SERVER_ERROR) {
					cb(HTTP_SERVER_ERROR);
					return;
				}

				// console.log(res);

				var userId = res.insertId;

				execStatement('insert into passwords ( USER_ID , HASH ) '
						+ ' values ( ? , ? ) ', [ userId, hash ], cb);

			});

};

// TEST METHODS

