# Java-Music-Player
자바 기반 음악 플레이어

### 🗂️ 프로젝트 
<br>

### 📆 개발 기간
* 2022년 11월 1일 ~ 2022년 12월 13일

#### 🙋🏻‍♂️ 멤버 구성
 - 장성용(팀장): 프로젝트 설계, I2C Display 제어 모듈 제작
 - [정영준](https://github.com/JeongYeongJoon): 온습도 제어를 통한 LED 제어
 - [박윤서](https://github.com/wndrksdl): 조도 센서를 통한 블라인드(Servo Motor) 제어
 

#### 🖥️ 개발 환경
 - Slave HW: `F103RB(on MBed OS 5)`
 - Master HW: `Unity 2D Program`
 - Sensor: `Light Sensor`, `IR Sensor`, `Temperature Sensor`
 - Actuator: `Led`, `Servo Motor`, `I2C Display`, `Speaker Module`
 - IDE: MBed Studio, Unity

#### 🔖 주요 기능
 - 센서를 통한 현재 상태 측정(온도, 조도, 물체 가림 여부)
 - 측정된 온도와 조도, 가림여부에 따른 각각의 모듈 제어(TV, 에어컨 등)
 - I2C Display를 통한 제어 상태 출력
 - Serial 통신을 이용하여 Unity 2D 제어프로그램 기반 제어

### 🕹️ 동작 화면
![스크린샷 2023-01-20 오후 1 51 18](https://user-images.githubusercontent.com/74158951/213619767-4bb00a8f-4200-45df-884a-1b4090fa1a2e.png)


