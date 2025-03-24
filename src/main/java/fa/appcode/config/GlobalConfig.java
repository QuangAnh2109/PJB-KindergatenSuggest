package fa.appcode.config;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Component
@PropertySource("classpath:messages.properties")
@PropertySource("classpath:webconfig.properties")
@PropertySource("classpath:application.properties")
@ConfigurationProperties
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class GlobalConfig {
    @Value("${page.init}")
    private Integer initPage;
    @Value("${ME_001}")
    private String incorrectLogin;
    @Value("${ME_002}")
    private String validEmail;
    @Value("${ME_003}")
    private String requiredField;
    @Value("${ME_004}")
    private String sendResetPassword;
    @Value("${ME_005}")
    private String expiredLink;
    @Value("${ME_006}")
    private String emailNotExist;
    @Value("${ME_007}")
    private String passwordNotMatch;
    @Value("${ME_008}")
    private String validatePassword;
    @Value("${ME_009}")
    private String nullMessage;
    @Value("${ME_010}")
    private String saveSchoolSuccess;
    @Value("${ME_011}")
    private String submitSchool;
    @Value("${ME_012}")
    private String notFound;
    @Value("${ME_013}")
    private String enrollSuccess;
    @Value("${ME_014}")
    private String emailExist;
    @Value("${ME_015}")
    private String passwordResetSuccess;
    @Value("${ME_016}")
    private String verifyLinkSend;
    @Value("${ME_017}")
    private String anErrorOccur;
    @Value("${ME_018}")
    private String phoneIsNotValid;
    @Value("${ME_019}")
    private String phoneIsExist;
    @Value("${ME_020}")
    private String userNotFound;
    @Value("${ME_021}")
    private String updateSuccess;
    @Value("${ME_022}")
    private String alreadyVerification;
    @Value("${ME_023}")
    private String oldPasswordWrong;
    @Value("${ME_024}")
    private String newPasswordWrong;
    @Value("${ME_025}")
    private String activeSuccess;
    @Value("${ME_026}")
    private String verifiedAccount;
    @Value("${ME_027}")
    private String loginFailed;
    @Value("${page.size}")
    private Integer sizeOfPage;
    @Value("${page.school_rating_feedback.size}")
    private Integer sizeOfPageSchoolRatingFeedback;
    @Value("${server.link}")
    private String serverLink;
    @Value("${ME_028}")
    private String inValidEmail;
    @Value("${ME_029}")
    private String invalidPhoneNumber;
    @Value("${ME_030}")
    private String requiredMessage;
    @Value("${ME_031}")
    private String parentEnrolled;
    @Value("${ME_032}")
    private String schoolOwnerAccess;
    @Value("${ME_035}")
    private String invalidDate;
    @Value("${ME_036}")
    private String unenrollSuccess;
    @Value("${ME_040}")
    private String invalidFullName;
    @Value("${ME_041}")
    private String UserAddSucess;
    @Value("${ME_042}")
    private String UserUpdateSucess;
    @Value("${ME_070}")
    private String logoutSuccessFully;
    @Value("${ME_071}")
    private String registerSuccess;
    @Value("${ME_072}")
    private String accountDisabled;
    @Value("${ME_073}")
    private String accountNotActive;
    @Value("${ME_074}")
    private String updateSuccessfullMessage;
    @Value("${ME_075}")
    private String updateFailMessage;
    @Value("${ME_InvalidID}")
    private String invalidIDFormatParent;
    @Value("${ME_050}")
    private String invalidActionType;
    @Value("${ME_077}")
    private String failedSearchParent;
    @Value("${ME_078}")
    private String changedRecord;
    @Value("${ME_079}")
    private String schoolNotPublishStatus;
    @Value("${ME_076}")
    private String DateInThePass;
    @Value("${ME_043}")
    private String passwordLengthLimit;
}
