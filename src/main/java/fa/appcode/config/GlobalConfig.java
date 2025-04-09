package fa.appcode.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Component
@PropertySource("classpath:messages.properties")
@PropertySource("classpath:webconfig.properties")
@PropertySource("classpath:application.properties")
@Getter
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

    @Value("${ME_045}")
    private String ResetSuccessTitle;

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

    @Value("${ME_080}")
    private String deleteFailed;

    @Value("${ME_081}")
    private String feeFromLessThanFeeTo;

    @Value("${ME_082}")
    private String schoolNameNotNull;

    @Value("${ME_083}")
    private String schoolTypeNotNull;

    @Value("${ME_084}")
    private String cityNotNull;

    @Value("${ME_085}")
    private String districtNotNull;

    @Value("${ME_086}")
    private String wardNotNull;

    @Value("${ME_087}")
    private String addressNotNull;

    @Value("${ME_088}")
    private String emailNotNull;

    @Value("${ME_089}")
    private String phoneNumberNotNull;

    @Value("${ME_090}")
    private String childReceivingAgeNotNull;

    @Value("${ME_091}")
    private String educationMethodNotNull;

    @Value("${ME_092}")
    private String feeFromNotNull;

    @Value("${ME_093}")
    private String feeToNotNull;

    @Value("${ME_094}")
    private String schoolNameTooLong;

    @Value("${ME_095}")
    private String addressTooLong;

    @Value("${ME_096}")
    private String invalidEmailFormat;

    @Value("${ME_097}")
    private String emailTooLong;

    @Value("${ME_098}")
    private String invalidPhoneNumberFormat;

    @Value("${ME_099}")
    private String phoneNumberTooLong;

    @Value("${ME_100}")
    private String feeFromNegative;

    @Value("${ME_101}")
    private String feeFromExceedsLimit;

    @Value("${ME_102}")
    private String feeToNegative;

    @Value("${ME_103}")
    private String feeToExceedsLimit;

    @Value("${ME_104}")
    private String schoolIntroTooLong;

    @Value("${ME_105}")
    private String imageMustBePng;

    @Value("${ME_106}")
    private String approveSuccess;

    @Value("${ME_107}")
    private String approveFailed;

    @Value("${ME_108}")
    private String rejectSuccess;

    @Value("${ME_109}")
    private String rejectFailed;

    @Value("${ME_110}")
    private String publishSuccess;

    @Value("${ME_111}")
    private String publishFailed;

    @Value("${ME_112}")
    private String unpublishSuccess;

    @Value("${ME_113}")
    private String unpublishFailed;

    @Value("${ME_114}")
    private String deleteSuccess;

    @Value("${ME_115}")
    private String updateFailed;

    @Value("${ME_116}")
    private String saveSchoolFailed;

    @Value("${ME_117}")
    private String submitSuccess;

    @Value("${ME_118}")
    private String submitFailed;

    @Value("${ME_119}")
    private String fromDateGreaterThanToDate;

    @Value("${ME_051}")
    private String searchLengthLimit;

    @Value("${ME_052}")
    private String invalidPageNumber;
    @Value("${ME_126}")
    private String inquiriesRequired;
    @Value("${ME_127}")
    private String inquiriesLength;
    @Value("${ME_128}")
    private String requestSuccess;
    @Value("${ME_129}")
    private String requestFailed;
    @Value("${ME_130}")
    private String phoneLength;
    @Value("${ME_131}")
    private String phoneFormat;
    @Value("${ME_132}")
    private String emailRequired;
    @Value("${ME_133}")
    private String emailLength;
    @Value("${ME_134}")
    private String emailFormat;
    @Value("${ME_135}")
    private String fullNameRequired;
    @Value("${ME_136}")
    private String fullNameLength;

    @Value("${ME_044}")
    private String invalidRecordNo;

    @Value("${ME_046}")
    private String noChangeToUpdate;
    @Value("${ME_053}")
    private String dontChangeAnything;

    @Value("${ME_120}")
    private String invalidFromDate;

    @Value("${ME_121}")
    private String invalidToDate;

    @Value("${ME_123}")
    private String fullAddressLengthLimit;
}

