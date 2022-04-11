#include <ArduinoBLE.h>

int RED_PIN=9;
int BLUE_PIN=7;
int GREEN_PIN=5;
int VIB_PIN=3;
int AUDIO_IN = A0;
const char* deviceServiceUuid = "19b10000-e8f2-537e-4f6c-d104768a1214";

const char* deviceServiceCharacteristicUuid = "19b10001-e8f2-537e-4f6c-d104768a1214";
BLEService gestureService(deviceServiceUuid); 

BLEByteCharacteristic gestureCharacteristic(deviceServiceCharacteristicUuid, BLERead | BLEWrite);
int ledeez[3];

void setup(){
    pinMode(RED_PIN, OUTPUT);
    pinMode(BLUE_PIN, OUTPUT);
    pinMode(GREEN_PIN, OUTPUT);
    pinMode(VIB_PIN, OUTPUT);
    pinMode(AUDIO_IN, INPUT);
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
  gestureCharacteristic.writeValue(-1);
  BLE.advertise();
}

void loop(){
    BLEDevice central = BLE.central();
    Serial.println("- Discovering central device...");
    delay(5000);
    if (central) {

    Serial.println("* Connected to central device!");

    Serial.print("* Device MAC address: ");

    Serial.println(central.address());

    Serial.println(" ");


    while (central.connected()) {
      int vol = analogRead(AUDIO_IN);
      Serial.print(vol);
      int ledLevel = map(abs(vol-540), 0, (1024-540), 0, 3);
      for(int i = 0; i < 3; i++){
        if(i < ledLevel){
            analogWrite(ledeez[i], 128);
        }
        else{
            analogWrite(ledeez[i], 0);
        }
      }
    }

    

    Serial.println("* Disconnected to central device!");

  }

}
