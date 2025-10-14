package com.ruoyi.system.domain;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Data
@Getter
@Setter
public class Block {
    private int height;
    private String block_hash;
    private double base_reward;
    private double total_reward;
    private long timestamp;
    private long settle_timestamp;
    private List<Block> blocklist;
}
