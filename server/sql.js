"use strict";
// Server for Cerberus
var __awaiter = (this && this.__awaiter) || function (thisArg, _arguments, P, generator) {
    function adopt(value) { return value instanceof P ? value : new P(function (resolve) { resolve(value); }); }
    return new (P || (P = Promise))(function (resolve, reject) {
        function fulfilled(value) { try { step(generator.next(value)); } catch (e) { reject(e); } }
        function rejected(value) { try { step(generator["throw"](value)); } catch (e) { reject(e); } }
        function step(result) { result.done ? resolve(result.value) : adopt(result.value).then(fulfilled, rejected); }
        step((generator = generator.apply(thisArg, _arguments || [])).next());
    });
};
var __generator = (this && this.__generator) || function (thisArg, body) {
    var _ = { label: 0, sent: function() { if (t[0] & 1) throw t[1]; return t[1]; }, trys: [], ops: [] }, f, y, t, g;
    return g = { next: verb(0), "throw": verb(1), "return": verb(2) }, typeof Symbol === "function" && (g[Symbol.iterator] = function() { return this; }), g;
    function verb(n) { return function (v) { return step([n, v]); }; }
    function step(op) {
        if (f) throw new TypeError("Generator is already executing.");
        while (_) try {
            if (f = 1, y && (t = op[0] & 2 ? y["return"] : op[0] ? y["throw"] || ((t = y["return"]) && t.call(y), 0) : y.next) && !(t = t.call(y, op[1])).done) return t;
            if (y = 0, t) op = [op[0] & 2, t.value];
            switch (op[0]) {
                case 0: case 1: t = op; break;
                case 4: _.label++; return { value: op[1], done: false };
                case 5: _.label++; y = op[1]; op = [0]; continue;
                case 7: op = _.ops.pop(); _.trys.pop(); continue;
                default:
                    if (!(t = _.trys, t = t.length > 0 && t[t.length - 1]) && (op[0] === 6 || op[0] === 2)) { _ = 0; continue; }
                    if (op[0] === 3 && (!t || (op[1] > t[0] && op[1] < t[3]))) { _.label = op[1]; break; }
                    if (op[0] === 6 && _.label < t[1]) { _.label = t[1]; t = op; break; }
                    if (t && _.label < t[2]) { _.label = t[2]; _.ops.push(op); break; }
                    if (t[2]) _.ops.pop();
                    _.trys.pop(); continue;
            }
            op = body.call(thisArg, _);
        } catch (e) { op = [6, e]; y = 0; } finally { f = t = 0; }
        if (op[0] & 5) throw op[1]; return { value: op[0] ? op[1] : void 0, done: true };
    }
};
Object.defineProperty(exports, "__esModule", { value: true });
var admin = require('firebase-admin');
var bodyParser = require('body-parser');
var express = require('express');
var mysql = require('mysql');
var node_fetch_1 = require("node-fetch");
var promisify = require('util').promisify;
var ring_client_api_1 = require("ring-client-api");
var operators_1 = require("rxjs/operators");
var refresh_token_1 = require("./ring/refresh-token");
var connection = mysql.createConnection({
    user: 'TestUser',
    password: 'B0h2awHbR8VRjhzN',
    host: 'cerberus-db-do-user-10302871-0.b.db.ondigitalocean.com',
    database: 'defaultdb',
    port: 25060
});
var q = promisify(connection.query).bind(connection);
var serviceAccount = require("./cerberus-fc82a-firebase-adminsdk-l9834-441f823aa1.json");
admin.initializeApp({
    credential: admin.credential.cert(serviceAccount)
});
var app = express();
app.use(bodyParser.json());
app.use(bodyParser.urlencoded({ extended: true }));
app.listen(3000, function () {
    console.log('Server is running...');
});
app.get('/test', function (req, res) {
    console.log('connected');
    res.json({
        'resultCode': 200,
        'message': 'OK'
    });
});
/*****************
 *     /ring     *
 *****************/
app.post("/ring/login", function (req, res) {
    var email = req.body.userEmail;
    var password = req.body.userPassword;
    var resultCode = 404;
    var message = 'Error';
    (0, refresh_token_1.ringLogin)(email, password)
        .then(function (client) {
        //console.log(client);
        client.getAuth();
        res.json({
            'resultCode': 200,
            'message': 'Success'
        });
    })
        .catch(function (error) {
        console.log(error);
        res.json({
            'resultCode': resultCode,
            'message': message
        });
    });
});
app.post("/ring/auth", function (req, res) {
    // add to android app
    var email = req.body.email;
    var password = req.body.password;
    var auth = req.body.authCode;
    var userId = req.body.userId;
    var resultCode = 404;
    var message = 'Error!';
    // TO DO: Future - be able to just pass in auth code and not email, pass again
    (0, refresh_token_1.haveCode)(email, password, auth)
        .then(function (token) {
        var sql = 'UPDATE Users SET RefreshToken = ?, RingEmail = ? WHERE UserID = ?;';
        var params = [token, email, userId];
        connection.query(sql, params, function (err, result) {
            if (err) {
                console.log(err);
            }
            else {
                resultCode = 200;
                message = 'Success!';
            }
            res.json({
                'resultCode': resultCode,
                'message': message
            });
        });
    })
        .catch(function (error) {
        console.log(error);
        res.json({
            'resultCode': resultCode,
            'message': message
        });
    });
});
app.get('/ring/event', function (req, res) {
    var sql = 'SELECT * FROM defaultdb.Event_history';
    connection.query(sql, function (err, result) {
        var resultCode = 404;
        var message = 'Error!';
        if (err) {
            console.log(err);
        }
        else {
            resultCode = 200;
            message = 'Success!';
            var results_parsed = JSON.parse(JSON.stringify(result));
            console.log(results_parsed);
            return results_parsed;
        }
    });
});
app.post('/ring/notify', function (req, res) {
    var id = req.body.userId;
    console.log('/ring/notify');
    //var id = 25;
    getRingEvents(id);
    res.json({
        'resultCode': 200,
        'message': 'OK'
    });
});
/*****************
 *     /notif    *
 *****************/
app.post('/notif', function (req, res) {
    console.log('notif');
    var userId = req.body.userId;
    var title = req.body.title;
    var body = req.body.body;
    console.log("".concat(userId, " ").concat(title, " ").concat(body));
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
            });
        }
        else {
            if (result.length > 0) {
                var token = result[0].NotifToken;
                console.log(result);
                resultCode = 200;
                message = 'Success';
                var notification_options = {
                    prioriy: 'high'
                };
                var payload = {
                    notification: {
                        title: title,
                        body: body
                    }
                };
                console.log(token);
                admin.messaging().sendToDevice(token, payload, notification_options)
                    .then(function (response) {
                    res.json({
                        'resultCode': resultCode,
                        'message': message
                    });
                    console.log(response);
                });
            }
            else {
                res.json({
                    'resultCode': resultCode,
                    'message': message
                });
            }
        }
    });
});
app.post('/notif/token', function (req, res) {
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
        }
        else {
            resultCode = 200;
            message = 'Success';
        }
        res.json({
            'resultCode': resultCode,
            'message': message
        });
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
        }
        else {
            resultCode = 200;
            message = 'Success!';
        }
        res.json({
            'code': resultCode,
            'message': message
        });
    });
});
app.post('/user/login', function (req, res) {
    var userEmail = req.body.userEmail;
    var userPwd = req.body.userPwd;
    var sql = "SELECT * FROM Users WHERE userEmail = ?";
    connection.query(sql, userEmail, function (err, result) {
        var resultCode = 404;
        var message = 'Error!';
        var ringLogin = false;
        if (err) {
            console.log(err);
        }
        else {
            if (result.length === 0) {
                resultCode = 204;
                message = 'ID not found!';
            }
            else if (userPwd !== result[0].UserPwd) {
                resultCode = 204;
                message = 'Wrong password!';
            }
            else {
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
            'message': message
        });
    });
});
/*****************
 *     funcs     *
 *****************/
function getRefreshToken(id) {
    return __awaiter(this, void 0, void 0, function () {
        var refreshToken, sql, params, results;
        return __generator(this, function (_a) {
            switch (_a.label) {
                case 0:
                    console.log('getRefreshToken');
                    refreshToken = null;
                    sql = 'SELECT RefreshToken FROM Users WHERE UserId = ?';
                    params = [id];
                    return [4 /*yield*/, q(sql, params)];
                case 1:
                    results = _a.sent();
                    refreshToken = results[0].RefreshToken;
                    return [2 /*return*/, refreshToken];
            }
        });
    });
}
function updateRefreshToken(id, token) {
    console.log('updateRefreshToken');
    var sql = 'UPDATE Users SET RefreshToken = ? WHERE UserId = ?';
    var params = [token, id];
    connection.query(sql, params, function (err, result) {
        if (err) {
            console.log(err);
            return null;
        }
        else {
            return true;
        }
    });
}
var nu = 0;
function getRingEvents(id) {
    return __awaiter(this, void 0, void 0, function () {
        var env, ringApi, locations, allCameras, _a, _loop_1, _i, locations_1, location_1, _b, locations_2, location_2, cameras, devices, _c, cameras_1, camera, _d, devices_1, device;
        var _e;
        var _this = this;
        return __generator(this, function (_f) {
            switch (_f.label) {
                case 0:
                    console.log('getRingEvents');
                    env = process.env;
                    _a = ring_client_api_1.RingApi.bind;
                    _e = {};
                    return [4 /*yield*/, getRefreshToken(id)];
                case 1: return [4 /*yield*/, new (_a.apply(ring_client_api_1.RingApi, [void 0, (
                        // Replace with your refresh token
                        _e.refreshToken = _f.sent(),
                            // Listen for dings and motion events
                            _e.cameraDingsPollingSeconds = 5,
                            _e.debug = true,
                            _e)]))()];
                case 2:
                    ringApi = _f.sent();
                    return [4 /*yield*/, ringApi.getLocations()];
                case 3:
                    locations = _f.sent();
                    return [4 /*yield*/, ringApi.getCameras()];
                case 4:
                    allCameras = _f.sent();
                    console.log("Found ".concat(locations.length, " location(s) with ").concat(allCameras.length, " camera(s)."));
                    ringApi.onRefreshTokenUpdated.subscribe(function (_a) {
                        var newRefreshToken = _a.newRefreshToken, oldRefreshToken = _a.oldRefreshToken;
                        return __awaiter(_this, void 0, void 0, function () {
                            return __generator(this, function (_b) {
                                if (!oldRefreshToken) {
                                    return [2 /*return*/];
                                }
                                updateRefreshToken(id, newRefreshToken);
                                return [2 /*return*/];
                            });
                        });
                    });
                    _loop_1 = function (location_1) {
                        location_1.onConnected.pipe((0, operators_1.skip)(1)).subscribe(function (connected) {
                            var status = connected ? 'Connected to' : 'Disconnected from';
                            console.log("**** ".concat(status, " location ").concat(location_1.name, " - ").concat(location_1.id));
                        });
                    };
                    for (_i = 0, locations_1 = locations; _i < locations_1.length; _i++) {
                        location_1 = locations_1[_i];
                        _loop_1(location_1);
                    }
                    console.log('here');
                    _b = 0, locations_2 = locations;
                    _f.label = 5;
                case 5:
                    if (!(_b < locations_2.length)) return [3 /*break*/, 8];
                    location_2 = locations_2[_b];
                    cameras = location_2.cameras;
                    return [4 /*yield*/, location_2.getDevices()];
                case 6:
                    devices = _f.sent();
                    console.log("Devices: ".concat(devices.length));
                    for (_c = 0, cameras_1 = cameras; _c < cameras_1.length; _c++) {
                        camera = cameras_1[_c];
                        console.log("\n- ".concat(camera.id, ": ").concat(camera.name, " (").concat(camera.deviceType, ")\n\n"));
                    }
                    console.log("\nLocation ".concat(location_2.name, " (").concat(location_2.id, ") has the following ").concat(devices.length, " device(s):"));
                    for (_d = 0, devices_1 = devices; _d < devices_1.length; _d++) {
                        device = devices_1[_d];
                        console.log("- ".concat(device.id, ": ").concat(device.name, " (").concat(device.deviceType, ")"));
                    }
                    if (allCameras.length) {
                        allCameras.forEach(function (camera) {
                            camera.onNewDing.subscribe(function (ding) {
                                console.log("\n\n".concat(nu, " ").concat(camera.id, ": ").concat(camera.name, " (").concat(camera.deviceType, ")\n"));
                                //console.log(`Camera: ${JSON.stringify(camera)}`);
                                console.log("ding: ".concat(JSON.stringify(ding)));
                                nu += 1;
                                // Get information for database
                                var epoch = ding["now"] * 1000;
                                var date = new Date(epoch);
                                var strYear = String(date.getFullYear());
                                var strMonth = String(date.getMonth() + 1);
                                if (strMonth.length < 2) {
                                    strMonth = '0' + strMonth;
                                }
                                var strDay = String(date.getDate());
                                if (strDay.length < 2) {
                                    strDay = '0' + strDay;
                                }
                                var strDate = strYear + '-' + strMonth + '-' + strDay;
                                var strHours = String(date.getHours());
                                if (strHours.length < 2) {
                                    strHours = '0' + strHours;
                                }
                                var strMinutes = String(date.getMinutes());
                                if (strMinutes.length < 2) {
                                    strMinutes = '0' + strMinutes;
                                }
                                var strSeconds = String(date.getSeconds());
                                if (strSeconds.length < 2) {
                                    strSeconds = '0' + strSeconds;
                                }
                                var strTime = strHours + ':' + strMinutes + ':' + strSeconds;
                                var sql = 'INSERT INTO Event_history(SensorID, SensorName, EventType, EventDate, EventTime, UserID) VALUES(?, ?, ?, Date(?), Time(?), ?)';
                                var params = [camera.id, camera.name, camera.deviceType, strDate, strTime, id];
                                // Send information to database
                                connection.query(sql, params, function (err, result) {
                                    if (err) {
                                        console.log(err);
                                        return null;
                                    }
                                    else {
                                        return true;
                                    }
                                });
                                // Get information for notification
                                var deviceName = camera.name;
                                var title = deviceName + ' Ding';
                                var body = 'Check Cerberus App for more details';
                                // Send notification
                                console.log('notifying');
                                (0, node_fetch_1.default)('http://localhost:3000/notif', {
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
                            });
                        });
                    }
                    if (devices.length) {
                        devices.forEach(function (device) {
                            device.onData.subscribe(function (data) {
                                //console.log(`\n\n${nu} ${device.zid}: ${device.name} (${device.deviceType})\n`);
                                //console.log(`Data: ${JSON.stringify(data)}`);
                                //nu += 1;
                                // Only notify for certain events
                                if ('faulted' in data && data.faulted) {
                                    console.log("\n\n".concat(nu, " ").concat(device.zid, ": ").concat(device.name, " (").concat(device.deviceType, ")\n"));
                                    console.log("Data: ".concat(JSON.stringify(data)));
                                    nu += 1;
                                    // Get information for database & send
                                    var epoch = data["lastUpdate"];
                                    var date = new Date(epoch);
                                    var strYear = String(date.getFullYear());
                                    var strMonth = String(date.getMonth() + 1);
                                    if (strMonth.length < 2) {
                                        strMonth = '0' + strMonth;
                                    }
                                    var strDay = String(date.getDate());
                                    if (strDay.length < 2) {
                                        strDay = '0' + strDay;
                                    }
                                    var strDate = strYear + '-' + strMonth + '-' + strDay;
                                    var strHours = String(date.getHours());
                                    if (strHours.length < 2) {
                                        strHours = '0' + strHours;
                                    }
                                    var strMinutes = String(date.getMinutes());
                                    if (strMinutes.length < 2) {
                                        strMinutes = '0' + strMinutes;
                                    }
                                    var strSeconds = String(date.getSeconds());
                                    if (strSeconds.length < 2) {
                                        strSeconds = '0' + strSeconds;
                                    }
                                    var strTime = strHours + ':' + strMinutes + ':' + strSeconds;
                                    var sql = 'INSERT INTO Event_history(SensorID, SensorName, EventType, EventDate, EventTime, UserID) VALUES(?, ?, ?, Date(?), Time(?), ?)';
                                    var params = [device.zid, device.name, device.deviceType, strDate, strTime, id];
                                    // Send information to database
                                    connection.query(sql, params, function (err, result) {
                                        if (err) {
                                            console.log(err);
                                            return null;
                                        }
                                        else {
                                            return true;
                                        }
                                    });
                                    // Get information for notification
                                    var deviceName = device.name;
                                    var title = deviceName + ' Alert';
                                    var body = 'Check Cerberus App for more details';
                                    // Send notification
                                    console.log('notifying');
                                    (0, node_fetch_1.default)('http://localhost:3000/notif', {
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
                            });
                        });
                    }
                    _f.label = 7;
                case 7:
                    _b++;
                    return [3 /*break*/, 5];
                case 8: return [2 /*return*/];
            }
        });
    });
}
