package com.ingroinfo.mm.service;

import java.util.List;

import com.ingroinfo.mm.dto.DivisionSubdivisionDto;

public interface DivisionSubdivisionService {

	//save Data
	DivisionSubdivisionDto saveDivisionSubdivision(DivisionSubdivisionDto divSubDto);
	
	//Find All Data
	List<DivisionSubdivisionDto> findAllDivSubdiv();
	
	//Delete
	void deleteDivSubDiv(Long divsubId);
	
	//Get Distinct Divisions
	List<String> getDistinctDivisions();

	//Get SubDivisionList By Division
	List<DivisionSubdivisionDto> getSubDivisionListByDivision(String division);
	
}
