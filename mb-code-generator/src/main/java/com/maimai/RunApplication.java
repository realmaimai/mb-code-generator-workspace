package com.maimai;

import com.maimai.bean.TableInfo;
import com.maimai.builders.BuildPO;
import com.maimai.builders.BuildTable;

import java.util.List;

public class RunApplication {
    public static void main(String[] args) {
        List<TableInfo> tableInfoList = BuildTable.getTables();
        for (TableInfo tableInfo : tableInfoList) {
            BuildPO.execute(tableInfo);
        }
    }
}
