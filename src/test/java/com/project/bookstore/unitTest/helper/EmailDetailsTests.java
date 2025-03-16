package com.project.bookstore.unitTest.helper;

import com.project.bookstore.helper.EmailDetails;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class EmailDetailsTests {
    private EmailDetails emailDetails;

    @BeforeEach
    public void SetUp() {
        emailDetails = new EmailDetails("testEmail@gmail.com", "testSubject", "testMsgBody");
    }

    @Test
    public void givenNothing_GetRecipient_ReturnRecipient() {
        String recipient = emailDetails.getRecipient();

        Assertions.assertThat(recipient).isEqualTo(emailDetails.getRecipient());
    }

    @Test
    public void givenRecipient_SetRecipient_ReturnNotNull() {
        emailDetails.setRecipient("newRecipient");

        Assertions.assertThat(emailDetails.getRecipient()).isNotNull();
    }

    @Test
    public void givenNothing_GetMsgBody_ReturnMsgBody() {
        String msgBody = emailDetails.getMsgBody();

        Assertions.assertThat(msgBody).isEqualTo(emailDetails.getMsgBody());
    }

    @Test
    public void givenMsgBody_SetMsgBody_ReturnNotNull() {
        emailDetails.setMsgBody("newMsgBody");

        Assertions.assertThat(emailDetails.getMsgBody()).isNotNull();
    }

    @Test
    public void givenNothing_GetSubject_ReturnSubject() {
        String subject = emailDetails.getSubject();

        Assertions.assertThat(subject).isEqualTo(emailDetails.getSubject());
    }

    @Test
    public void givenSubject_SetSubject_ReturnNotNull() {
        emailDetails.setSubject("newSubject@gmail.com");

        Assertions.assertThat(emailDetails.getSubject()).isNotNull();
    }
}
