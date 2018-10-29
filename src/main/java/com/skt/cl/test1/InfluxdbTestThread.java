package com.skt.cl.test1;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

public class InfluxdbTestThread implements Callable<List<String>> {
    private ArrayList<InfluxdbTestDto> list;

    public InfluxdbTestThread(ArrayList<InfluxdbTestDto> list) {
            this.list = list;

    }

    /* (non-Javadoc)
     * @see java.util.concurrent.Callable#call()
     */
    public List<String> call() {
            List<String> stringList = new ArrayList<>(); // temp
            InfluxdbTestService service = new InfluxdbTestService();
            service.insertTest(this.list);
            return stringList;
        }

}
