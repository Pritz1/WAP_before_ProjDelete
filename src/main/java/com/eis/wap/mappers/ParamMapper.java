package com.eis.wap.mappers;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.eis.wap.model.Parameters;

import org.springframework.jdbc.core.RowMapper;


public class ParamMapper implements RowMapper<Parameters>{
	
	public Parameters mapRow(ResultSet resultSet, int arg1) throws SQLException {
		Parameters param = new Parameters();
		//param.setParam_id(resultSet.getInt(1));
		param.setParam_name(resultSet.getString(2));
		//param.setWeightage(resultSet.getFloat(3));
		//param.setAdd_date(resultSet.getDate(4));
		param.setAddedBy(resultSet.getString(5));
        return param;
    }
}
