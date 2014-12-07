These are payloads for settings stuff.

### Set Heat
{"H":"thermostat-v1","M":"SetHeat","A":["36-6f-92-ff-fe-01-3a-1b",68,"F"],"I":2}
{"H":"thermostat-v1","M":"SetHeat","A":["36-6f-92-ff-fe-01-3a-1b",65,"F"],"I":5}

####Response:

ICD: 36-6f-92-ff-fe-01-3a-1b, Model: {Capabilities={HumidityDisplay=true, HeatLimits={Min={F=45.0, C=7.0}, Max={F=99.0, C=37.0}}, IndoorEquipment=Electric, IndoorStages=1.0, OutdoorEquipment=AC, OutdoorStages=2.0}, EnvironmentControls={HeatSetpoint={F=65.0, C=18.0}}, OperationalStatus={Temperature={F=70.0, C=21.0}, Humidity=36.0, BatteryVoltage=3227.0, Running={Mode=Off}, LowPower=false, OperatingMode=AutoHeat, ScheduleTemps={AutoHeat={F=62.0, C=17.0}, AutoCool={F=83.0, C=28.0}, Heat={F=62.0, C=17.0}, Cool={F=83.0, C=28.0}}}}

#### Auto System Mode Enabled

{"H":"thermostat-v1","M":"SetAutoHeat","A":["36-6f-92-ff-fe-01-3a-1b",64,"F"],"I":12}
{"H":"thermostat-v1","M":"SetAutoCool","A":["36-6f-92-ff-fe-01-3a-1b",84,"F"],"I":13}

### Set System Mode
{"H":"thermostat-v1","M":"SetSystemMode","A":["36-6f-92-ff-fe-01-3a-1b","Cool"],"I":7}
{"H":"thermostat-v1","M":"SetSystemMode","A":["36-6f-92-ff-fe-01-3a-1b","Heat"],"I":9}
{"H":"thermostat-v1","M":"SetSystemMode","A":["36-6f-92-ff-fe-01-3a-1b","Auto"],"I":11}

