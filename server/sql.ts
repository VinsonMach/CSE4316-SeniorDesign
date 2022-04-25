// Server for Cerberus

var admin = require('firebase-admin');
var bodyParser = require('body-parser');
var express = require('express');
var mysql = require('mysql');

import fetch from 'node-fetch'
const { promisify } = require('util');
import { RingApi } from 'ring-client-api' 
import { skip } from 'rxjs/operators'

import { haveCode, ringLogin } from './ring/refresh-token'
import { requestInput } from './ring/util';
import { RingRestClient } from './ring/rest-client'

var connection = mysql.createConnection({
	user: 'TestUser',
	password: 'B0h2awHbR8VRjhzN',
	host: 'cerberus-db-do-user-10302871-0.b.db.ondigitalocean.com',
	database: 'defaultdb',
	port: 25060
});
const q = promisify(connection.query).bind(connection);

var serviceAccount = require("./cerberus-fc82a-firebase-adminsdk-l9834-441f823aa1.json");
admin.initializeApp({
	credential: admin.credential.cert(serviceAccount)
});

var app = express();
app.use(bodyParser.json());
app.use(bodyParser.urlencoded({extended: true}));
app.listen(3000, function () {
	console.log('Server is running...');
});

app.get('/test', function(req, res) {
	console.log('connected');
	res.json({
		'resultCode': 200,
		'message': 'OK'
	})
});

/*****************
 *     /ring     *
 *****************/
app.post("/ring/login", (req, res) => {
	var email = req.body.userEmail;
	var password = req.body.userPassword;
	var resultCode = 404;
	var message = 'Error';
	ringLogin(email, password)
	.then(client => {
		//console.log(client);
		client.getAuth();
		res.json({
			'resultCode': 200,
			'message': 'Success'
		})
	})
	.catch(error => {
		console.log(error);
		res.json({
			'resultCode': resultCode,
			'message': message
		})
	});
});

app.post("/ring/auth", (req, res) => {	
	// add to android app
	var email = req.body.email;
	var password = req.body.password;
	
	var auth = req.body.authCode;
	var userId = req.body.userId;
	
	console.log(email);
	console.log(password);
	console.log(auth);
	console.log(userId);
	
	var resultCode = 404;
	var message = 'Error!';

	// TO DO: Future - be able to just pass in auth code and not email, pass again
	haveCode(email, password, auth)
	.then(token => {
		console.log(token);
		var sql = 'UPDATE Users SET RefreshToken = ?, RingEmail = ? WHERE UserID = ?;';
		var params = [token, email, userId]
		connection.query(sql, params, function (err, result) {
			if (err) {
				console.log(err);
			} else {
				resultCode = 200;
				message = 'Success!';
			}
			res.json({
				'resultCode': resultCode,
				'message': message
			})
		});
	})
	.catch(error => {
		console.log(error);
		res.json({
			'resultCode': resultCode,
			'message': message
		})
	});
});

app.get('/ring/event', function(req, res) {
	var sql = 'SELECT * FROM defaultdb.Event_history';
	connection.query(sql, function (err, result) {
		var resultCode = 404;
		var message = 'Error!';
		if (err) {
			console.log(err);
		} else {
			resultCode = 200;
			message = 'Success!';
			var results_parsed = JSON.parse(JSON.stringify(result));
			console.log(results_parsed);
			return results_parsed;
		}
	});

});

app.post('/ring/notify', function(req, res) {
	var id = req.body.userId;
	console.log('/ring/notify');
	getRingEvents(id);
	
	res.json({
		'resultCode': 200,
		'message': 'OK'
	})
});

/*****************
 *     /notif    *
 *****************/
 app.post('/notif', function(req, res) {
	console.log('notif');
	var userId = req.body.userId
	var title = req.body.title
	var body = req.body.body
	
	console.log(`${userId} ${title} ${body}`);
	
	var sql = 'SELECT NotifToken FROM Users WHERE UserId = ?';
	var params = [userId];
	var resultCode = 404;
	var message = 'Error';
	connection.query(sql, params, function (err, result) {
		if (err) {
			console.log(err);
			res.json({
				'resultCode': resultCode,
				'message': message
			})
		} else {
			if (result.length > 0) {
				const token = result[0].NotifToken;
				console.log(result);
				resultCode = 200;
				message = 'Success';
				
				const notification_options = {
					prioriy: 'high'
				}
			
				const payload = {
					notification: {
						title: title,
						body: body
					}
				}
				console.log(token);
				admin.messaging().sendToDevice(token, payload, notification_options)
				.then(response => {
					res.json({
						'resultCode': resultCode,
						'message': message
					})
					console.log(response);
				})
			} else {
				res.json({
					'resultCode': resultCode,
					'message': message
				})
			}
		}
	});
});
 
app.post('/notif/token', function(req, res) {
	console.log('notification token updated');
	var userId = req.body.userId;
	var token = req.body.token;
	console.log(userId);
	console.log(token);
	var sql = 'UPDATE Users SET NotifToken = ? WHERE UserID = ?';
	var params = [token, userId];
	
	var resultCode = 404;
	var message = 'Error';
	connection.query(sql, params, function (err, result) {
		if (err) {
			console.log(err);
		} else {
			resultCode = 200;
			message = 'Success';
		}
		res.json({
			'resultCode': resultCode,
			'message': message
		})
	});
});

/*****************
 *     /user     *
 *****************/
 app.post('/user/join', function (req, res) {
	console.log(req.body);
	var userEmail = req.body.userEmail;
	var userPwd = req.body.userPwd;
	var userName = req.body.userName;

	var sql = 'INSERT INTO Users (UserEmail, UserPwd, UserName) VALUES (?, ?, ?)';
	var params = [userEmail, userPwd, userName];

	connection.query(sql, params, function (err, result) {
		var resultCode = 404;
		var message = 'Error!';

		if (err) {
			console.log(err);
		} else {
			resultCode = 200;
			message = 'Success!';
		}

		res.json({
			'code': resultCode,
			'message': message
		});
	});
});

app.post('/user/login', function(req, res){
	var userEmail = req.body.userEmail;
	var userPwd = req.body.userPwd;
	var sql = "SELECT * FROM Users WHERE userEmail = ?";

	connection.query(sql, userEmail, function (err, result) {
		var resultCode = 404;
		var message = 'Error!';
		var ringLogin = false;
		//var hashedPwd = result[0].UserPwd;

		if (err) {
			console.log(err);
		} else {
			if (result.length === 0) {
				resultCode = 204;
				message = 'ID not found!';
			} else if (userPwd !== result[0].UserPwd) {
				resultCode = 204;
				message = 'Wrong password!';
			} else {
				resultCode = 200;
				message = 'Login success. ' + result[0].UserName + ' welcome!';
				if (result[0].RefreshToken != null) {
					ringLogin = true;
				}
			}
		}

		var id = -1;
		if (resultCode != 204) {
			id = result[0].UserID; 
		}

		res.json({
			'code': resultCode,
			'userId': id,
			'ringLogin': ringLogin,
			'message': message,
			//'hashedPwd': hashedPwd
		});
	})
});

/*****************
 *     funcs     *
 *****************/
async function getRefreshToken(id) {
	console.log('getRefreshToken');
	var refreshToken = null;
	var sql = 'SELECT RefreshToken FROM Users WHERE UserId = ?';
	var params = [id]
	
	
	var results = await q(sql, params);
	refreshToken = results[0].RefreshToken;
	console.log('refreshToken');
	return refreshToken;
}

function updateRefreshToken(id, token) {
	console.log('updateRefreshToken')
	var sql = 'UPDATE Users SET RefreshToken = ? WHERE UserId = ?';
	var params = [token, id]
	connection.query(sql, params, function (err, result) {
		if (err) {
			console.log(err);
			return null
		} else {
			return true;
		}
	});
}

var nu = 0;
async function getRingEvents(id) {
	console.log('getRingEvents');
	const { env } = process,
    ringApi = await new RingApi({
		// Replace with your refresh token
		refreshToken: await getRefreshToken(id),
		// Listen for dings and motion events
		cameraDingsPollingSeconds: 5,
		debug: true,
    }),
    locations = await ringApi.getLocations(),
    allCameras = await ringApi.getCameras()

	console.log(
		`Found ${locations.length} location(s) with ${allCameras.length} camera(s).`
	)

	ringApi.onRefreshTokenUpdated.subscribe(
		async ({ newRefreshToken, oldRefreshToken }) => {
			if (!oldRefreshToken) {
				return
			}
			updateRefreshToken(id, newRefreshToken);
		}
	)

	for (const location of locations) {
		location.onConnected.pipe(skip(1)).subscribe((connected) => {
			const status = connected ? 'Connected to' : 'Disconnected from'
			console.log(`**** ${status} location ${location.name} - ${location.id}`)
		})
	}

	console.log('here');
	
	for (const location of locations) {
		const cameras = location.cameras,
			devices = await location.getDevices()
		console.log(`Devices: ${devices.length}`);
    
		for (const camera of cameras) {
			console.log(`\n- ${camera.id}: ${camera.name} (${camera.deviceType})\n\n`)
		}
    
		console.log(
		`\nLocation ${location.name} (${location.id}) has the following ${devices.length} device(s):`
		)

		for (const device of devices) {
			console.log(`- ${device.id}: ${device.name} (${device.deviceType})`)
		}
    
		if (allCameras.length) {
			allCameras.forEach((camera) => {
				camera.onNewDing.subscribe((ding) => {
					console.log(`\n\n${nu} ${camera.id}: ${camera.name} (${camera.deviceType})\n`);
					//console.log(`Camera: ${JSON.stringify(camera)}`);
					console.log(`ding: ${JSON.stringify(ding)}`);
					nu += 1;
					
					// Get information for database
					var epoch = ding["now"] * 1000;
					var date = new Date(epoch);
					
					var strYear = String(date.getFullYear());
					var strMonth = String(date.getMonth() + 1);
					if (strMonth.length < 2) {strMonth = '0' + strMonth;}
					var strDay = String(date.getDate());
					if (strDay.length < 2) {strDay = '0' + strDay;}
					var strDate = strYear + '-' + strMonth + '-' + strDay;
					
					var strHours = String(date.getHours());
					if (strHours.length < 2) {strHours = '0' + strHours;}
					var strMinutes = String(date.getMinutes());
					if (strMinutes.length < 2) {strMinutes = '0' + strMinutes;}
					var strSeconds = String(date.getSeconds());
					if (strSeconds.length < 2) {strSeconds = '0' + strSeconds;}
					var strTime = strHours + ':' + strMinutes + ':' + strSeconds;
					
					var sql = 'INSERT INTO Event_history(SensorID, SensorName, EventType, EventDate, EventTime, UserID) VALUES(?, ?, ?, Date(?), Time(?), ?)'
					var params = [camera.id, camera.name, camera.deviceType, strDate, strTime, id]
					
					// Send information to database
					connection.query(sql, params, function (err, result) {
						if (err) {
							console.log(err);
							return null
						} else {
							return true;
						}
					});
					
					// Get information for notification
					var deviceName = camera.name;
					var title = deviceName + ' Ding';
					var body = 'Check Cerberus App for more details';
					
					// Send notification
					console.log('notifying')
					fetch('http://localhost:3000/notif', {
						method: 'POST',
						body: JSON.stringify({
							userId: id,
							title: title,
							body: body
						}),
						headers: {
							'Content-Type': 'application/json'
						}
					});
				})
			})
		}
    
		if (devices.length) {
			devices.forEach((device) => {
				device.onData.subscribe((data) => {
					//console.log(`\n\n${nu} ${device.zid}: ${device.name} (${device.deviceType})\n`);
					//console.log(`Data: ${JSON.stringify(data)}`);
					//nu += 1;
					
					// Only notify for certain events
					if ('faulted' in data && data.faulted) {
						console.log(`\n\n${nu} ${device.zid}: ${device.name} (${device.deviceType})\n`);
						console.log(`Data: ${JSON.stringify(data)}`);
						nu += 1;
						
						// Get information for database & send
						var epoch = data["lastUpdate"];
						var date = new Date(epoch);
						
						
						var strYear = String(date.getFullYear());
						var strMonth = String(date.getMonth() + 1);
						if (strMonth.length < 2) {strMonth = '0' + strMonth;}
						var strDay = String(date.getDate());
						if (strDay.length < 2) {strDay = '0' + strDay;}
						var strDate = strYear + '-' + strMonth + '-' + strDay;
						
						var strHours = String(date.getHours());
						if (strHours.length < 2) {strHours = '0' + strHours;}
						var strMinutes = String(date.getMinutes());
						if (strMinutes.length < 2) {strMinutes = '0' + strMinutes;}
						var strSeconds = String(date.getSeconds());
						if (strSeconds.length < 2) {strSeconds = '0' + strSeconds;}
						var strTime = strHours + ':' + strMinutes + ':' + strSeconds;
						
						var sql = 'INSERT INTO Event_history(SensorID, SensorName, EventType, EventDate, EventTime, UserID) VALUES(?, ?, ?, ?, ?, ?)'
						var params = [device.zid, device.name, device.deviceType, strDate, strTime, id]
						
						// Send information to database
						connection.query(sql, params, function (err, result) {
							if (err) {
								console.log(err);
								return null
							} else {
								return true;
							}
						});
						
						// Get information for notification
						var deviceName = device.name;
						var title = deviceName + ' Alert';
						var body = 'Check Cerberus App for more details';
						
						// Send notification
						console.log('notifying')
						fetch('http://localhost:3000/notif', {
							method: 'POST',
							body: JSON.stringify({
								userId: id,
								title: title,
								body: body
							}),
							headers: {
								'Content-Type': 'application/json'
							}
						});
					}
				})
			})
		}
	}
}

