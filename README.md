# Nemezida-UAV
System for targeting &amp; analize frames from drones and detect incidients.


## Libraries need for server & preparation on target machine 
1. Install POCO Libraies : On Linux(Debian): sudo apt-get install poco-dev 

## Server part description 
**Table 1. Protocol Transport description**

| Description | Socket Type | Port |
| ------ | ------ | ------ |
| Server default work port  | TCP | 9980|


**Table 2. HTTP requesy & responce API list list**
| Request| Request type |Responce| Responce type | Payload| Description|
| ------ | ------ | ------ | ------ | ------ | ------ |
| CREATE_SESSION |POST| Standart HTTP-CODES | HTTP CODE | NULL| Open insecure user session |
| DESTROY_SESSION |POST| Standart HTTP-CODES | HTTP CODE | NULL| Close insecure user session |
| MARK_IMG|POST| Standart HTTP-CODES | HTTP CODE | Image file for processing| Mark image for targeting (in payload image with rectan]gle list)|
| GET_REPORT|POST| JSON File with atributes |JSON File| NULL| Report about image|
