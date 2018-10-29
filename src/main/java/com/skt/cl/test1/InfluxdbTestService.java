package com.skt.cl.test1;

import lombok.extern.slf4j.Slf4j;
import org.influxdb.InfluxDB;
import org.influxdb.InfluxDBFactory;
import org.influxdb.dto.BatchPoints;
import org.influxdb.dto.Point;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class InfluxdbTestService {


    public void createTest1(){

        SystemUtils systemUtils = new SystemUtils();
        ArrayList<InfluxdbTestDto> list = new ArrayList<>();
        String getCount = "1000000";

        int count = Integer.valueOf(getCount);
        Random randomGenerator = new Random();

        for(int i=0; i<count; i++){

            int rHost   = i%10;
            int rIdc    = i%3;
            int rSvc    = i%5;

            long ibps = randomGenerator.nextLong();
            long obps = randomGenerator.nextLong();

            long ipps = randomGenerator.nextLong();
            long opps = randomGenerator.nextLong();

            long ierr = randomGenerator.nextLong();
            long oerr = randomGenerator.nextLong();

            InfluxdbTestDto data = new InfluxdbTestDto();
            data.setHostname("skt"+rHost);
            data.setIdc("idc"+rIdc);
            data.setService("service"+rSvc);
            data.setInbps(ibps);
            data.setOutbps(obps);
            data.setInpps(ipps);
            data.setOutpps(opps);
            data.setInerror(ierr);
            data.setOuterror(oerr);

            list.add(data);
        }

        log.debug(">>>>> Start Influxdb Insert Test!!");

        ArrayList<ArrayList<InfluxdbTestDto>> sList = systemUtils.arraySplit(list,10000);

        ExecutorService concurrentService = Executors.newFixedThreadPool(sList.size());
        for(ArrayList<InfluxdbTestDto> tdata: sList){
            concurrentService.submit(new InfluxdbTestThread(tdata));
        }
        concurrentService.shutdown();
    }

    /**
     *
     * @return
     */
    public InfluxDB getInfluxDbConnection() {
        InfluxDB influxDB = InfluxDBFactory.connect("http://127.0.0.1:8086", "root", "root");
        return influxDB;
    }

    /**
     *
     * @param list
     */
    @Async
    public void insertTest(ArrayList<InfluxdbTestDto> list){
        InfluxDB influxDB = this.getInfluxDbConnection();

        BatchPoints batchPoints = BatchPoints
                .database("test1")
                .tag("case", "test")
                .retentionPolicy("1day")
                .consistency(InfluxDB.ConsistencyLevel.ALL)
                .build();

        for( InfluxdbTestDto data : list){
            Point point = Point.measurement("stress")
                    .time(System.currentTimeMillis(), TimeUnit.MILLISECONDS)
                    .tag("hostname", data.getHostname())
                    .tag("idc", data.getIdc())
                    .tag("service", data.getService())
                    .addField("ibps", data.getInbps())
                    .addField("obps", data.getOutbps())
                    .addField("ipps", data.getInpps())
                    .addField("opps", data.getOutpps())
                    .addField("ierror", data.getInerror())
                    .addField("oerror", data.getOutpps())
                    .build();
            batchPoints.point(point);
        }
        influxDB.write(batchPoints);

        log.debug(">>>>> Async Insert Job End");
    }
}
