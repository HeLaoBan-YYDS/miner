package com.ruoyi.system.domain;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Data
@Getter
@Setter
public class BlockData {

    private List<Block> blocklist;
}
