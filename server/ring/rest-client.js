"use strict";
var __assign = (this && this.__assign) || function () {
    __assign = Object.assign || function(t) {
        for (var s, i = 1, n = arguments.length; i < n; i++) {
            s = arguments[i];
            for (var p in s) if (Object.prototype.hasOwnProperty.call(s, p))
                t[p] = s[p];
        }
        return t;
    };
    return __assign.apply(this, arguments);
};
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
exports.RingRestClient = exports.appApi = exports.deviceApi = exports.clientApi = void 0;
// from dgreif/ring 
var got_1 = require("got");
var util_1 = require("./util");
var rxjs_1 = require("rxjs");
var defaultRequestOptions = {
    responseType: 'json',
    method: 'GET',
    retry: 0,
    timeout: 20000,
}, ringErrorCodes = {
    7050: 'NO_ASSET',
    7019: 'ASSET_OFFLINE',
    7061: 'ASSET_CELL_BACKUP',
    7062: 'UPDATING',
    7063: 'MAINTENANCE',
}, clientApiBaseUrl = 'https://api.ring.com/clients_api/', deviceApiBaseUrl = 'https://api.ring.com/devices/v1/', appApiBaseUrl = 'https://app.ring.com/api/v1/', apiVersion = 11;
function clientApi(path) {
    return clientApiBaseUrl + path;
}
exports.clientApi = clientApi;
function deviceApi(path) {
    return deviceApiBaseUrl + path;
}
exports.deviceApi = deviceApi;
function appApi(path) {
    return appApiBaseUrl + path;
}
exports.appApi = appApi;
function requestWithRetry(requestOptions) {
    return __awaiter(this, void 0, void 0, function () {
        var options, _a, headers, body, data, e_1;
        return __generator(this, function (_b) {
            switch (_b.label) {
                case 0:
                    _b.trys.push([0, 2, , 5]);
                    options = __assign(__assign({}, defaultRequestOptions), requestOptions);
                    return [4 /*yield*/, (0, got_1.default)(options)];
                case 1:
                    _a = (_b.sent()), headers = _a.headers, body = _a.body, data = body;
                    if (data !== null && typeof data === 'object' && headers.date) {
                        data.responseTimestamp = new Date(headers.date).getTime();
                    }
                    return [2 /*return*/, data];
                case 2:
                    e_1 = _b.sent();
                    if (!!e_1.response) return [3 /*break*/, 4];
                    (0, util_1.logError)("Failed to reach Ring server at ".concat(requestOptions.url, ".  ").concat(e_1.message, ".  Trying again in 5 seconds..."));
                    if (e_1.message.includes('NGHTTP2_ENHANCE_YOUR_CALM')) {
                        (0, util_1.logError)("There is a known issue with your current NodeJS version (".concat(process.version, ").  Please see https://github.com/dgreif/ring/wiki/NGHTTP2_ENHANCE_YOUR_CALM-Error for details"));
                    }
                    (0, util_1.logDebug)(e_1);
                    return [4 /*yield*/, (0, util_1.delay)(5000)];
                case 3:
                    _b.sent();
                    return [2 /*return*/, requestWithRetry(requestOptions)];
                case 4: throw e_1;
                case 5: return [2 /*return*/];
            }
        });
    });
}
var RingRestClient = /** @class */ (function () {
    function RingRestClient(authOptions) {
        this.authOptions = authOptions;
        this.timeouts = [];
        this.sessionPromise = undefined;
        this.using2fa = false;
        this.onRefreshTokenUpdated = new rxjs_1.ReplaySubject(1);
        this.refreshToken =
            'refreshToken' in this.authOptions
                ? this.authOptions.refreshToken
                : undefined;
        this.hardwareIdPromise = (0, util_1.getHardwareId)(this.authOptions.systemId);
    }
    RingRestClient.prototype.clearPreviousAuth = function () {
        this._authPromise = undefined;
    };
    Object.defineProperty(RingRestClient.prototype, "authPromise", {
        get: function () {
            var _this = this;
            if (!this._authPromise) {
                var authPromise_1 = this.getAuth();
                this._authPromise = authPromise_1;
                authPromise_1
                    .then(function (_a) {
                    var expires_in = _a.expires_in;
                    // clear the existing auth promise 1 minute before it expires
                    var timeout = setTimeout(function () {
                        if (_this._authPromise === authPromise_1) {
                            _this.clearPreviousAuth();
                        }
                    }, ((expires_in || 3600) - 60) * 1000);
                    _this.timeouts.push(timeout);
                })
                    .catch(function () {
                    // ignore these errors here, they should be handled by the function making a rest request
                });
            }
            return this._authPromise;
        },
        enumerable: false,
        configurable: true
    });
    RingRestClient.prototype.getGrantData = function (twoFactorAuthCode) {
        if (this.refreshToken && !twoFactorAuthCode) {
            return {
                grant_type: 'refresh_token',
                refresh_token: this.refreshToken,
            };
        }
        var authOptions = this.authOptions;
        if ('email' in authOptions) {
            return {
                grant_type: 'password',
                password: authOptions.password,
                username: authOptions.email,
            };
        }
        throw new Error('Refresh token is not valid.  Unable to authenticate with Ring servers.  See https://github.com/dgreif/ring/wiki/Refresh-Tokens');
    };
    RingRestClient.prototype.getAuth = function (twoFactorAuthCode) {
        return __awaiter(this, void 0, void 0, function () {
            var grantData, response, _a, requestError_1, response, responseData, responseError, tsv_state, phone, prompt_1, authTypeMessage, errorMessage;
            var _b, _c;
            return __generator(this, function (_d) {
                switch (_d.label) {
                    case 0:
                        grantData = this.getGrantData(twoFactorAuthCode);
                        _d.label = 1;
                    case 1:
                        _d.trys.push([1, 4, , 5]);
                        _a = requestWithRetry;
                        _b = {
                            url: 'https://oauth.ring.com/oauth/token',
                            json: __assign({ client_id: 'ring_official_android', scope: 'client' }, grantData),
                            method: 'POST'
                        };
                        _c = {
                            '2fa-support': 'true',
                            '2fa-code': twoFactorAuthCode || ''
                        };
                        return [4 /*yield*/, this.hardwareIdPromise];
                    case 2: return [4 /*yield*/, _a.apply(void 0, [(_b.headers = (_c.hardware_id = _d.sent(),
                                _c),
                                _b)])];
                    case 3:
                        response = _d.sent();
                        this.onRefreshTokenUpdated.next({
                            oldRefreshToken: this.refreshToken,
                            newRefreshToken: response.refresh_token,
                        });
                        this.refreshToken = response.refresh_token;
                        return [2 /*return*/, response];
                    case 4:
                        requestError_1 = _d.sent();
                        if (grantData.refresh_token) {
                            // failed request with refresh token
                            this.refreshToken = undefined;
                            (0, util_1.logError)(requestError_1);
                            return [2 /*return*/, this.getAuth()];
                        }
                        response = requestError_1.response || {}, responseData = response.body || {}, responseError = 'error' in responseData && typeof responseData.error === 'string'
                            ? responseData.error
                            : '';
                        if (response.statusCode === 412 || // need 2fa code
                            (response.statusCode === 400 &&
                                responseError.startsWith('Verification Code')) // invalid 2fa code entered
                        ) {
                            (0, util_1.logError)(requestError_1);
                            this.using2fa = true;
                            if ('tsv_state' in responseData) {
                                tsv_state = responseData.tsv_state, phone = responseData.phone, prompt_1 = tsv_state === 'totp'
                                    ? 'from your authenticator app'
                                    : "sent to ".concat(phone, " via ").concat(tsv_state);
                                this.promptFor2fa = "Please enter the code ".concat(prompt_1);
                            }
                            else {
                                this.promptFor2fa = 'Please enter the code sent to your text/email';
                            }
                            throw new Error('Your Ring account is configured to use 2-factor authentication (2fa).  See https://github.com/dgreif/ring/wiki/Refresh-Tokens for details.');
                        }
                        authTypeMessage = 'refreshToken' in this.authOptions
                            ? 'refresh token is'
                            : 'email and password are', errorMessage = 'Failed to fetch oauth token from Ring. ' +
                            ('error_description' in responseData &&
                                responseData.error_description ===
                                    'too many requests from dependency service'
                                ? 'You have requested too many 2fa codes.  Ring limits 2fa to 10 codes within 10 minutes.  Please try again in 10 minutes.'
                                : "Verify that your ".concat(authTypeMessage, " correct.")) +
                            " (error: ".concat(responseError, ")");
                        (0, util_1.logError)(requestError_1.response || requestError_1);
                        (0, util_1.logError)(errorMessage);
                        throw new Error(errorMessage);
                    case 5: return [2 /*return*/];
                }
            });
        });
    };
    RingRestClient.prototype.fetchNewSession = function (authToken) {
        var _a;
        return __awaiter(this, void 0, void 0, function () {
            var _b;
            var _c, _d, _e;
            return __generator(this, function (_f) {
                switch (_f.label) {
                    case 0:
                        _b = requestWithRetry;
                        _c = {
                            url: clientApi('session')
                        };
                        _d = {};
                        _e = {};
                        return [4 /*yield*/, this.hardwareIdPromise];
                    case 1: return [2 /*return*/, _b.apply(void 0, [(_c.json = (_d.device = (_e.hardware_id = _f.sent(),
                                _e.metadata = {
                                    api_version: apiVersion,
                                    device_model: (_a = this.authOptions.controlCenterDisplayName) !== null && _a !== void 0 ? _a : 'ring-client-api',
                                },
                                _e.os = 'android',
                                _e),
                                _d),
                                _c.method = 'POST',
                                _c.headers = {
                                    authorization: "Bearer ".concat(authToken.access_token),
                                },
                                _c)])];
                }
            });
        });
    };
    RingRestClient.prototype.getSession = function () {
        var _this = this;
        return this.authPromise.then(function (authToken) { return __awaiter(_this, void 0, void 0, function () {
            var e_2, response, retryAfter, waitSeconds;
            return __generator(this, function (_a) {
                switch (_a.label) {
                    case 0:
                        _a.trys.push([0, 2, , 7]);
                        return [4 /*yield*/, this.fetchNewSession(authToken)];
                    case 1: return [2 /*return*/, _a.sent()];
                    case 2:
                        e_2 = _a.sent();
                        response = e_2.response || {};
                        if (!(response.statusCode === 401)) return [3 /*break*/, 4];
                        return [4 /*yield*/, this.refreshAuth()];
                    case 3:
                        _a.sent();
                        return [2 /*return*/, this.getSession()];
                    case 4:
                        if (!(response.statusCode === 429)) return [3 /*break*/, 6];
                        retryAfter = e_2.response.headers['retry-after'], waitSeconds = isNaN(retryAfter)
                            ? 200
                            : Number.parseInt(retryAfter, 10);
                        (0, util_1.logError)("Session response rate limited. Waiting to retry after ".concat(waitSeconds, " seconds"));
                        return [4 /*yield*/, (0, util_1.delay)((waitSeconds + 1) * 1000)];
                    case 5:
                        _a.sent();
                        (0, util_1.logInfo)('Retrying session request');
                        return [2 /*return*/, this.getSession()];
                    case 6: throw e_2;
                    case 7: return [2 /*return*/];
                }
            });
        }); });
    };
    RingRestClient.prototype.refreshAuth = function () {
        return __awaiter(this, void 0, void 0, function () {
            return __generator(this, function (_a) {
                switch (_a.label) {
                    case 0:
                        this.clearPreviousAuth();
                        return [4 /*yield*/, this.authPromise];
                    case 1:
                        _a.sent();
                        return [2 /*return*/];
                }
            });
        });
    };
    RingRestClient.prototype.refreshSession = function () {
        this.sessionPromise = this.getSession();
    };
    RingRestClient.prototype.request = function (options) {
        var _a, _b;
        return __awaiter(this, void 0, void 0, function () {
            var hardwareId, url, initialSessionPromise, authTokenResponse, e_3, response, errors, errorText;
            return __generator(this, function (_c) {
                switch (_c.label) {
                    case 0: return [4 /*yield*/, this.hardwareIdPromise];
                    case 1:
                        hardwareId = _c.sent(), url = options.url, initialSessionPromise = this.sessionPromise;
                        _c.label = 2;
                    case 2:
                        _c.trys.push([2, 6, , 14]);
                        return [4 /*yield*/, initialSessionPromise];
                    case 3:
                        _c.sent();
                        return [4 /*yield*/, this.authPromise];
                    case 4:
                        authTokenResponse = _c.sent();
                        return [4 /*yield*/, requestWithRetry(__assign(__assign({}, options), { headers: __assign(__assign({}, options.headers), { authorization: "Bearer ".concat(authTokenResponse.access_token), hardware_id: hardwareId, 'User-Agent': 'android:com.ringapp' }) }))];
                    case 5: return [2 /*return*/, _c.sent()];
                    case 6:
                        e_3 = _c.sent();
                        response = e_3.response || {};
                        if (!(response.statusCode === 401)) return [3 /*break*/, 8];
                        return [4 /*yield*/, this.refreshAuth()];
                    case 7:
                        _c.sent();
                        return [2 /*return*/, this.request(options)];
                    case 8:
                        if (!(response.statusCode === 504)) return [3 /*break*/, 10];
                        // Gateway Timeout.  These should be recoverable, but wait a few seconds just to be on the safe side
                        return [4 /*yield*/, (0, util_1.delay)(5000)];
                    case 9:
                        // Gateway Timeout.  These should be recoverable, but wait a few seconds just to be on the safe side
                        _c.sent();
                        return [2 /*return*/, this.request(options)];
                    case 10:
                        if (!(response.statusCode === 404 &&
                            response.body &&
                            Array.isArray(response.body.errors))) return [3 /*break*/, 13];
                        errors = response.body.errors, errorText = errors
                            .map(function (code) { return ringErrorCodes[code]; })
                            .filter(function (x) { return x; })
                            .join(', ');
                        if (!errorText) return [3 /*break*/, 12];
                        (0, util_1.logError)("http request failed.  ".concat(url, " returned errors: (").concat(errorText, ").  Trying again in 20 seconds"));
                        return [4 /*yield*/, (0, util_1.delay)(20000)];
                    case 11:
                        _c.sent();
                        return [2 /*return*/, this.request(options)];
                    case 12:
                        (0, util_1.logError)("http request failed.  ".concat(url, " returned unknown errors: (").concat((0, util_1.stringify)(errors), ")."));
                        _c.label = 13;
                    case 13:
                        if (response.statusCode === 404 && url.startsWith(clientApiBaseUrl)) {
                            (0, util_1.logError)('404 from endpoint ' + url);
                            if ((_b = (_a = response.body) === null || _a === void 0 ? void 0 : _a.error) === null || _b === void 0 ? void 0 : _b.includes(hardwareId)) {
                                (0, util_1.logError)('Session hardware_id not found.  Creating a new session and trying again.');
                                if (this.sessionPromise === initialSessionPromise) {
                                    this.refreshSession();
                                }
                                return [2 /*return*/, this.request(options)];
                            }
                            throw new Error('Not found with response: ' + (0, util_1.stringify)(response.body));
                        }
                        if (response.statusCode) {
                            (0, util_1.logError)("Request to ".concat(url, " failed with status ").concat(response.statusCode, ". Response body: ").concat((0, util_1.stringify)(response.body)));
                        }
                        else {
                            (0, util_1.logError)("Request to ".concat(url, " failed:"));
                            (0, util_1.logError)(e_3);
                        }
                        throw e_3;
                    case 14: return [2 /*return*/];
                }
            });
        });
    };
    RingRestClient.prototype.getCurrentAuth = function () {
        return this.authPromise;
    };
    RingRestClient.prototype.clearTimeouts = function () {
        this.timeouts.forEach(clearTimeout);
    };
    return RingRestClient;
}());
exports.RingRestClient = RingRestClient;
