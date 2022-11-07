#!/bin/sh
java -jar dnsserver.jar local 59000 &
java -jar dnsserver.jar root 59001 &
java -jar dnsserver.jar tld 59002 net.txt &
java -jar dnsserver.jar tld 59003 com.txt &
java -jar dnsserver.jar tld 59004 org.txt &