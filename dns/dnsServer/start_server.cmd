@echo off
title start server
start cmd /c java -jar dnsserver.jar local 59000 ^& title Local Server ^& pause
start cmd /c java -jar dnsserver.jar root 59001 ^& title Root Server ^&  pause
start cmd /c java -jar dnsserver.jar tld 59002 com.txt ^& title .com TLD Server ^&  pause
start cmd /c java -jar dnsserver.jar tld 59003 net.txt ^& title .net TLD Server ^&  pause
start cmd /c java -jar dnsserver.jar tld 59004 org.txt ^& title .org TLD Server ^&  pause