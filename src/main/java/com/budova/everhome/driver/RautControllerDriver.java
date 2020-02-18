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
    private SimpMessagingTemplate template;

    private final static String CONTROLLER_IP = "192.168.1.152";

    private final TcpParameters tcpParameters;
    private final ModbusMaster master;

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
            if(prevC == null || Connection.isModuled(prevC, c)) {
                connectionRepo.save(c);
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
            if(prevHum == null || Humidity.isModuled(h, prevHum)){
                humidityRepo.save(h);
            }

            float setPowerVal = (float) regs[2];
            SetPower setPower = new SetPower(Parameter.SET_POWER, now, setPowerVal);
            SetPowerDto setPowerDto = new SetPowerDto(setPower);
            template.convertAndSend("/topic/set_power", setPowerDto);
            SetPower prevSetPower = setPowerRepo.findFirstByParamIsOrderByTimeDesc(Parameter.SET_POWER);
            if(prevSetPower == null || SetPower.isModuled(setPower, prevSetPower)){
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
            SetConditionerMode prevSetConditionerMode = setConditionerModeRepo.findFirstByParamIsOrderByTimeDesc(Parameter.SET_CONDITIONER_MODE);             //TODO возможны ошибки при работе
            setConditionerModeRepo.save(setConditionerMode);

        } catch (ModbusProtocolException | ModbusNumberException | ModbusIOException e) {
            Connection c = new Connection(Parameter.RAUT_CONNECTION, now, false);
            ConnectionDto cDto = new ConnectionDto(c);
            template.convertAndSend("/topic/connection", cDto);
            Connection prevC = connectionRepo.findFirstByParamIsOrderByTimeDesc(Parameter.RAUT_CONNECTION);
            if(prevC == null || Connection.isModuled(prevC, c)) {
                connectionRepo.save(c);
            }
            System.err.println(e);        }
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
}
