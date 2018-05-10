package com.anyun.cloud.dto;

import com.anyun.cloud.tools.db.AbstractEntity;

/**
 * Created by sxt on 16-8-4.
 */
public class HostMemoryInfoDto extends AbstractEntity {
    private   int   memoryLimit;
    private   int   memorySwapLimit;

    public int getMemoryLimit() {
        return memoryLimit;
    }

    public void setMemoryLimit(int memoryLimit) {
        this.memoryLimit = memoryLimit;
    }

    public int getMemorySwapLimit() {
        return memorySwapLimit;
    }

    public void setMemorySwapLimit(int memorySwapLimit) {
        this.memorySwapLimit = memorySwapLimit;
    }
}
