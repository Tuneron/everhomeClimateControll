package com.budova.everhome.driver;

import com.budova.everhome.domain.*;
import com.budova.everhome.dto.*;
import com.budova.everhome.repos.*;
import com.intelligt.modbus.jlibmodbus.Modbus;
import com.intelligt.modbus.jlibmodbus.exception.ModbusIOException;
import com.intelligt.modbus.jlibmodbus.exception.ModbusNumberException;
import com.intelligt.modbus.jlibmodbus.exception.ModbusProtocolException;
import com.intelligt.modbus.jlibmodbus.master.ModbusMaster;
import com.intelligt.modbus.jlibmodbus.master.ModbusMasterFactory;
import com.intelligt.modbus.jlibmodbus.tcp.TcpParameters;
import com.sun.org.apache.xalan.internal.xsltc.util.IntegerArray;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Set;

@Controller
@EnableScheduling
public class RautControllerDriver {

    @Autowired
    private ConnectionRepo connectionRepo;
    @Autowired
    private HumidityRepo humidityRepo;
    @Autowired
    private TemperatureRepo tempRepo;
    @Autowired
    private SetPowerRepo setPowerRepo;
    @Autowired
    private SetTemperatureRepo setTemperatureRepo;
    @Autowired
    private SetConditionerModeRepo setConditionerModeRepo;
    @Autowired
    private SetFlugerRepo setFlugerRepo;
    @Autowired
    private SetFanSpeedRepo setFanSpeedRepo;
    @Autowired
    private SetCustomRepo setCustomRepo;
    @Autowired
    private SetSettingRepo setSettingRepo;
    @Autowired
    private SetClimateModeRepo setClimateModeRepo;
    @Autowired
    private SetSeasonRepo setSeasonRepo;
    @Autowired
    private SetRadiatorRepo setRadiatorRepo;
    @Autowired
    private SetHumidifierRepo setHumidifierRepo;
    @Autowired
    private SetCommandRepo setCommandRepo;
    @Autowired
    private DatePointRepo datePointRepo;
    @Autowired
    private SetEcoRepo setEcoRepo;
    @Autowired
    private SetWindowRepo setWindowRepo;
    @Autowired
    private SetRecuperatorRepo setRecuperatorRepo;
    @Autowired
    private SetWaterFloorRepo setWaterFloorRepo;
    @Autowired
    private SetElectricFloorRepo setElectricFloorRepo;
    @Autowired
    private SetOutsideConditionsRepo setOutsideConditionsRepo;
    @Autowired
    private SetSilenceRepo setSilenceRepo;
    @Autowired
    private SetNightRepo setNightRepo;
    @Autowired
    private SetEnableEcoRadiatorRepo setEnableEcoRadiatorRepo;
    @Autowired
    private SetEnableEcoConditionerRepo setEnableEcoConditionerRepo;
    @Autowired
    private SetEnableEcoRecuperatorRepo setEnableEcoRecuperatorRepo;
    @Autowired
    private SetEnableEcoWaterFloorRepo setEnableEcoWaterFloorRepo;
    @Autowired
    private SetEnableEcoElectricFloorRepo setEnableEcoElectricFloorRepo;

    @Autowired
    private SimpMessagingTemplate template;

    private final static String CONTROLLER_IP = "10.132.83.152";

    private final TcpParameters tcpParameters;
    private final ModbusMaster master;
    private boolean buttonFlag = false;
    private Integer[] checkRegs = new Integer[13];

    private RautControllerDriver() {
        tcpParameters = new TcpParameters();
        tcpParameters.setKeepAlive(true);
        tcpParameters.setPort(Modbus.TCP_PORT);
        try {
            tcpParameters.setHost(InetAddress.getByName(CONTROLLER_IP));
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        Modbus.setAutoIncrementTransactionId(true);
        master = ModbusMasterFactory.createModbusMasterTCP(tcpParameters);
        master.setResponseTimeout(1000);
    }

    private boolean changes(int[] first, Integer[] second){
        for(int i =2; i < 13; i++){
            if(first[i] != second[i])
                return true;
        }
        return false;
    }

    @Scheduled(fixedDelay = 1000L)
    public void poll() {
        LocalDateTime now = LocalDateTime.now();
        try {

            if (!master.isConnected()) {
                master.connect();
            }

            //need to add more registers

            int[] regs = master.readHoldingRegisters(1, 0, 26);

            Connection c = new Connection(Parameter.RAUT_CONNECTION, now, true);
            ConnectionDto cDto = new ConnectionDto(c);
            template.convertAndSend("/topic/connection", cDto);
            Connection prevC = connectionRepo.findFirstByParamIsOrderByTimeDesc(Parameter.RAUT_CONNECTION);
            if (prevC == null || Connection.isModuled(prevC, c)) {
                connectionRepo.save(c);
            }

            for (int i = 2; i < 13; i++){
                checkRegs[i] = regs[i];
            }

            float t1Val = (float) regs[0] / 10;
            Temperature t1 = new Temperature(Parameter.TEMPERATURE, now, t1Val);
            TemperatureDto t1Dto = new TemperatureDto(t1);
            template.convertAndSend("/topic/temperature", t1Dto);
            Temperature prevT1 = tempRepo.findFirstByParamIsOrderByTimeDesc(Parameter.TEMPERATURE);
            if (prevT1 == null || Temperature.isModuled(t1, prevT1)) {
                tempRepo.save(t1);
            }

            float humVal = (float) regs[1] / 10;
            humVal = humVal + 20F;
            Humidity h = new Humidity(Parameter.HUMIDITY, now, humVal);
            HumidityDto hDto = new HumidityDto(h);
            template.convertAndSend("/topic/humidity", hDto);
            Humidity prevHum = humidityRepo.findFirstByParamIsOrderByTimeDesc(Parameter.HUMIDITY);
            if (prevHum == null || Humidity.isModuled(h, prevHum)) {
                humidityRepo.save(h);
            }

            float setPowerVal = (float) regs[2];
            SetPower setPower = new SetPower(Parameter.SET_POWER, now, setPowerVal);
            SetPowerDto setPowerDto = new SetPowerDto(setPower);
            template.convertAndSend("/topic/set_power", setPowerDto);
            SetPower prevSetPower = setPowerRepo.findFirstByParamIsOrderByTimeDesc(Parameter.SET_POWER);
            if (prevSetPower == null || SetPower.isModuled(setPower, prevSetPower)) {
                setPowerRepo.save(setPower);
            }

            float setTemperatureVal = (float) regs[3];
            SetTemperature st = new SetTemperature(Parameter.SET_TEMPERATURE, now, setTemperatureVal);
            SetTemperatureDto stDto = new SetTemperatureDto(st);
            template.convertAndSend("/topic/set_temperature", stDto);
            SetTemperature prevSt = setTemperatureRepo.findFirstByParamIsOrderByTimeDesc(Parameter.SET_TEMPERATURE);
            if (prevSt == null || SetTemperature.isModuled(st, prevSt)) {
                setTemperatureRepo.save(st);
            }

            float setConditionerModeVal = (float) regs[4];
            SetConditionerMode setConditionerMode = new SetConditionerMode(Parameter.SET_CONDITIONER_MODE, now, setConditionerModeVal);
            SetConditionerModeDto setConditionerModeDto = new SetConditionerModeDto(setConditionerMode);
            template.convertAndSend("/topic/set_conditioner_mode", setConditionerModeDto);
            SetConditionerMode prevSetConditionerMode = setConditionerModeRepo.findFirstByParamIsOrderByTimeDesc(Parameter.SET_CONDITIONER_MODE);
            setConditionerModeRepo.save(setConditionerMode);

            float setFanSpeedVal = (float) regs[5];
            SetFanSpeed setFanSpeed = new SetFanSpeed(Parameter.SET_FAN_SPEED, now, setFanSpeedVal);
            SetFanSpeedDto setFanSpeedDto = new SetFanSpeedDto(setFanSpeed);
            template.convertAndSend("/topic/set_fan_speed", setFanSpeedDto);
            SetFanSpeed prevSetFanSpeed = setFanSpeedRepo.findFirstByParamIsOrderByTimeDesc(Parameter.SET_FAN_SPEED);
            setFanSpeedRepo.save(setFanSpeed);

            float setFlugerVal = (float) regs[6];
            SetFluger setFluger = new SetFluger(Parameter.SET_FLUGER, now, setFlugerVal);
            SetFlugerDto setFlugerDto = new SetFlugerDto(setFluger);
            template.convertAndSend("/topic/set_fluger", setFlugerDto);
            SetFluger prevSetFluger = setFlugerRepo.findFirstByParamIsOrderByTimeDesc(Parameter.SET_FLUGER);
            setFlugerRepo.save(setFluger);

            float setCustomVal = (float) regs[7];
            SetCustom setCustom = new SetCustom(Parameter.SET_CUSTOM, now, setCustomVal);
            SetCustomDto setCustomDto = new SetCustomDto(setCustom);
            template.convertAndSend("/topic/set_custom", setCustomDto);
            SetCustom prevSetCustom = setCustomRepo.findFirstByParamIsOrderByTimeDesc(Parameter.SET_CUSTOM);
            setCustomRepo.save(setCustom);

            float setSettingVal = (float) regs[8];
            SetSetting setSetting = new SetSetting(Parameter.SET_SETTING, now, setSettingVal);
            SetSettingDto setSettingDto = new SetSettingDto(setSetting);
            template.convertAndSend("/topic/set_setting", setSettingDto);
            SetSetting prevSetSetting = setSettingRepo.findFirstByParamIsOrderByTimeDesc(Parameter.SET_SETTING);
            setSettingRepo.save(setSetting);

            float setClimateModeVal = (float) regs[9];
            SetClimateMode setClimateMode = new SetClimateMode(Parameter.SET_CLIMATE_MODE, now, setClimateModeVal);
            SetClimateModeDto setClimateModeDto = new SetClimateModeDto(setClimateMode);
            template.convertAndSend("/topic/set_climate_mode", setClimateModeDto);
            SetClimateMode prevSetClimateMode = setClimateModeRepo.findFirstByParamIsOrderByTimeDesc(Parameter.SET_CLIMATE_MODE);
            setClimateModeRepo.save(setClimateMode);

            float setSeasonVal = (float) regs[10];
            SetSeason setSeason = new SetSeason(Parameter.SET_SEASON, now, setSeasonVal);
            SetSeasonDto setSeasonDto = new SetSeasonDto(setSeason);
            template.convertAndSend("/topic/set_season", setSeasonDto);
            SetSeason prevSetSeason = setSeasonRepo.findFirstByParamIsOrderByTimeDesc(Parameter.SET_SEASON);
            setSeasonRepo.save(setSeason);

            float setRadiatorVal = (float) regs[11];
            SetRadiator setRadiator = new SetRadiator(Parameter.SET_RADIATOR, now, setRadiatorVal);
            SetRadiatorDto setRadiatorDto = new SetRadiatorDto(setRadiator);
            template.convertAndSend("/topic/set_radiator", setRadiatorDto);
            SetRadiator prevSetRadiator = setRadiatorRepo.findFirstByParamIsOrderByTimeDesc(Parameter.SET_RADIATOR);
            setRadiatorRepo.save(setRadiator);

            float setHumidifierVal = (float) regs[12];
            SetHumidifier setHumidifier = new SetHumidifier(Parameter.SET_HUMIDIFIER, now, setHumidifierVal);
            SetHumidifierDto setHumidifierDto = new SetHumidifierDto(setHumidifier);
            template.convertAndSend("/topic/set_humidifier", setHumidifierDto);
            SetHumidifier prevSetHumidifier = setHumidifierRepo.findFirstByParamIsOrderByTimeDesc(Parameter.SET_HUMIDIFIER);
            setHumidifierRepo.save(setHumidifier);

            float setCommandVal = (float) regs[2];
            SetCommand setCommand = new SetCommand(Parameter.SET_COMMAND, now, setCommandVal);
            setCommand.setCurrentHumidity((float) regs[0]);
            setCommand.setCurrentTemperature((float) regs[1]);
            setCommand.setConditionerPower(regs[2]);
            setCommand.setConditionerTemperature(regs[3]);
            setCommand.setConditionerMode(regs[4]);
            setCommand.setConditionerFanSpeed(regs[5]);
            setCommand.setConditionerFluger(regs[6]);
            setCommand.setConditionerCustom(regs[7]);
            setCommand.setConditionerSetting(regs[8]);
            setCommand.setClimateMode(regs[9]);
            setCommand.setClimateSeason(regs[10]);
            setCommand.setRadiator(regs[11]);
            setCommand.setHumidifier(regs[12]);
            SetCommandDto setCommandDto = new SetCommandDto(setCommand);
            template.convertAndSend("/topic/set_command", setCommandDto);
            SetCommand prevSetCommand = setCommandRepo.findFirstByParamIsOrderByTimeDesc(Parameter.SET_COMMAND);
            if(prevSetCommand == null || changes(regs, prevSetCommand.getRegisters()) || buttonFlag){
                setCommandRepo.save(setCommand);
                buttonFlag = false;
            }

            float setEcoVal = (float) regs[13];
            SetEco setEco = new SetEco(Parameter.ECO, now, setEcoVal);
            SetEcoDto setEcoDto = new SetEcoDto(setEco);
            template.convertAndSend("/topic/setEco", setEcoDto);
            SetEco prevSetEco = setEcoRepo.findFirstByParamIsOrderByTimeDesc(Parameter.ECO);
            setEcoRepo.save(setEco);

            float setWindowVal = (float) regs[14];
            SetWindow setWindow = new SetWindow(Parameter.SET_WINDOW, now, setWindowVal);
            SetWindowDto setWindowDto = new SetWindowDto(setWindow);
            template.convertAndSend("/topic/setWindow", setWindowDto);
            SetWindow prevSetWindow = setWindowRepo.findFirstByParamIsOrderByTimeDesc(Parameter.SET_WINDOW);
            setWindowRepo.save(setWindow);

            float setRecuperatorVal = (float) regs[15];
            SetRecuperator setRecuperator = new SetRecuperator(Parameter.SET_RECUPERATOR, now, setRecuperatorVal);
            SetRecuperatorDto setRecuperatorDto = new SetRecuperatorDto(setRecuperator);
            template.convertAndSend("/topic/setRecuperator", setRecuperatorDto);
            SetRecuperator prevSetRecuperator= setRecuperatorRepo.findFirstByParamIsOrderByTimeDesc(Parameter.SET_RECUPERATOR);
            setRecuperatorRepo.save(setRecuperator);

            float setWaterFloorVal = (float) regs[16];
            SetWaterFloor setWaterFloor = new SetWaterFloor(Parameter.SET_WATER_FLOOR, now, setWaterFloorVal);
            SetWaterFloorDto setWaterFloorDto = new SetWaterFloorDto(setWaterFloor);
            template.convertAndSend("/topic/setWaterFloor", setWaterFloorDto);
            SetWaterFloor prevSetWaterFloor = setWaterFloorRepo.findFirstByParamIsOrderByTimeDesc(Parameter.SET_WATER_FLOOR);
            setWaterFloorRepo.save(setWaterFloor);

            float setElectricFloorVal = (float) regs[17];
            SetElectricFloor setElectricFloor = new SetElectricFloor(Parameter.SET_ELECTRIC_FLOOR, now, setElectricFloorVal);
            SetElectricFloorDto setElectricFloorDto = new SetElectricFloorDto(setElectricFloor);
            template.convertAndSend("/topic/setElectricFloor", setElectricFloorDto);
            SetElectricFloor prevSetElectricFloor = setElectricFloorRepo.findFirstByParamIsOrderByTimeDesc(Parameter.SET_ELECTRIC_FLOOR);
            setElectricFloorRepo.save(setElectricFloor);

            float setOutsideConditionsVal = (float) regs[18];
            SetOutsideConditions setOutsideConditions = new SetOutsideConditions(Parameter.SET_OUTSIDE_CONDITIONS, now, setOutsideConditionsVal);
            SetOutsideConditionsDto setOutsideConditionsDto = new SetOutsideConditionsDto(setOutsideConditions);
            template.convertAndSend("/topic/setOutsideConditions", setOutsideConditionsDto);
            SetOutsideConditions prevSetOutsideConditions = setOutsideConditionsRepo.findFirstByParamIsOrderByTimeDesc(Parameter.SET_OUTSIDE_CONDITIONS);
            setOutsideConditionsRepo.save(setOutsideConditions);

            float setSilenceVal = (float) regs[19];
            SetSilence setSilence = new SetSilence(Parameter.SET_SILENCE, now, setSilenceVal);
            SetSilenceDto setSilenceDto = new SetSilenceDto(setSilence);
            template.convertAndSend("/topic/setSilence", setSilenceDto);
            SetSilence prevSetSilence = setSilenceRepo.findFirstByParamIsOrderByTimeDesc(Parameter.SET_SILENCE);
            setSilenceRepo.save(setSilence);

            float setNightVal = (float) regs[20];
            SetNight setNight = new SetNight(Parameter.SET_NIGHT, now, setNightVal);
            SetNightDto setNightDto = new SetNightDto(setNight);
            template.convertAndSend("/topic/setNight", setNightDto);
            SetNight prevSetNight = setNightRepo.findFirstByParamIsOrderByTimeDesc(Parameter.SET_NIGHT);
            setNightRepo.save(setNight);

            float setEnableEcoRadiatorVal = (float) regs[21];
            SetEnableEcoRadiator setEnableEcoRadiator = new SetEnableEcoRadiator(Parameter.SET_ENABLE_ECO_RADIATOR, now, setEnableEcoRadiatorVal);
            SetEnableEcoRadiatorDto setEnableEcoRadiatorDto = new SetEnableEcoRadiatorDto(setEnableEcoRadiator);
            template.convertAndSend("/topic/SetEnableEcoRadiator", setEnableEcoRadiatorDto);
            SetEnableEcoRadiator prevSetEnableEcoRadiator = setEnableEcoRadiatorRepo.findFirstByParamIsOrderByTimeDesc(Parameter.SET_ENABLE_ECO_RADIATOR);
            setEnableEcoRadiatorRepo.save(setEnableEcoRadiator);

            float setEnableEcoConditionerVal = (float) regs[22];
            SetEnableEcoConditioner setEnableEcoConditioner = new SetEnableEcoConditioner(Parameter.SET_ENABLE_ECO_CONDITIONER, now, setEnableEcoConditionerVal);
            SetEnableEcoConditionerDto setEnableEcoConditionerDto = new SetEnableEcoConditionerDto(setEnableEcoConditioner);
            template.convertAndSend("/topic/SetEnableEcoConditioner", setEnableEcoConditionerDto);
            SetEnableEcoConditioner prevSetEnableEcoConditioner = setEnableEcoConditionerRepo.findFirstByParamIsOrderByTimeDesc(Parameter.SET_ENABLE_ECO_CONDITIONER);
            setEnableEcoConditionerRepo.save(setEnableEcoConditioner);

            float setEnableEcoRecuperatorVal = (float) regs[23];
            SetEnableEcoRecuperator setEnableEcoRecuperator = new SetEnableEcoRecuperator(Parameter.SET_ENABLE_ECO_RECUPERATOR, now, setEnableEcoRecuperatorVal);
            SetEnableEcoRecuperatorDto setEnableEcoRecuperatorDto = new SetEnableEcoRecuperatorDto(setEnableEcoRecuperator);
            template.convertAndSend("/topic/SetEnableEcoRecuperator", setEnableEcoRecuperatorDto);
            SetEnableEcoRecuperator prevSetEnableEcoRecuperator = setEnableEcoRecuperatorRepo.findFirstByParamIsOrderByTimeDesc(Parameter.SET_ENABLE_ECO_RECUPERATOR);
            setEnableEcoRecuperatorRepo.save(setEnableEcoRecuperator);

            float setEnableEcoWaterFloorVal = (float) regs[24];
            SetEnableEcoWaterFloor setEnableEcoWaterFloor = new SetEnableEcoWaterFloor(Parameter.SET_ENABLE_ECO_WATER_FLOOR, now, setEnableEcoWaterFloorVal);
            SetEnableEcoWaterFloorDto setEnableEcoWaterFloorDto = new SetEnableEcoWaterFloorDto(setEnableEcoWaterFloor);
            template.convertAndSend("/topic/SetEnableEcoWaterFloor", setEnableEcoWaterFloorDto);
            SetEnableEcoWaterFloor prevSetEnableEcoWaterFloor = setEnableEcoWaterFloorRepo.findFirstByParamIsOrderByTimeDesc(Parameter.SET_ENABLE_ECO_WATER_FLOOR);
            setEnableEcoWaterFloorRepo.save(setEnableEcoWaterFloor);

            float setEnableEcoElectricFloorVal = (float) regs[25];
            SetEnableEcoElectricFloor setEnableEcoElectricFloor = new SetEnableEcoElectricFloor(Parameter.SET_ENABLE_ECO_ELECTRIC_FLOOR, now, setEnableEcoElectricFloorVal);
            SetEnableEcoElectricFloorDto setEnableEcoElectricFloorDto = new SetEnableEcoElectricFloorDto(setEnableEcoElectricFloor);
            template.convertAndSend("/topic/SetEnableEcoElectricFloor", setEnableEcoElectricFloorDto);
            SetEnableEcoElectricFloor prevSetEnableEcoElectricFloor = setEnableEcoElectricFloorRepo.findFirstByParamIsOrderByTimeDesc(Parameter.SET_ENABLE_ECO_ELECTRIC_FLOOR);
            setEnableEcoElectricFloorRepo.save(setEnableEcoElectricFloor);
            
//            DatePoint datePoint = new DatePoint(Parameter.DATE_POINT, now, now);
//            datePoint.setConditionerPower(1);
//            datePoint.setConditionerTemperature(20);
//
//           DatePoint prevDataPoint = datePointRepo.findFirstByParamIsOrderByTimeDesc(Parameter.DATE_POINT);
//            if (prevDataPoint == null) {
//                datePointRepo.save(datePoint);
//            }

            LinkedList<DatePoint> datePoints = datePointRepo.findAllByParam(Parameter.DATE_POINT);
            LinkedList<Long> deleteList = new LinkedList<Long>();

            if(datePoints != null){
                for (DatePoint d: datePoints) {
                    if(LocalDateTime.now().isAfter(d.getDate())) {
                        master.writeMultipleRegisters(1, 2, ArrayUtils.toPrimitive(d.getRegisters()));
                        deleteList.add(d.getId());
                    }
                }

                for (Long del: deleteList) {
                    datePointRepo.deleteById(del);
                }
            }

        } catch (ModbusProtocolException | ModbusNumberException | ModbusIOException e) {
            Connection c = new Connection(Parameter.RAUT_CONNECTION, now, false);
            ConnectionDto cDto = new ConnectionDto(c);
            template.convertAndSend("/topic/connection", cDto);
            Connection prevC = connectionRepo.findFirstByParamIsOrderByTimeDesc(Parameter.RAUT_CONNECTION);
            if (prevC == null || Connection.isModuled(prevC, c)) {
                connectionRepo.save(c);
            }
            System.err.println(e);
        }
    }

    @MessageMapping("/setPower/onPower")
    @SendTo("/topic/set_power")
    public SetPowerDto incPowerSetPower(SetPowerDto setPowerDto) throws Exception {
        setPowerDto.setTime(LocalDateTime.now());
        setPowerDto.setValue(1F);
        master.writeSingleRegister(1, 2, (int) setPowerDto.getValue().floatValue());
        SetPower stPower = new SetPower(Parameter.SET_POWER, setPowerDto.getTime(), setPowerDto.getValue());
        setPowerRepo.save(stPower);
        return setPowerDto;
    }

    @MessageMapping("/setPower/offPower")
    @SendTo("/topic/set_power")
    public SetPowerDto decPowerSetPower(SetPowerDto setPowerDto) throws Exception {
        setPowerDto.setTime(LocalDateTime.now());
        setPowerDto.setValue(0F);
        master.writeSingleRegister(1, 2, (int) setPowerDto.getValue().floatValue());
        SetPower stPower = new SetPower(Parameter.SET_POWER, setPowerDto.getTime(), setPowerDto.getValue());
        setPowerRepo.save(stPower);
        return setPowerDto;
    }

    @MessageMapping("/setTemperature/inc")
    @SendTo("/topic/set_temperature")
    public SetTemperatureDto incSetTemperature(SetTemperatureDto stDto) throws Exception {
        stDto.setTime(LocalDateTime.now());
        stDto.setValue(SetTemperature.inBorder(stDto.getValue() + 1F));
        master.writeSingleRegister(1, 3, (int) stDto.getValue().floatValue());
        SetTemperature st = new SetTemperature(Parameter.SET_TEMPERATURE, LocalDateTime.now(), stDto.getValue());
        setTemperatureRepo.save(st);
        return stDto;
    }

    @MessageMapping("/setTemperature/dec")
    @SendTo("/topic/set_temperature")
    public SetTemperatureDto decSetTemperature(SetTemperatureDto stDto) throws Exception {
        stDto.setTime(LocalDateTime.now());
        stDto.setValue(SetTemperature.inBorder(stDto.getValue() - 1F));
        master.writeSingleRegister(1, 3, (int) stDto.getValue().floatValue());
        SetTemperature st = new SetTemperature(Parameter.SET_TEMPERATURE, stDto.getTime(), stDto.getValue());
        setTemperatureRepo.save(st);
        return stDto;
    }

    @MessageMapping("/setConditionerMode/incConditionerMode")
    @SendTo("/topic/set_conditioner_mode")
    public SetConditionerModeDto incSetConditionerMode(SetConditionerModeDto setConditionerModeDto) throws Exception {
        setConditionerModeDto.setTime(LocalDateTime.now());
        setConditionerModeDto.setValue(SetConditionerMode.inBorder(setConditionerModeDto.getValue().floatValue() + 1F));
        master.writeSingleRegister(1, 4, (int) setConditionerModeDto.getValue().floatValue());
        SetConditionerMode setConditionerMode = new SetConditionerMode(Parameter.SET_CONDITIONER_MODE, setConditionerModeDto.getTime(), setConditionerModeDto.getValue());
        setConditionerModeRepo.save(setConditionerMode);
        return setConditionerModeDto;
    }

    @MessageMapping("/setConditionerMode/decConditionerMode")
    @SendTo("/topic/set_conditioner_mode")
    public SetConditionerModeDto decSetConditionerMode(SetConditionerModeDto setConditionerModeDto) throws Exception {
        setConditionerModeDto.setTime(LocalDateTime.now());
        setConditionerModeDto.setValue(SetConditionerMode.inBorder(setConditionerModeDto.getValue().floatValue() - 1F));
        master.writeSingleRegister(1, 4, (int) setConditionerModeDto.getValue().floatValue());
        SetConditionerMode setConditionerMode = new SetConditionerMode(Parameter.SET_CONDITIONER_MODE, setConditionerModeDto.getTime(), setConditionerModeDto.getValue());
        setConditionerModeRepo.save(setConditionerMode);
        return setConditionerModeDto;
    }

    @MessageMapping("/setFanSpeed/incFanSpeed")
    @SendTo("/topic/set_fan_speed")
    public SetFanSpeedDto incSetFanSpeed(SetFanSpeedDto setFanSpeedDto) throws Exception {
        setFanSpeedDto.setTime(LocalDateTime.now());
        setFanSpeedDto.setValue(SetFanSpeed.inBorder(setFanSpeedDto.getValue().floatValue() + 1F));
        master.writeSingleRegister(1, 5, (int) setFanSpeedDto.getValue().floatValue());
        SetFanSpeed setFanSpeed = new SetFanSpeed(Parameter.SET_FAN_SPEED, setFanSpeedDto.getTime(), setFanSpeedDto.getValue());
        setFanSpeedRepo.save(setFanSpeed);
        return setFanSpeedDto;
    }

    @MessageMapping("/setFanSpeed/decFanSpeed")
    @SendTo("/topic/set_fan_speed")
    public SetFanSpeedDto decSetFanSpeed(SetFanSpeedDto setFanSpeedDto) throws Exception {
        setFanSpeedDto.setTime(LocalDateTime.now());
        setFanSpeedDto.setValue(SetFanSpeed.inBorder(setFanSpeedDto.getValue().floatValue() - 1F));
        master.writeSingleRegister(1, 5, (int) setFanSpeedDto.getValue().floatValue());
        SetFanSpeed setFanSpeed = new SetFanSpeed(Parameter.SET_FAN_SPEED, setFanSpeedDto.getTime(), setFanSpeedDto.getValue());
        setFanSpeedRepo.save(setFanSpeed);
        return setFanSpeedDto;
    }

    @MessageMapping("/setFluger/incFluger")
    @SendTo("/topic/set_fluger")
    public SetFlugerDto incSetFluger(SetFlugerDto setFlugerDto) throws Exception {
        setFlugerDto.setTime(LocalDateTime.now());
        setFlugerDto.setValue(SetFluger.inBorder(setFlugerDto.getValue().floatValue() + 1F));
        master.writeSingleRegister(1, 6, (int) setFlugerDto.getValue().floatValue());
        SetFluger setFluger = new SetFluger(Parameter.SET_FLUGER, setFlugerDto.getTime(), setFlugerDto.getValue());
        setFlugerRepo.save(setFluger);
        return setFlugerDto;
    }

    @MessageMapping("/setFluger/decFluger")
    @SendTo("/topic/set_fluger")
    public SetFlugerDto decSetFluger(SetFlugerDto setFlugerDto) throws Exception {
        setFlugerDto.setTime(LocalDateTime.now());
        setFlugerDto.setValue(SetFluger.inBorder(setFlugerDto.getValue().floatValue() - 1F));
        master.writeSingleRegister(1, 6, (int) setFlugerDto.getValue().floatValue());
        SetFluger setFluger = new SetFluger(Parameter.SET_FLUGER, setFlugerDto.getTime(), setFlugerDto.getValue());
        setFlugerRepo.save(setFluger);
        return setFlugerDto;
    }

    @MessageMapping("/setCustom/incCustom")
    @SendTo("/topic/set_custom")
    public SetCustomDto incSetCustom(SetCustomDto setCustomDto) throws Exception{
        setCustomDto.setTime(LocalDateTime.now());
        setCustomDto.setValue(SetCustom.inBorder(setCustomDto.getValue().floatValue() + 1F));
        master.writeSingleRegister(1, 7, (int) setCustomDto.getValue().floatValue());
        SetCustom setCustom = new SetCustom(Parameter.SET_CUSTOM, setCustomDto.getTime(), setCustomDto.getValue());
        setCustomRepo.save(setCustom);
        return  setCustomDto;
    }

    @MessageMapping("/setCustom/decCustom")
    @SendTo("/topic/set_custom")
    public SetCustomDto decSetCustom(SetCustomDto setCustomDto) throws Exception{
        setCustomDto.setTime(LocalDateTime.now());
        setCustomDto.setValue(SetCustom.inBorder(setCustomDto.getValue().floatValue() - 1F));
        master.writeSingleRegister(1, 7, (int) setCustomDto.getValue().floatValue());
        SetCustom setCustom = new SetCustom(Parameter.SET_CUSTOM, setCustomDto.getTime(), setCustomDto.getValue());
        setCustomRepo.save(setCustom);
        return  setCustomDto;
    }

    @MessageMapping("/setSetting/incSetting")
    @SendTo("/topic/set_setting")
    public SetSettingDto incSetSetting(SetSettingDto setSettingDto) throws Exception{
        setSettingDto.setTime(LocalDateTime.now());
        setSettingDto.setValue(SetSetting.inBorder(setSettingDto.getValue().floatValue() + 1F));
        master.writeSingleRegister(1, 8, (int) setSettingDto.getValue().floatValue());
        SetSetting setSetting = new SetSetting(Parameter.SET_SETTING, setSettingDto.getTime(), setSettingDto.getValue());
        setSettingRepo.save(setSetting);
        return setSettingDto;
    }

    @MessageMapping("/setSetting/decSetting")
    @SendTo("/topic/set_setting")
    public SetSettingDto decSetSetting(SetSettingDto setSettingDto) throws Exception{
        setSettingDto.setTime(LocalDateTime.now());
        setSettingDto.setValue(SetSetting.inBorder(setSettingDto.getValue().floatValue() - 1F));
        master.writeSingleRegister(1, 8, (int) setSettingDto.getValue().floatValue());
        SetSetting setSetting = new SetSetting(Parameter.SET_SETTING, setSettingDto.getTime(), setSettingDto.getValue());
        setSettingRepo.save(setSetting);
        return setSettingDto;
    }

    @MessageMapping("/setClimateMode/incClimateMode")
    @SendTo("/topic/set_climate_mode")
    public SetClimateModeDto incSetClimateMode(SetClimateModeDto setClimateModeDto) throws Exception{
        setClimateModeDto.setTime(LocalDateTime.now());
        setClimateModeDto.setValue(SetClimateMode.inBorder(setClimateModeDto.getValue().floatValue() + 1F));
        master.writeSingleRegister(1, 9, (int) setClimateModeDto.getValue().floatValue());
        SetClimateMode setClimateMode = new SetClimateMode(Parameter.SET_CLIMATE_MODE, setClimateModeDto.getTime(), setClimateModeDto.getValue());
        setClimateModeRepo.save(setClimateMode);
        return setClimateModeDto;
    }

    @MessageMapping("/setClimateMode/decClimateMode")
    @SendTo("/topic/set_climate_mode")
    public SetClimateModeDto decSetClimateMode(SetClimateModeDto setClimateModeDto) throws Exception{
        setClimateModeDto.setTime(LocalDateTime.now());
        setClimateModeDto.setValue(SetClimateMode.inBorder(setClimateModeDto.getValue().floatValue() - 1F));
        master.writeSingleRegister(1, 9, (int) setClimateModeDto.getValue().floatValue());
        SetClimateMode setClimateMode = new SetClimateMode(Parameter.SET_CLIMATE_MODE, setClimateModeDto.getTime(), setClimateModeDto.getValue());
        setClimateModeRepo.save(setClimateMode);
        return setClimateModeDto;
    }

    @MessageMapping("/setSeason/incSeason")
    @SendTo("/topic/set_season")
    public SetSeasonDto incSetSeason(SetSeasonDto setSeasonDto) throws Exception{
        setSeasonDto.setTime(LocalDateTime.now());
        setSeasonDto.setValue(SetSeason.inBorder(setSeasonDto.getValue().floatValue() + 1F));
        master.writeSingleRegister(1, 10, (int) setSeasonDto.getValue().floatValue());
        SetSeason setSeason = new SetSeason(Parameter.SET_SEASON, setSeasonDto.getTime(), setSeasonDto.getValue());
        setSeasonRepo.save(setSeason);
        return setSeasonDto;
    }

    @MessageMapping("/setSeason/decSeason")
    @SendTo("/topic/set_season")
    public SetSeasonDto decSetSeason(SetSeasonDto setSeasonDto) throws Exception{
        setSeasonDto.setTime(LocalDateTime.now());
        setSeasonDto.setValue(SetSeason.inBorder(setSeasonDto.getValue().floatValue() - 1F));
        master.writeSingleRegister(1, 10, (int) setSeasonDto.getValue().floatValue());
        SetSeason setSeason = new SetSeason(Parameter.SET_SEASON, setSeasonDto.getTime(), setSeasonDto.getValue());
        setSeasonRepo.save(setSeason);
        return setSeasonDto;
    }

    @MessageMapping("/setRadiator/incRadiator")
    @SendTo("/topic/set_radiator")
    public SetRadiatorDto incSetRadiator(SetRadiatorDto setRadiatorDto) throws Exception{
        setRadiatorDto.setTime(LocalDateTime.now());
        setRadiatorDto.setValue(SetRadiator.inBorder(setRadiatorDto.getValue().floatValue() + 1F));
        master.writeSingleRegister(1, 11, (int) setRadiatorDto.getValue().floatValue());
        SetRadiator setRadiator = new SetRadiator(Parameter.SET_RADIATOR, setRadiatorDto.getTime() ,setRadiatorDto.getValue());
        setRadiatorRepo.save(setRadiator);
        return setRadiatorDto;
    }

    @MessageMapping("/setRadiator/decRadiator")
    @SendTo("/topic/set_radiator")
    public SetRadiatorDto decSetRadiator(SetRadiatorDto setRadiatorDto) throws Exception{
        setRadiatorDto.setTime(LocalDateTime.now());
        setRadiatorDto.setValue(SetRadiator.inBorder(setRadiatorDto.getValue().floatValue() - 1F));
        master.writeSingleRegister(1, 11, (int) setRadiatorDto.getValue().floatValue());
        SetRadiator setRadiator = new SetRadiator(Parameter.SET_RADIATOR, setRadiatorDto.getTime() ,setRadiatorDto.getValue());
        setRadiatorRepo.save(setRadiator);
        return setRadiatorDto;
    }

    @MessageMapping("/setHumidifier/incHumidifier")
    @SendTo("/topic/set_humidifier")
    public SetHumidifierDto incSetHumidifier (SetHumidifierDto setHumidifierDto) throws Exception{
        setHumidifierDto.setTime(LocalDateTime.now());
        setHumidifierDto.setValue(SetHumidifier.inBorder(setHumidifierDto.getValue().floatValue() + 1F));
        master.writeSingleRegister(1, 12, (int) setHumidifierDto.getValue().floatValue());
        SetHumidifier setHumidifier = new SetHumidifier(Parameter.SET_HUMIDIFIER, setHumidifierDto.getTime(), setHumidifierDto.getValue());
        setHumidifierRepo.save(setHumidifier);
        return  setHumidifierDto;
    }

    @MessageMapping("/setHumidifier/decHumidifier")
    @SendTo("/topic/set_humidifier")
    public SetHumidifierDto decSetHumidifier (SetHumidifierDto setHumidifierDto) throws Exception{
        setHumidifierDto.setTime(LocalDateTime.now());
        setHumidifierDto.setValue(SetHumidifier.inBorder(setHumidifierDto.getValue().floatValue() - 1F));
        master.writeSingleRegister(1, 12, (int) setHumidifierDto.getValue().floatValue());
        SetHumidifier setHumidifier = new SetHumidifier(Parameter.SET_HUMIDIFIER, setHumidifierDto.getTime(), setHumidifierDto.getValue());
        setHumidifierRepo.save(setHumidifier);
        return  setHumidifierDto;
    }

    @MessageMapping("/setCommand/sendCommand")
    @SendTo("/topic/set_command")
    public SetCommandDto sendSetCommand (SetCommandDto setCommandDto) throws Exception{
        setCommandDto.setTime(LocalDateTime.now());
        setCommandDto.setValue(setCommandDto.getValue().floatValue());
        master.writeSingleRegister(1, 2, (int) setCommandDto.getValue().floatValue());
        SetCommand setCommand = new SetCommand(Parameter.SET_COMMAND, setCommandDto.getTime(), setCommandDto.getValue());
        //setCommandRepo.save(setCommand);
        buttonFlag = true;

        return setCommandDto;
    }

    @MessageMapping("/setEco/incEco")
    @SendTo("/topic/set_eco")
    public SetEcoDto incSetEco (SetEcoDto setEcoDto) throws  Exception{
        setEcoDto.setTime(LocalDateTime.now());
        setEcoDto.setValue(SetEco.inBorder(setEcoDto.getValue().floatValue() + 1F));
        master.writeSingleRegister(1,13, (int) setEcoDto.getValue().floatValue());
        SetEco setEco = new SetEco(Parameter.ECO, setEcoDto.getTime(), setEcoDto.getValue());
        setEcoRepo.save(setEco);
        return setEcoDto;
    }

    @MessageMapping("/setEco/decEco")
    @SendTo("/topic/set_eco")
    public SetEcoDto decSetEco (SetEcoDto setEcoDto) throws  Exception{
        setEcoDto.setTime(LocalDateTime.now());
        setEcoDto.setValue(SetEco.inBorder(setEcoDto.getValue().floatValue() - 1F));
        master.writeSingleRegister(1,13, (int) setEcoDto.getValue().floatValue());
        SetEco setEco = new SetEco(Parameter.ECO, setEcoDto.getTime(), setEcoDto.getValue());
        setEcoRepo.save(setEco);
        return setEcoDto;
    }

    @MessageMapping("/setWindow/incWindow")
    @SendTo("/topic/set_window")
    public SetWindowDto incSetWindow (SetWindowDto setWindowDto) throws  Exception{
        setWindowDto.setTime(LocalDateTime.now());
        setWindowDto.setValue(SetWindow.inBorder(setWindowDto.getValue().floatValue() + 1F));
        master.writeSingleRegister(1,14, (int) setWindowDto.getValue().floatValue());
        SetWindow setWindow = new SetWindow(Parameter.SET_WINDOW, setWindowDto.getTime(), setWindowDto.getValue());
        setWindowRepo.save(setWindow);
        return setWindowDto;
    }

    @MessageMapping("/setWindow/decWindow")
    @SendTo("/topic/set_window")
    public SetWindowDto decSetWindow (SetWindowDto setWindowDto) throws  Exception{
        setWindowDto.setTime(LocalDateTime.now());
        setWindowDto.setValue(SetWindow.inBorder(setWindowDto.getValue().floatValue() - 1F));
        master.writeSingleRegister(1,14, (int) setWindowDto.getValue().floatValue());
        SetWindow setWindow = new SetWindow(Parameter.SET_WINDOW, setWindowDto.getTime(), setWindowDto.getValue());
        setWindowRepo.save(setWindow);
        return setWindowDto;
    }

    @MessageMapping("/setRecuperator//incRecuperator")
    @SendTo("/topic/set_recuperator")
    public SetRecuperatorDto incSetRecuperator (SetRecuperatorDto setRecuperatorDto) throws  Exception{
        setRecuperatorDto.setTime(LocalDateTime.now());
        setRecuperatorDto.setValue(SetRecuperator.inBorder(setRecuperatorDto.getValue().floatValue() + 1F));
        master.writeSingleRegister(1,15, (int) setRecuperatorDto.getValue().floatValue());
        SetRecuperator setRecuperator = new SetRecuperator(Parameter.SET_RECUPERATOR, setRecuperatorDto.getTime(), setRecuperatorDto.getValue());
        setRecuperatorRepo.save(setRecuperator);
        return setRecuperatorDto;
    }

    @MessageMapping("/setRecuperator/decRecuperator")
    @SendTo("/topic/set_recuperator")
    public SetRecuperatorDto decSetRecuperator (SetRecuperatorDto setRecuperatorDto) throws  Exception{
        setRecuperatorDto.setTime(LocalDateTime.now());
        setRecuperatorDto.setValue(SetRecuperator.inBorder(setRecuperatorDto.getValue().floatValue() - 1F));
        master.writeSingleRegister(1,15, (int) setRecuperatorDto.getValue().floatValue());
        SetRecuperator setRecuperator = new SetRecuperator(Parameter.SET_RECUPERATOR, setRecuperatorDto.getTime(), setRecuperatorDto.getValue());
        setRecuperatorRepo.save(setRecuperator);
        return setRecuperatorDto;
    }

    @MessageMapping("/setWaterFloor/incWaterFloor")
    @SendTo("/topic/set_water_floor")
    public SetWaterFloorDto incSetWaterFloor(SetWaterFloorDto setWaterFloorDto) throws Exception{
        setWaterFloorDto.setTime(LocalDateTime.now());
        setWaterFloorDto.setValue(SetWaterFloor.inBorder(setWaterFloorDto.getValue().floatValue() + 1F));
        master.writeSingleRegister(1, 16, (int) setWaterFloorDto.getValue().floatValue());
        SetWaterFloor setWaterFloor = new SetWaterFloor(Parameter.SET_WATER_FLOOR, setWaterFloorDto.getTime(), setWaterFloorDto.getValue());
        setWaterFloorRepo.save(setWaterFloor);
        return setWaterFloorDto;
    }

    @MessageMapping("/setWaterFloor/decWaterFloor")
    @SendTo("/topic/set_water_floor")
    public SetWaterFloorDto decSetWaterFloor(SetWaterFloorDto setWaterFloorDto) throws Exception{
        setWaterFloorDto.setTime(LocalDateTime.now());
        setWaterFloorDto.setValue(SetWaterFloor.inBorder(setWaterFloorDto.getValue().floatValue() - 1F));
        master.writeSingleRegister(1, 16, (int) setWaterFloorDto.getValue().floatValue());
        SetWaterFloor setWaterFloor = new SetWaterFloor(Parameter.SET_WATER_FLOOR, setWaterFloorDto.getTime(), setWaterFloorDto.getValue());
        setWaterFloorRepo.save(setWaterFloor);
        return setWaterFloorDto;
    }

    @MessageMapping("/setElectricFloor/incElectricFloor")
    @SendTo("/topic/set_electric_floor")
    public SetElectricFloorDto incSetElectricFloor(SetElectricFloorDto setElectricFloorDto) throws Exception{
        setElectricFloorDto.setTime(LocalDateTime.now());
        setElectricFloorDto.setValue(SetElectricFloor.inBorder(setElectricFloorDto.getValue().floatValue() + 1F));
        master.writeSingleRegister(1, 17, (int) setElectricFloorDto.getValue().floatValue());
        SetElectricFloor setElectricFloor = new SetElectricFloor(Parameter.SET_ELECTRIC_FLOOR, setElectricFloorDto.getTime(), setElectricFloorDto.getValue());
        setElectricFloorRepo.save(setElectricFloor);
        return setElectricFloorDto;
    }

    @MessageMapping("/setElectricFloor/decElectricFloor")
    @SendTo("/topic/set_electric_floor")
    public SetElectricFloorDto decSetElectricFloor(SetElectricFloorDto setElectricFloorDto) throws Exception{
        setElectricFloorDto.setTime(LocalDateTime.now());
        setElectricFloorDto.setValue(SetElectricFloor.inBorder(setElectricFloorDto.getValue().floatValue() - 1F));
        master.writeSingleRegister(1, 17, (int) setElectricFloorDto.getValue().floatValue());
        SetElectricFloor setElectricFloor = new SetElectricFloor(Parameter.SET_ELECTRIC_FLOOR, setElectricFloorDto.getTime(), setElectricFloorDto.getValue());
        setElectricFloorRepo.save(setElectricFloor);
        return setElectricFloorDto;
    }

    @MessageMapping("/setOutsideConditions/incOutsideConditions")
    @SendTo("/topic/set_outside_conditions")
    public  SetOutsideConditionsDto incSetOutsideConditions(SetOutsideConditionsDto setOutsideConditionsDto) throws  Exception{
        setOutsideConditionsDto.setTime(LocalDateTime.now());
        setOutsideConditionsDto.setValue(SetOutsideConditions.inBorder(setOutsideConditionsDto.getValue().floatValue() + 1F));
        master.writeSingleRegister(1, 18, (int) setOutsideConditionsDto.getValue().floatValue());
        SetOutsideConditions setOutsideConditions = new SetOutsideConditions(Parameter.SET_OUTSIDE_CONDITIONS, setOutsideConditionsDto.getTime(), setOutsideConditionsDto.getValue());
        setOutsideConditionsRepo.save(setOutsideConditions);
        return setOutsideConditionsDto;
    }

    @MessageMapping("/setOutsideConditions/decOutsideConditions")
    @SendTo("/topic/set_outside_conditions")
    public  SetOutsideConditionsDto decSetOutsideConditions(SetOutsideConditionsDto setOutsideConditionsDto) throws  Exception{
        setOutsideConditionsDto.setTime(LocalDateTime.now());
        setOutsideConditionsDto.setValue(SetOutsideConditions.inBorder(setOutsideConditionsDto.getValue().floatValue() - 1F));
        master.writeSingleRegister(1, 18, (int) setOutsideConditionsDto.getValue().floatValue());
        SetOutsideConditions setOutsideConditions = new SetOutsideConditions(Parameter.SET_OUTSIDE_CONDITIONS, setOutsideConditionsDto.getTime(), setOutsideConditionsDto.getValue());
        setOutsideConditionsRepo.save(setOutsideConditions);
        return setOutsideConditionsDto;
    }

    @MessageMapping("/setSilence/incSilence")
    @SendTo("/topic/set_silence")
    public SetSilenceDto incSetSilence (SetSilenceDto setSilenceDto) throws Exception{
        setSilenceDto.setTime(LocalDateTime.now());
        setSilenceDto.setValue(SetSilence.inBorder(setSilenceDto.getValue().floatValue() + 1F));
        master.writeSingleRegister(1, 19, (int) setSilenceDto.getValue().floatValue());
        SetSilence setSilence = new SetSilence(Parameter.SET_SILENCE, setSilenceDto.getTime(), setSilenceDto.getValue());
        setSilenceRepo.save(setSilence);
        return setSilenceDto;
    }

    @MessageMapping("/setSilence/decSilence")
    @SendTo("/topic/set_silence")
    public SetSilenceDto decSetSilence (SetSilenceDto setSilenceDto) throws Exception{
        setSilenceDto.setTime(LocalDateTime.now());
        setSilenceDto.setValue(SetSilence.inBorder(setSilenceDto.getValue().floatValue() - 1F));
        master.writeSingleRegister(1, 19, (int) setSilenceDto.getValue().floatValue());
        SetSilence setSilence = new SetSilence(Parameter.SET_SILENCE, setSilenceDto.getTime(), setSilenceDto.getValue());
        setSilenceRepo.save(setSilence);
        return setSilenceDto;
    }

    @MessageMapping("/setNight/incNight")
    @SendTo("/topic/set_night")
    public SetNightDto incSetNight (SetNightDto setNightDto) throws Exception{
        setNightDto.setTime(LocalDateTime.now());
        setNightDto.setValue(SetNight.inBorder(setNightDto.getValue().floatValue() + 1F));
        master.writeSingleRegister(1, 20, (int) setNightDto.getValue().floatValue());
        SetNight setNight = new SetNight(Parameter.SET_NIGHT, setNightDto.getTime(), setNightDto.getValue());
        setNightRepo.save(setNight);
        return setNightDto;
    }

    @MessageMapping("/setNight/decNight")
    @SendTo("/topic/set_night")
    public SetNightDto decSetNight (SetNightDto setNightDto) throws Exception{
        setNightDto.setTime(LocalDateTime.now());
        setNightDto.setValue(SetNight.inBorder(setNightDto.getValue().floatValue() - 1F));
        master.writeSingleRegister(1, 20, (int) setNightDto.getValue().floatValue());
        SetNight setNight = new SetNight(Parameter.SET_NIGHT, setNightDto.getTime(), setNightDto.getValue());
        setNightRepo.save(setNight);
        return setNightDto;
    }

    @MessageMapping("/SetEnableEcoRadiator/incEnableEcoRadiator")
    @SendTo("/topic/set_enable_eco_radiator")
    public SetEnableEcoRadiatorDto incSetEnableEcoRadiator (SetEnableEcoRadiatorDto setEnableEcoRadiatorDto) throws Exception{
        setEnableEcoRadiatorDto.setTime(LocalDateTime.now());
        setEnableEcoRadiatorDto.setValue(SetEnableEcoRadiator.inBorder(setEnableEcoRadiatorDto.getValue().floatValue() + 1F));
        master.writeSingleRegister(1, 21, (int) setEnableEcoRadiatorDto.getValue().floatValue());
        SetEnableEcoRadiator setEnableEcoRadiator = new SetEnableEcoRadiator(Parameter.SET_ENABLE_ECO_RADIATOR, setEnableEcoRadiatorDto.getTime(), setEnableEcoRadiatorDto.getValue());
        setEnableEcoRadiatorRepo.save(setEnableEcoRadiator);
        return setEnableEcoRadiatorDto;
    }

    @MessageMapping("/SetEnableEcoRadiator/decEnableEcoRadiator")
    @SendTo("/topic/set_enable_eco_radiator")
    public SetEnableEcoRadiatorDto decSetEnableEcoRadiator (SetEnableEcoRadiatorDto setEnableEcoRadiatorDto) throws Exception{
        setEnableEcoRadiatorDto.setTime(LocalDateTime.now());
        setEnableEcoRadiatorDto.setValue(SetEnableEcoRadiator.inBorder(setEnableEcoRadiatorDto.getValue().floatValue() - 1F));
        master.writeSingleRegister(1, 21, (int) setEnableEcoRadiatorDto.getValue().floatValue());
        SetEnableEcoRadiator setEnableEcoRadiator = new SetEnableEcoRadiator(Parameter.SET_ENABLE_ECO_RADIATOR, setEnableEcoRadiatorDto.getTime(), setEnableEcoRadiatorDto.getValue());
        setEnableEcoRadiatorRepo.save(setEnableEcoRadiator);
        return setEnableEcoRadiatorDto;
    }

    @MessageMapping("/SetEnableEcoConditioner/incEnableEcoConditioner")
    @SendTo("/topic/set_enable_eco_conditioner")
    public  SetEnableEcoConditionerDto incSetEnableEcoConditioner (SetEnableEcoConditionerDto setEnableEcoConditionerDto) throws Exception{
        setEnableEcoConditionerDto.setTime(LocalDateTime.now());
        setEnableEcoConditionerDto.setValue(SetEnableEcoConditioner.inBorder(setEnableEcoConditionerDto.getValue().floatValue() + 1F));
        master.writeSingleRegister(1, 22, (int) setEnableEcoConditionerDto.getValue().floatValue());
        SetEnableEcoConditioner setEnableEcoConditioner = new SetEnableEcoConditioner(Parameter.SET_ENABLE_ECO_CONDITIONER, setEnableEcoConditionerDto.getTime(), setEnableEcoConditionerDto.getValue());
        setEnableEcoConditionerRepo.save(setEnableEcoConditioner);
        return setEnableEcoConditionerDto;
    }

    @MessageMapping("/SetEnableEcoConditioner/decEnableEcoConditioner")
    @SendTo("/topic/set_enable_eco_conditioner")
    public  SetEnableEcoConditionerDto decSetEnableEcoConditioner (SetEnableEcoConditionerDto setEnableEcoConditionerDto) throws Exception{
        setEnableEcoConditionerDto.setTime(LocalDateTime.now());
        setEnableEcoConditionerDto.setValue(SetEnableEcoConditioner.inBorder(setEnableEcoConditionerDto.getValue().floatValue() - 1F));
        master.writeSingleRegister(1, 22, (int) setEnableEcoConditionerDto.getValue().floatValue());
        SetEnableEcoConditioner setEnableEcoConditioner = new SetEnableEcoConditioner(Parameter.SET_ENABLE_ECO_CONDITIONER, setEnableEcoConditionerDto.getTime(), setEnableEcoConditionerDto.getValue());
        setEnableEcoConditionerRepo.save(setEnableEcoConditioner);
        return setEnableEcoConditionerDto;
    }

    @MessageMapping("/SetEnableEcoRecuperator/incEnableEcoRecuperator")
    @SendTo("/topic/set_enable_eco_recuperator")
    public  SetEnableEcoRecuperatorDto incSetEnableEcoRecuperator (SetEnableEcoRecuperatorDto setEnableEcoRecuperatorDto) throws Exception{
        setEnableEcoRecuperatorDto.setTime(LocalDateTime.now());
        setEnableEcoRecuperatorDto.setValue(SetEnableEcoRecuperator.inBorder(setEnableEcoRecuperatorDto.getValue().floatValue() + 1F));
        master.writeSingleRegister(1, 23, (int) setEnableEcoRecuperatorDto.getValue().floatValue());
        SetEnableEcoRecuperator setEnableEcoRecuperator = new SetEnableEcoRecuperator(Parameter.SET_ENABLE_ECO_RECUPERATOR, setEnableEcoRecuperatorDto.getTime(), setEnableEcoRecuperatorDto.getValue());
        setEnableEcoRecuperatorRepo.save(setEnableEcoRecuperator);
        return setEnableEcoRecuperatorDto;
    }

    @MessageMapping("/SetEnableEcoRecuperator/decEnableEcoRecuperator")
    @SendTo("/topic/set_enable_eco_recuperator")
    public  SetEnableEcoRecuperatorDto decSetEnableEcoRecuperator (SetEnableEcoRecuperatorDto setEnableEcoRecuperatorDto) throws Exception{
        setEnableEcoRecuperatorDto.setTime(LocalDateTime.now());
        setEnableEcoRecuperatorDto.setValue(SetEnableEcoRecuperator.inBorder(setEnableEcoRecuperatorDto.getValue().floatValue() - 1F));
        master.writeSingleRegister(1, 23, (int) setEnableEcoRecuperatorDto.getValue().floatValue());
        SetEnableEcoRecuperator setEnableEcoRecuperator = new SetEnableEcoRecuperator(Parameter.SET_ENABLE_ECO_RECUPERATOR, setEnableEcoRecuperatorDto.getTime(), setEnableEcoRecuperatorDto.getValue());
        setEnableEcoRecuperatorRepo.save(setEnableEcoRecuperator);
        return setEnableEcoRecuperatorDto;
    }

    @MessageMapping("/SetEnableEcoWaterFloor/incEnableEcoWaterFloor")
    @SendTo("/topic/set_enable_eco_water_floor")
    public  SetEnableEcoWaterFloorDto incSetEnableEcoWaterFloor (SetEnableEcoWaterFloorDto setEnableEcoWaterFloorDto) throws Exception{
        setEnableEcoWaterFloorDto.setTime(LocalDateTime.now());
        setEnableEcoWaterFloorDto.setValue(SetEnableEcoWaterFloor.inBorder(setEnableEcoWaterFloorDto.getValue().floatValue() + 1F));
        master.writeSingleRegister(1, 24, (int) setEnableEcoWaterFloorDto.getValue().floatValue());
        SetEnableEcoWaterFloor setEnableEcoWaterFloor = new SetEnableEcoWaterFloor(Parameter.SET_ENABLE_ECO_WATER_FLOOR, setEnableEcoWaterFloorDto.getTime(), setEnableEcoWaterFloorDto.getValue());
        setEnableEcoWaterFloorRepo.save(setEnableEcoWaterFloor);
        return setEnableEcoWaterFloorDto;
    }

    @MessageMapping("/SetEnableEcoWaterFloor/decEnableEcoWaterFloor")
    @SendTo("/topic/set_enable_eco_water_floor")
    public  SetEnableEcoWaterFloorDto decSetEnableEcoWaterFloor (SetEnableEcoWaterFloorDto setEnableEcoWaterFloorDto) throws Exception{
        setEnableEcoWaterFloorDto.setTime(LocalDateTime.now());
        setEnableEcoWaterFloorDto.setValue(SetEnableEcoWaterFloor.inBorder(setEnableEcoWaterFloorDto.getValue().floatValue() - 1F));
        master.writeSingleRegister(1, 24, (int) setEnableEcoWaterFloorDto.getValue().floatValue());
        SetEnableEcoWaterFloor setEnableEcoWaterFloor = new SetEnableEcoWaterFloor(Parameter.SET_ENABLE_ECO_WATER_FLOOR, setEnableEcoWaterFloorDto.getTime(), setEnableEcoWaterFloorDto.getValue());
        setEnableEcoWaterFloorRepo.save(setEnableEcoWaterFloor);
        return setEnableEcoWaterFloorDto;
    }

    @MessageMapping("/SetEnableEcoElectricFloor/incEnableEcoElectricFloor")
    @SendTo("/topic/set_enable_eco_electric_floor")
    public  SetEnableEcoElectricFloorDto incSetEnableEcoElectricFloor (SetEnableEcoElectricFloorDto setEnableEcoElectricFloorDto) throws Exception{
        setEnableEcoElectricFloorDto.setTime(LocalDateTime.now());
        setEnableEcoElectricFloorDto.setValue(SetEnableEcoElectricFloor.inBorder(setEnableEcoElectricFloorDto.getValue().floatValue() + 1F));
        master.writeSingleRegister(1, 25, (int) setEnableEcoElectricFloorDto.getValue().floatValue());
        SetEnableEcoElectricFloor setEnableEcoElectricFloor = new SetEnableEcoElectricFloor(Parameter.SET_ENABLE_ECO_ELECTRIC_FLOOR, setEnableEcoElectricFloorDto.getTime(), setEnableEcoElectricFloorDto.getValue());
        setEnableEcoElectricFloorRepo.save(setEnableEcoElectricFloor);
        return setEnableEcoElectricFloorDto;
    }

    @MessageMapping("/SetEnableEcoElectricFloor/decEnableEcoElectricFloor")
    @SendTo("/topic/set_enable_eco_electric_floor")
    public  SetEnableEcoElectricFloorDto decSetEnableEcoElectricFloor (SetEnableEcoElectricFloorDto setEnableEcoElectricFloorDto) throws Exception{
        setEnableEcoElectricFloorDto.setTime(LocalDateTime.now());
        setEnableEcoElectricFloorDto.setValue(SetEnableEcoElectricFloor.inBorder(setEnableEcoElectricFloorDto.getValue().floatValue() - 1F));
        master.writeSingleRegister(1, 25, (int) setEnableEcoElectricFloorDto.getValue().floatValue());
        SetEnableEcoElectricFloor setEnableEcoElectricFloor = new SetEnableEcoElectricFloor(Parameter.SET_ENABLE_ECO_ELECTRIC_FLOOR, setEnableEcoElectricFloorDto.getTime(), setEnableEcoElectricFloorDto.getValue());
        setEnableEcoElectricFloorRepo.save(setEnableEcoElectricFloor);
        return setEnableEcoElectricFloorDto;
    }
}
