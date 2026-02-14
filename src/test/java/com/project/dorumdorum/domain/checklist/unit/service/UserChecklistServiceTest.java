package com.project.dorumdorum.domain.checklist.unit.service;

import com.project.dorumdorum.domain.checklist.domain.entity.UserChecklist;
import com.project.dorumdorum.domain.checklist.domain.repository.UserChecklistRepository;
import com.project.dorumdorum.domain.checklist.domain.service.UserChecklistService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("UserChecklistService Unit Tests")
class UserChecklistServiceTest {

    @Mock private UserChecklistRepository userChecklistRepository;
    @InjectMocks private UserChecklistService service;

    @Test
    void findByUserNo_WhenExists_ReturnsChecklist() {
        UserChecklist checklist = UserChecklist.builder().userNo("u1").otherNotes("note").build();
        when(userChecklistRepository.findByUserNo("u1")).thenReturn(Optional.of(checklist));

        UserChecklist result = service.findByUserNo("u1");

        assertThat(result).isEqualTo(checklist);
    }

    @Test
    void findByUserNo_WhenMissing_ReturnsDefaultChecklist() {
        when(userChecklistRepository.findByUserNo("u1")).thenReturn(Optional.empty());

        UserChecklist result = service.findByUserNo("u1");

        assertThat(result.getUserNo()).isEqualTo("u1");
        assertThat(result.getOtherNotes()).isEqualTo("");
    }

    @Test
    void save_ReturnsSavedChecklist() {
        UserChecklist checklist = UserChecklist.builder().userNo("u1").build();
        when(userChecklistRepository.save(checklist)).thenReturn(checklist);

        assertThat(service.save(checklist)).isEqualTo(checklist);
    }
}
