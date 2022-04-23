#include <ArduinoBLE.h>
#include <TimedAction.h>

int RED_PIN=9;
int BLUE_PIN=7;
int GREEN_PIN=5;
int VIB_PIN=3;
int AUDIO_IN = A0;
const char* deviceServiceUuid = "19b10000-e8f2-537e-4f6c-d104768a1214";

const char* deviceServiceCharacteristicUuid = "19b10001-e8f2-537e-4f6c-d104768a1214";
BLEService gestureService(deviceServiceUuid); 

BLEStringCharacteristic gestureCharacteristic(deviceServiceCharacteristicUuid, BLERead | BLEWrite, 128);
int ledeez[3];

byte queue[10][2];

void setup(){
    pinMode(RED_PIN, OUTPUT);
    pinMode(BLUE_PIN, OUTPUT);
    pinMode(GREEN_PIN, OUTPUT);
    pinMode(VIB_PIN, OUTPUT);
    pinMode(AUDIO_IN, INPUT);
    pinMode(LED_BUILTIN, OUTPUT);
    ledeez[0] = 9;
    ledeez[1] = 7;
    ledeez[2] = 5;
    Serial.begin(9600);
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
    Serial.println("IN HERE");
    for(int i = 0; i < queue[0][1]; i++){
      Serial.println("LOOP");
      digitalWrite(LED_BUILTIN, HIGH);
      delay(queue[0][0]*10);
      digitalWrite(LED_BUILTIN, LOW);
      delay(25);
    }
    shift(queue);
  }
}

TimedAction vibrateThread = TimedAction(10,vibrate);

void loop(){
    vibrateThread.check();
    BLEDevice central = BLE.central();
    Serial.println("- Discovering central device...");
    delay(500);
    if (central) {

    Serial.println("* Connected to central device!");

    Serial.print("* Device MAC address: ");

    Serial.println(central.address());

    Serial.println(" ");


    while (central.connected()) {
      vibrateThread.check();
      if (gestureCharacteristic.written()) {
        Serial.println("WRITE");
        String read_in = gestureCharacteristic.value();
        char temp[3];
        read_in.substring(0, read_in.indexOf(" ")).toCharArray(temp, 3);
        byte byte1 = atoi(temp);
        read_in.substring(read_in.indexOf(" ")+1, read_in.length()).toCharArray(temp, 3);
        byte byte2 = atoi(temp);
        Serial.println(byte2);
        insert(byte1, byte2);
      }
      int vol = 10;//analogRead(AUDIO_IN);
      //Serial.print(vol);
      int ledLevel = map(abs(vol-540), 0, (1024-540), 0, 3);
      for(int i = 0; i < 3; i++){
        vibrateThread.check();
        if(i < ledLevel){
            //analogWrite(ledeez[i], 128);
        }
        else{
            //analogWrite(ledeez[i], 0);
        }
      }
    }

    

    Serial.println("* Disconnected to central device!");

  }

}
