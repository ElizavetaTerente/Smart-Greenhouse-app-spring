
# Scan QR-Code with your Smartphone to reach a gallery of the plant

! WIFI for your PC and Smartphone should be the same.

1. Use next command in your Terminal to find IP-address:
- ipconfig (Windows) or
- ifconfig | grep "inet " | grep -v 127.0.0.1 (Unix) 

2. put IP-address in file src/main/webapp/admin/concreteHouse.html on the line 795 AS ADMIN:
or
   put IP-address in file src/main/webapp/gardener/concreteHouse.html on the line 731 AS GARDENER:
new QRCode(document.getElementById("qrcode"), "XXXXXXXXXX:8080/besucher/concreteHouse.html?id=" + id);

my case :
new QRCode(document.getElementById("qrcode"), "172.16.220.30:8080/besucher/concreteHouse.html?id=" + id);

3. now you can scan QR-Code with your Smartphone and be redirect to web-page.

