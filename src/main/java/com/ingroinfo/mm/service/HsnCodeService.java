package com.ingroinfo.mm.service;

import java.util.List;
import com.ingroinfo.mm.dto.HsnCodeDto;

public interface HsnCodeService {

	//create
	HsnCodeDto saveHsnCode(HsnCodeDto hsnCodeDto);
	
	//findAll Data
	List<HsnCodeDto> findAllHsnCode();
	
	//Delete
	void deleteHsnCode(Long hsnCodeId);

	//get HsnCode By Category
	HsnCodeDto getHsnCodeByCategory(String category);
}
