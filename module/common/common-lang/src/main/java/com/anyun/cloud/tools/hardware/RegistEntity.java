/*
 *
 *      RegistEntity.java
 *      Copyright (C) <2015-?>  <twitchgg@yahoo.com>
 *
 *      This program is free software: you can redistribute it and/or modify
 *      it under the terms of the GNU General Public License as published by
 *      the Free Software Foundation, either version 3 of the License, or
 *      (at your option) any later version.
 *
 *      This program is distributed in the hope that it will be useful,
 *      but WITHOUT ANY WARRANTY; without even the implied warranty of
 *      MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *      GNU General Public License for more details.
 *
 *      You should have received a copy of the GNU General Public License
 *      along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */

package com.anyun.cloud.tools.hardware;

import java.util.List;

/**
 * Created by twitchgg on 10/27/15.
 */
public class RegistEntity {
    private String machineId;
    private List<CpuInfoEntity> cpuInfoEntities;
    private List<EthernetCardInfoEntity> cardInfoEntities;
    private List<MemoryInfoEntity> memoryInfoEntities;

    public String getMachineId() {
        return machineId;
    }

    public void setMachineId(String machineId) {
        this.machineId = machineId;
    }

    public List<CpuInfoEntity> getCpuInfoEntities() {
        return cpuInfoEntities;
    }

    public void setCpuInfoEntities(List<CpuInfoEntity> cpuInfoEntities) {
        this.cpuInfoEntities = cpuInfoEntities;
    }

    public List<EthernetCardInfoEntity> getCardInfoEntities() {
        return cardInfoEntities;
    }

    public void setCardInfoEntities(List<EthernetCardInfoEntity> cardInfoEntities) {
        this.cardInfoEntities = cardInfoEntities;
    }

    public List<MemoryInfoEntity> getMemoryInfoEntities() {
        return memoryInfoEntities;
    }

    public void setMemoryInfoEntities(List<MemoryInfoEntity> memoryInfoEntities) {
        this.memoryInfoEntities = memoryInfoEntities;
    }
}
