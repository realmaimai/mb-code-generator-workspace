package com.maimai;

import com.maimai.bean.TableInfo;
import com.maimai.builders.BuildTable;

import java.util.List;

public class RunApplication {
    public static void main(String[] args) {
        List<TableInfo> tables = BuildTable.getTables();
    }
}
