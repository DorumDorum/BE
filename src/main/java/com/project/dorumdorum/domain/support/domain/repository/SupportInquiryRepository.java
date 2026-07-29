package com.project.dorumdorum.domain.support.domain.repository;

import com.project.dorumdorum.domain.support.domain.entity.SupportInquiry;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SupportInquiryRepository extends JpaRepository<SupportInquiry, String> {
    List<SupportInquiry> findByUserNoAndDeletedAtIsNullOrderByCreatedAtDescInquiryNoDesc(String userNo);

    Optional<SupportInquiry> findByInquiryNoAndUserNoAndDeletedAtIsNull(String inquiryNo, String userNo);
}
