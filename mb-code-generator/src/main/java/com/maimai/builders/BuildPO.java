package com.maimai.builders;

import com.maimai.bean.Constants;
import com.maimai.bean.TableInfo;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;

@Slf4j
public class BuildPO {
    public static void execute(TableInfo tableInfo) {
        File folder = new File(Constants.PATH_PO);
        if (!folder.exists()) {
            folder.mkdirs();
        }

        File file = new File(folder, tableInfo.getBeanName() + ".java");
        try {
            file.createNewFile();
        } catch (IOException e) {
            log.info("create po file error: " + e);
        }

    }
}
