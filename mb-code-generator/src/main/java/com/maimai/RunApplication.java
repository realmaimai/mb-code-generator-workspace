package com.maimai;

import com.maimai.bean.TableInfo;
import com.maimai.builders.*;

import java.util.List;

public class RunApplication {
    public static void main(String[] args) {
        List<TableInfo> tableInfoList = BuildTable.getTables();

        BuildBaseClass.execute();

        for (TableInfo tableInfo : tableInfoList) {
            BuildPO.execute(tableInfo);

            BuildQuery.execute(tableInfo);

            BuildMapper.execute(tableInfo);

            BuildMapperXML.execute(tableInfo);

            BuildService.execute(tableInfo);
        }
    }
}
