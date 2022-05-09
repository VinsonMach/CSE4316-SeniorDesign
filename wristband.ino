#include <ArduinoBLE.h>
#include <TimedAction.h>

int RED_PIN=9;
int GREEN_PIN=8;
int BLUE_PIN=7;
int VIB_PIN=10;
int VIB_PIN2 = 6;
int AUDIO_IN = A0;
const char* deviceServiceUuid = "19b10000-e8f2-537e-4f6c-d104768a1214";

const char* deviceServiceCharacteristicUuid = "19b10001-e8f2-537e-4f6c-d104768a1214";
BLEService gestureService(deviceServiceUuid); 

BLEStringCharacteristic gestureCharacteristic(deviceServiceCharacteristicUuid, BLERead | BLEWrite, 128);

byte queue[10][2];

void setup(){
    pinMode(RED_PIN, OUTPUT);
    pinMode(BLUE_PIN, OUTPUT);
    pinMode(GREEN_PIN, OUTPUT);
    pinMode(VIB_PIN, OUTPUT);
    pinMode(VIB_PIN2, OUTPUT);
    pinMode(AUDIO_IN, INPUT);
    pinMode(LED_BUILTIN, OUTPUT);
    if (!BLE.begin()) {
      Serial.println("- Starting Bluetooth® Low Energy module failed!");
      while (1);
  }
  
  BLE.setLocalName("Wristband");
  BLE.setAdvertisedService(gestureService);
  gestureService.addCharacteristic(gestureCharacteristic);
  BLE.addService(gestureService);
  gestureCharacteristic.writeValue("");
  BLE.advertise();
}

void insert(byte b1, byte b2){
  for(int i = 0; i < 10; i++){
    if(queue[i][0] == 0){
      queue[i][0] = b1;
      queue[i][1] = b2;
      break;
    }
  }
}

void shift(byte arr[10][2]){
  for(int i = 0; i < 9; i++){
    arr[i][0] = arr[i+1][0];
    arr[i][1] = arr[i+1][1];
  }
  arr[9][0] = 0;
  arr[9][1] = 0;
}

void vibrate(){
  if(queue[0][0] != 0){
    for(int i = 0; i < queue[0][1]; i++){
      digitalWrite(VIB_PIN, HIGH);
      digitalWrite(VIB_PIN2, HIGH);
      digitalWrite(LED_BUILTIN, HIGH);
      delay(queue[0][0]*100);
      digitalWrite(VIB_PIN, LOW);
      digitalWrite(VIB_PIN2, LOW);
      digitalWrite(LED_BUILTIN, LOW);
      delay(100);
    }
    shift(queue);
  }
}

TimedAction vibrateThread = TimedAction(10,vibrate);

void loop(){
    vibrateThread.check();
    BLEDevice central = BLE.central();
    digitalWrite(BLUE_PIN, HIGH);
    digitalWrite(GREEN_PIN, LOW);
    digitalWrite(RED_PIN, LOW);
    delay(500);
    digitalWrite(BLUE_PIN, LOW);
    if (central) {


    while (central.connected()) {
      vibrateThread.check();
      if (gestureCharacteristic.written()) {
        String read_in = gestureCharacteristic.value();
        char temp[3];
        read_in.substring(0, read_in.indexOf(" ")).toCharArray(temp, 3);
        byte byte1 = atoi(temp);
        read_in.substring(read_in.indexOf(" ")+1, read_in.length()).toCharArray(temp, 3);
        byte byte2 = atoi(temp);
        insert(byte1, byte2);
      }
      int vol = analogRead(AUDIO_IN);
      if(vol < 50){
        digitalWrite(GREEN_PIN, HIGH);
        digitalWrite(RED_PIN, LOW);
        digitalWrite(BLUE_PIN, LOW);
      }
      else if(vol < 512){
        digitalWrite(GREEN_PIN, HIGH);
        digitalWrite(RED_PIN, HIGH);
        digitalWrite(BLUE_PIN, LOW);
      }
      else{
        digitalWrite(GREEN_PIN, LOW);
        digitalWrite(RED_PIN, HIGH);
        digitalWrite(BLUE_PIN, LOW);
      }
    }


  }

}
