package com.skt.cl.test1;

import lombok.Data;

@Data
public class InfluxdbTestDto {
    private String hostname;
    private String idc;
    private String service;
    private long inbps;
    private long outbps;
    private long inpps;
    private long outpps;
    private long inerror;
    private long outerror;
}
