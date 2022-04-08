"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
exports.isWebSocketSupportedAsset = exports.disabledLocationModes = exports.DispatchSignalType = exports.DoorbellType = exports.ChimeModel = exports.deviceTypesWithVolume = exports.allAlarmStates = exports.RingCameraModel = exports.isBatteryCameraKind = exports.RingCameraKind = exports.RingDeviceCategory = exports.RingDeviceType = void 0;
// eslint-disable-next-line no-shadow
var RingDeviceType;
(function (RingDeviceType) {
    RingDeviceType["BaseStation"] = "hub.redsky";
    RingDeviceType["Keypad"] = "security-keypad";
    RingDeviceType["SecurityPanel"] = "security-panel";
    RingDeviceType["ContactSensor"] = "sensor.contact";
    RingDeviceType["MotionSensor"] = "sensor.motion";
    RingDeviceType["FloodFreezeSensor"] = "sensor.flood-freeze";
    RingDeviceType["FreezeSensor"] = "sensor.freeze";
    RingDeviceType["TemperatureSensor"] = "sensor.temperature";
    RingDeviceType["WaterSensor"] = "sensor.water";
    RingDeviceType["TiltSensor"] = "sensor.tilt";
    RingDeviceType["GlassbreakSensor"] = "sensor.glassbreak";
    RingDeviceType["RangeExtender"] = "range-extender.zwave";
    RingDeviceType["ZigbeeAdapter"] = "adapter.zigbee";
    RingDeviceType["AccessCodeVault"] = "access-code.vault";
    RingDeviceType["AccessCode"] = "access-code";
    RingDeviceType["SmokeAlarm"] = "alarm.smoke";
    RingDeviceType["CoAlarm"] = "alarm.co";
    RingDeviceType["SmokeCoListener"] = "listener.smoke-co";
    RingDeviceType["MultiLevelSwitch"] = "switch.multilevel";
    RingDeviceType["Fan"] = "switch.multilevel";
    RingDeviceType["MultiLevelBulb"] = "switch.multilevel.bulb";
    RingDeviceType["Switch"] = "switch";
    RingDeviceType["BeamsMotionSensor"] = "motion-sensor.beams";
    RingDeviceType["BeamsSwitch"] = "switch.beams";
    RingDeviceType["BeamsMultiLevelSwitch"] = "switch.multilevel.beams";
    RingDeviceType["BeamsLightGroupSwitch"] = "group.light-group.beams";
    RingDeviceType["BeamsTransformerSwitch"] = "switch.transformer.beams";
    RingDeviceType["BeamsDevice"] = "device.beams";
    RingDeviceType["RetrofitBridge"] = "bridge.flatline";
    RingDeviceType["RetrofitZone"] = "sensor.zone";
    RingDeviceType["Thermostat"] = "temperature-control.thermostat";
    RingDeviceType["Sensor"] = "sensor";
    RingDeviceType["RingNetAdapter"] = "adapter.ringnet";
    RingDeviceType["CodeVault"] = "access-code.vault";
    RingDeviceType["SecurityAccessCode"] = "access-code";
    RingDeviceType["ZWaveAdapter"] = "adapter.zwave";
    RingDeviceType["ZWaveExtender"] = "range-extender.zwave";
    RingDeviceType["PanicButton"] = "security-panic";
    RingDeviceType["UnknownZWave"] = "unknown.zwave";
})(RingDeviceType = exports.RingDeviceType || (exports.RingDeviceType = {}));
// eslint-disable-next-line no-shadow
var RingDeviceCategory;
(function (RingDeviceCategory) {
    RingDeviceCategory[RingDeviceCategory["Outlets"] = 1] = "Outlets";
    RingDeviceCategory[RingDeviceCategory["Lights"] = 2] = "Lights";
    RingDeviceCategory[RingDeviceCategory["Sensors"] = 5] = "Sensors";
    RingDeviceCategory[RingDeviceCategory["Appliances"] = 7] = "Appliances";
    RingDeviceCategory[RingDeviceCategory["Locks"] = 10] = "Locks";
    RingDeviceCategory[RingDeviceCategory["Thermostats"] = 11] = "Thermostats";
    RingDeviceCategory[RingDeviceCategory["Cameras"] = 12] = "Cameras";
    RingDeviceCategory[RingDeviceCategory["Alarms"] = 15] = "Alarms";
    RingDeviceCategory[RingDeviceCategory["Fans"] = 17] = "Fans";
    RingDeviceCategory[RingDeviceCategory["Security"] = 22] = "Security";
    RingDeviceCategory[RingDeviceCategory["Unknown"] = 29] = "Unknown";
    RingDeviceCategory[RingDeviceCategory["SensorsMotion"] = 30] = "SensorsMotion";
    RingDeviceCategory[RingDeviceCategory["Controller"] = 31] = "Controller";
    RingDeviceCategory[RingDeviceCategory["RangeExtenders"] = 32] = "RangeExtenders";
    RingDeviceCategory[RingDeviceCategory["Keypads"] = 33] = "Keypads";
    RingDeviceCategory[RingDeviceCategory["Sirens"] = 34] = "Sirens";
    RingDeviceCategory[RingDeviceCategory["PanicButtons"] = 35] = "PanicButtons";
})(RingDeviceCategory = exports.RingDeviceCategory || (exports.RingDeviceCategory = {}));
// eslint-disable-next-line no-shadow
var RingCameraKind;
(function (RingCameraKind) {
    RingCameraKind["doorbot"] = "doorbot";
    RingCameraKind["doorbell"] = "doorbell";
    RingCameraKind["doorbell_v3"] = "doorbell_v3";
    RingCameraKind["doorbell_v4"] = "doorbell_v4";
    RingCameraKind["doorbell_v5"] = "doorbell_v5";
    RingCameraKind["doorbell_portal"] = "doorbell_portal";
    RingCameraKind["doorbell_scallop"] = "doorbell_scallop";
    RingCameraKind["doorbell_scallop_lite"] = "doorbell_scallop_lite";
    RingCameraKind["doorbell_graham_cracker"] = "doorbell_graham_cracker";
    RingCameraKind["lpd_v1"] = "lpd_v1";
    RingCameraKind["lpd_v2"] = "lpd_v2";
    RingCameraKind["jbox_v1"] = "jbox_v1";
    RingCameraKind["stickup_cam"] = "stickup_cam";
    RingCameraKind["stickup_cam_v3"] = "stickup_cam_v3";
    RingCameraKind["stickup_cam_elite"] = "stickup_cam_elite";
    RingCameraKind["stickup_cam_lunar"] = "stickup_cam_lunar";
    RingCameraKind["spotlightw_v2"] = "spotlightw_v2";
    RingCameraKind["hp_cam_v1"] = "hp_cam_v1";
    RingCameraKind["hp_cam_v2"] = "hp_cam_v2";
    RingCameraKind["stickup_cam_v4"] = "stickup_cam_v4";
    RingCameraKind["floodlight_v1"] = "floodlight_v1";
    RingCameraKind["floodlight_v2"] = "floodlight_v2";
    RingCameraKind["cocoa_camera"] = "cocoa_camera";
    RingCameraKind["cocoa_doorbell"] = "cocoa_doorbell";
    RingCameraKind["stickup_cam_mini"] = "stickup_cam_mini";
})(RingCameraKind = exports.RingCameraKind || (exports.RingCameraKind = {}));
// RegExp taken from ring.com app
var isWiredCameraRegExp = new RegExp(/(^(lpd|jbox|stickup_cam_elite|stickup_cam_mini|hp_cam|spotlightw|floodlight|doorbell_graham_cracker))/);
function isBatteryCameraKind(kind) {
    return !kind.match(isWiredCameraRegExp);
}
exports.isBatteryCameraKind = isBatteryCameraKind;
exports.RingCameraModel = {
    doorbot: 'Doorbell',
    doorbell: 'Doorbell',
    doorbell_v3: 'Doorbell',
    doorbell_v4: 'Doorbell 2',
    doorbell_v5: 'Doorbell 2',
    doorbell_portal: 'Door View Cam',
    doorbell_scallop: 'Doorbell 3 Plus',
    doorbell_scallop_lite: 'Doorbell 3',
    doorbell_graham_cracker: 'Doorbell Wired',
    lpd_v1: 'Doorbell Pro',
    lpd_v2: 'Doorbell Pro',
    jbox_v1: 'Doorbell Elite',
    stickup_cam: 'Stick Up Cam',
    stickup_cam_v3: 'Stick Up Cam',
    stickup_cam_elite: 'Stick Up Cam',
    stickup_cam_lunar: 'Stick Up Cam',
    spotlightw_v2: 'Spotlight Cam',
    hp_cam_v1: 'Floodlight Cam',
    hp_cam_v2: 'Spotlight Cam',
    stickup_cam_v4: 'Spotlight Cam',
    floodlight_v1: 'Floodlight Cam',
    floodlight_v2: 'Floodlight Cam',
    cocoa_camera: 'Stick Up Cam',
    cocoa_doorbell: 'Doorbell Gen 2',
    stickup_cam_mini: 'Indoor Cam',
};
exports.allAlarmStates = [
    'burglar-alarm',
    'entry-delay',
    'fire-alarm',
    'co-alarm',
    'panic',
    'user-verified-burglar-alarm',
    'user-verified-co-or-fire-alarm',
    'burglar-accelerated-alarm',
    'fire-accelerated-alarm',
];
exports.deviceTypesWithVolume = [
    RingDeviceType.BaseStation,
    RingDeviceType.Keypad,
];
exports.ChimeModel = {
    chime: 'Chime',
    chime_pro: 'Chime Pro',
    chime_v2: 'Chime v2',
    chime_pro_v2: 'Chime Pro v2',
};
// eslint-disable-next-line no-shadow
var DoorbellType;
(function (DoorbellType) {
    DoorbellType[DoorbellType["Mechanical"] = 0] = "Mechanical";
    DoorbellType[DoorbellType["Digital"] = 1] = "Digital";
    DoorbellType[DoorbellType["None"] = 2] = "None";
})(DoorbellType = exports.DoorbellType || (exports.DoorbellType = {}));
// eslint-disable-next-line no-shadow
var DispatchSignalType;
(function (DispatchSignalType) {
    DispatchSignalType["Burglar"] = "user-verified-burglar-xa";
    DispatchSignalType["Fire"] = "user-verified-fire-xa";
})(DispatchSignalType = exports.DispatchSignalType || (exports.DispatchSignalType = {}));
exports.disabledLocationModes = ['disabled', 'unset'];
function isWebSocketSupportedAsset(_a) {
    var kind = _a.kind;
    return kind.startsWith('base_station') || kind.startsWith('beams_bridge');
}
exports.isWebSocketSupportedAsset = isWebSocketSupportedAsset;
