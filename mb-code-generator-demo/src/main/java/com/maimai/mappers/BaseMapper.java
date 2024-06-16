package com.maimai.mappers;

import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface BaseMapper<T, P> {
    Long insert(@Param("bean") T t);

    Long insertOrUpdate(@Param("bean") T t);

    Long insertBatch(@Param("list") List<T> list);

    Long insertOrUpdateBatch(@Param("list") List<T> list);

    List<T> selectList(@Param("query") P p);

    Long selectCount(@Param("query") P p);
}
