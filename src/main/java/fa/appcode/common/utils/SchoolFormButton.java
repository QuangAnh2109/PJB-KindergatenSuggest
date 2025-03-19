package fa.appcode.common.utils;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum SchoolFormButton {
    // Button for admin
    admin1(SchoolFormButtonBuild.builder().delete(true).build()),
    admin2(SchoolFormButtonBuild.builder().delete(true).reject(true).approve(true).build()),
    admin3(SchoolFormButtonBuild.builder().delete(true).publish(true).unPublish(true).canEdit(true).build()),
    admin4(SchoolFormButtonBuild.builder().delete(true).build()),
    admin5(SchoolFormButtonBuild.builder().delete(true).unPublish(true).canEdit(true).build()),
    admin6(SchoolFormButtonBuild.builder().publish(true).delete(true).canEdit(true).build()),
    admin7(SchoolFormButtonBuild.builder().build()),

    // Button for owner
    owner1(SchoolFormButtonBuild.builder().submit(true).delete(true).canEdit(true).build()),
    owner2(SchoolFormButtonBuild.builder().delete(true).build()),
    owner3(SchoolFormButtonBuild.builder().canEdit(true).delete(true).publish(true).build()),
    owner4(SchoolFormButtonBuild.builder().delete(true).build()),
    owner5(SchoolFormButtonBuild.builder().unPublish(true).delete(true).canEdit(true).build()),
    owner6(SchoolFormButtonBuild.builder().publish(true).delete(true).canEdit(true).build()),
    owner7(SchoolFormButtonBuild.builder().build());

    private final SchoolFormButtonBuild schoolFormButtonBuild;
}