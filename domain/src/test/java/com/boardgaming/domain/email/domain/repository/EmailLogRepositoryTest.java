package com.boardgaming.domain.email.domain.repository;

import com.boardgaming.domain.email.domain.EmailLog;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.*;

@DataJpaTest
class EmailLogRepositoryTest {
    @Autowired
    private EmailLogRepository repository;

    @Test
    @DisplayName("저장 및 불러오기 테스트")
    void test1() throws ClassNotFoundException {
        //given
        EmailLog emailLog = EmailLog.builder()
            .sender("sender")
            .recipient("recipient")
            .subject("subject")
            .text("text")
            .build();

        //when
        repository.save(emailLog);
        EmailLog emailLog2 = repository.findById(emailLog.getId())
            .orElseThrow(RuntimeException::new);

        //then
        assertThat(emailLog2.getId()).isEqualTo(emailLog.getId());
        assertThat(emailLog2.getSender()).isEqualTo(emailLog.getSender());
        assertThat(emailLog2.getRecipient()).isEqualTo(emailLog.getRecipient());
        assertThat(emailLog2.getSubject()).isEqualTo(emailLog.getSubject());
        assertThat(emailLog2.getText()).isEqualTo(emailLog.getText());
    }
}