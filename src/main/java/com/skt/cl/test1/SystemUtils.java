package com.skt.cl.test1;

import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component
public class SystemUtils {
    /**
     *
     * @param resList
     * @param count
     * @return
     */
    public ArrayList<ArrayList<InfluxdbTestDto>> arraySplit(ArrayList<InfluxdbTestDto> resList , int count){
        ArrayList<ArrayList<InfluxdbTestDto>> ret = new ArrayList<>();
        if (resList == null || count <1){
            return null;
        }
        int size = resList.size();
        if (size <= count) {
            ret.add(resList);
        } else {
            int pre = size / count;
            int last = size % count;
            for (int i = 0; i <pre; i++) {
                ArrayList<InfluxdbTestDto> itemList = new ArrayList<>();
                for (int j = 0; j <count; j++) {
                    itemList.add(resList.get(i * count + j));
                }
                ret.add(itemList);
            }
            if (last > 0) {
                ArrayList<InfluxdbTestDto> itemList = new ArrayList<>();
                for (int i = 0; i <last; i++) {
                    itemList.add(resList.get(pre * count + i));
                }
                ret.add(itemList);
            }
        }
        return ret;
    }
}
