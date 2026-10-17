package com.peter.shortener.features.shared.dto;

import jakarta.servlet.http.HttpServletRequest;

import static com.peter.shortener.features.shared.constants.ClientConstants.IP_ADDRESS;
import static com.peter.shortener.features.shared.constants.ClientConstants.USER_AGENT;

public record ClientInfo(
        String ipAddress,
        String userAgent,
        String referrer
) {
    public static ClientInfo save(HttpServletRequest servletRequest){
        String agent = servletRequest.getHeader(USER_AGENT);
        String referrer = servletRequest.getHeader(org.springframework.http.HttpHeaders.REFERER);

        String ip = servletRequest.getHeader(IP_ADDRESS);
        if (ip == null || ip.isBlank()) {
            ip = servletRequest.getRemoteAddr();
        } else {
            ip = ip.split(",")[0].trim();
        }

        return new ClientInfo(ip, agent, referrer);
    }
}
