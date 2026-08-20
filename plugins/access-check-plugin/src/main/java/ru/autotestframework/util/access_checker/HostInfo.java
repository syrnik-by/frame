package ru.autotestframework.util.access_checker;

import lombok.Data;
import lombok.RequiredArgsConstructor;

/**
 * Класс для объекта json'а defaultHost.
 */
@Data
@RequiredArgsConstructor
public class HostInfo implements Comparable<HostInfo> {

    private final String standName;
    private final String hostAddress;
    private final String accessTag;

    /**
     * compares hostInfos
     * @param hostInfo the object to be compared.
     * @return
     */
    @Override
    public int compareTo(final HostInfo hostInfo) {
        return this.standName.compareTo(hostInfo.standName);
    }
}
