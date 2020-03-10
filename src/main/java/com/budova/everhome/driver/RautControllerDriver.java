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
    private SimpMessagingTemplate template;

    private final static String CONTROLLER_IP = "192.168.1.152";

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
        for(int i =2; i < first.length; i++){
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

            int[] regs = master.readHoldingRegisters(1, 0, 13);

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
            template.convertAndSend("/topic/setHumidifier", setHumidifierDto);
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

}
