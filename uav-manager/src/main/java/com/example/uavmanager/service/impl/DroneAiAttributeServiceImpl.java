package com.example.uavmanager.service.impl;

import com.example.uavmanager.domain.DroneAiAttributes;
import com.example.uavmanager.service.DroneAiAttributeService;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Random;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;

@Service
public class DroneAiAttributeServiceImpl implements DroneAiAttributeService {

    @Override
    public DroneAiAttributes generate(String name, String model, String serialNo) {
        long seed = stableSeed(name, model, serialNo);
        Random random = new Random(seed);

        int maxSpeedKmh = 30 + random.nextInt(121); // 30..150
        int flightTimeMin = 10 + random.nextInt(111); // 10..120

        double payloadKg = 0.2 + (random.nextDouble() * 9.8); // 0.2..10.0
        payloadKg = BigDecimal.valueOf(payloadKg).setScale(2, RoundingMode.HALF_UP).doubleValue();

        int batteryMah = 1000 + (random.nextInt(14001)); // 1000..15000
        int aiScore = 40 + random.nextInt(61); // 40..100

        return new DroneAiAttributes(maxSpeedKmh, flightTimeMin, payloadKg, batteryMah, aiScore);
    }

    @SneakyThrows
    private long stableSeed(String name, String model, String serialNo) {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        String raw = String.valueOf(name) + "|" + model + "|" + serialNo;
        byte[] bytes = digest.digest(raw.getBytes(StandardCharsets.UTF_8));
        long seed = 0L;
        for (int i = 0; i < 8; i++) {
            seed = (seed << 8) | (bytes[i] & 0xffL);
        }
        return seed;
    }
}

