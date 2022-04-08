"use strict";
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
exports.randomString = exports.randomInteger = exports.mapAsync = exports.stringify = exports.requestInput = exports.getHardwareId = exports.generateUuid = exports.enableDebug = exports.useLogger = exports.logError = exports.logInfo = exports.logDebug = exports.delay = void 0;
// from dgreif/ring 
var debug_1 = require("debug");
var colors_1 = require("colors");
var readline_1 = require("readline");
var uuid_1 = require("uuid");
var systeminformation_1 = require("systeminformation");
var debugLogger = (0, debug_1.default)('ring'), uuidNamespace = 'e53ffdc0-e91d-4ce1-bec2-df939d94739c';
var logger = {
    logInfo: function (message) {
        debugLogger(message);
    },
    logError: function (message) {
        debugLogger((0, colors_1.red)(message));
    },
}, debugEnabled = false;
function delay(milliseconds) {
    return new Promise(function (resolve) {
        setTimeout(resolve, milliseconds);
    });
}
exports.delay = delay;
function logDebug(message) {
    if (debugEnabled) {
        logger.logInfo(message);
    }
}
exports.logDebug = logDebug;
function logInfo(message) {
    logger.logInfo(message);
}
exports.logInfo = logInfo;
function logError(message) {
    logger.logError(message);
}
exports.logError = logError;
function useLogger(newLogger) {
    logger = newLogger;
}
exports.useLogger = useLogger;
function enableDebug() {
    debugEnabled = true;
}
exports.enableDebug = enableDebug;
function generateUuid(seed) {
    if (seed) {
        return (0, uuid_1.v5)(seed, uuidNamespace);
    }
    return (0, uuid_1.v4)();
}
exports.generateUuid = generateUuid;
function getHardwareId(systemId) {
    return __awaiter(this, void 0, void 0, function () {
        var timeoutValue, id;
        return __generator(this, function (_a) {
            switch (_a.label) {
                case 0:
                    if (systemId) {
                        return [2 /*return*/, generateUuid(systemId)];
                    }
                    timeoutValue = '-1';
                    return [4 /*yield*/, Promise.race([
                            (0, systeminformation_1.uuid)(),
                            delay(5000).then(function () { return ({ os: timeoutValue }); }),
                        ])];
                case 1:
                    id = (_a.sent()).os;
                    if (id === timeoutValue) {
                        logError('Request for system uuid timed out.  Falling back to random session id');
                        return [2 /*return*/, (0, uuid_1.v4)()];
                    }
                    if (id === '-') {
                        // default value set by systeminformation if it can't find a real value
                        logError('Unable to get system uuid.  Falling back to random session id');
                        return [2 /*return*/, (0, uuid_1.v4)()];
                    }
                    return [2 /*return*/, generateUuid(id)];
            }
        });
    });
}
exports.getHardwareId = getHardwareId;
function requestInput(question) {
    return __awaiter(this, void 0, void 0, function () {
        var lineReader, answer;
        return __generator(this, function (_a) {
            switch (_a.label) {
                case 0:
                    lineReader = (0, readline_1.createInterface)({
                        input: process.stdin,
                        output: process.stdout,
                    });
                    return [4 /*yield*/, new Promise(function (resolve) {
                            lineReader.question(question, resolve);
                        })];
                case 1:
                    answer = _a.sent();
                    lineReader.close();
                    return [2 /*return*/, answer.trim()];
            }
        });
    });
}
exports.requestInput = requestInput;
function stringify(data) {
    if (typeof data === 'string') {
        return data;
    }
    if (typeof data === 'object' && Buffer.isBuffer(data)) {
        return data.toString();
    }
    return JSON.stringify(data) + '';
}
exports.stringify = stringify;
function mapAsync(records, asyncMapper) {
    return Promise.all(records.map(function (record) { return asyncMapper(record); }));
}
exports.mapAsync = mapAsync;
function randomInteger() {
    return Math.floor(Math.random() * 99999999) + 100000;
}
exports.randomInteger = randomInteger;
function randomString(length) {
    var uuid = generateUuid();
    return uuid.replace(/-/g, '').substring(0, length).toLowerCase();
}
exports.randomString = randomString;
