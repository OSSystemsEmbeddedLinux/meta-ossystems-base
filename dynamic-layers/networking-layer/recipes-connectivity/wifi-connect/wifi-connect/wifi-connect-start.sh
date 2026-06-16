#!/bin/sh

export DBUS_SYSTEM_BUS_ADDRESS=unix:path=/run/dbus/system_bus_socket

echo "Starting WiFi Connect"
/usr/bin/wifi-connect -u /usr/share/wifi-connect/ui/
