package cn.sliew.http.stream.dao.mapper.jst;

import cn.sliew.http.stream.dao.entity.jst.JstOrder;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

public interface JstOrderMapper extends BaseMapper<JstOrder> {

    JstOrder selectByOId(@Param("oId") Long oId);

    int insertSelective(JstOrder record);

    int updateByPrimaryKeySelective(JstOrder record);

}
