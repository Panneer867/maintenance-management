package com.ingroinfo.mm.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.ingroinfo.mm.entity.HoldWorkOrderLabours;

@Repository
public interface HoldWorkOrderLaboursRepository extends JpaRepository<HoldWorkOrderLabours, Long> {

}
