package com.alibaba.agentic.computer.use.utils;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class StringBasedLetterSnowflake {
    private static final char[] CHARS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();
    private static final int CHAR_LENGTH = CHARS.length;

    private final String datacenterId;
    private final int datacenterIdHash;
    private long sequence = 0L;
    private long lastTimestamp = -1L;

    private static final long sequenceBits = 12L;
    private static final long datacenterIdBits = 10L; // 增加到10位，以容纳更多可能的hash值

    private static final long maxDatacenterId = -1L ^ (-1L << datacenterIdBits);
    private static final long datacenterIdShift = sequenceBits;
    private static final long timestampLeftShift = sequenceBits + datacenterIdBits;

    private static final long sequenceMask = -1L ^ (-1L << sequenceBits);

    public StringBasedLetterSnowflake(String datacenterId) {
        this.datacenterId = datacenterId;
        this.datacenterIdHash = Math.abs(hashString(datacenterId)) & (int)maxDatacenterId;
    }

    private int hashString(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] hashBytes = md.digest(input.getBytes(StandardCharsets.UTF_8));
            return bytesToInt(hashBytes);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("MD5 algorithm not found", e);
        }
    }

    private int bytesToInt(byte[] bytes) {
        return ((bytes[0] & 0xFF) << 24) |
                ((bytes[1] & 0xFF) << 16) |
                ((bytes[2] & 0xFF) << 8)  |
                ((bytes[3] & 0xFF));
    }

    public synchronized String nextId() {
        long timestamp = timeGen();

        if (timestamp < lastTimestamp) {
            throw new RuntimeException("Clock moved backwards. Refusing to generate id");
        }

        if (lastTimestamp == timestamp) {
            sequence = (sequence + 1) & sequenceMask;
            if (sequence == 0) {
                timestamp = tilNextMillis(lastTimestamp);
            }
        } else {
            sequence = 0L;
        }

        lastTimestamp = timestamp;

        long id = ((timestamp - 1288834974657L) << timestampLeftShift) |
                (datacenterIdHash << datacenterIdShift) |
                sequence;

        return convertToLetters(id);
    }

    private String convertToLetters(long id) {
        StringBuilder sb = new StringBuilder();
        while (id > 0) {
            sb.append(CHARS[(int)(id % CHAR_LENGTH)]);
            id /= CHAR_LENGTH;
        }
        return sb.reverse().toString();
    }

    private long tilNextMillis(long lastTimestamp) {
        long timestamp = timeGen();
        while (timestamp <= lastTimestamp) {
            timestamp = timeGen();
        }
        return timestamp;
    }

    private long timeGen() {
        return System.currentTimeMillis();
    }

    public static void main(String[] args) {
        StringBasedLetterSnowflake idWorker = new StringBasedLetterSnowflake("my-datacenter");
        for (int i = 0; i < 10; i++) {
            String id = idWorker.nextId();
            System.out.println(id);
        }
    }
}