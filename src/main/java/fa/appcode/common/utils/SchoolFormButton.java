package fa.appcode.common.utils;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum SchoolFormButton {
    // Button for admin
    adminSaved(SchoolFormButtonBuild.builder().submit(true).delete(true).canEdit(true).build()),
    adminSubmitted(SchoolFormButtonBuild.builder().delete(true).reject(true).approve(true).build()),
    adminApproved(SchoolFormButtonBuild.builder().delete(true).publish(true).unPublish(true).canEdit(true).build()),
    adminRejected(SchoolFormButtonBuild.builder().delete(true).build()),
    adminPublished(SchoolFormButtonBuild.builder().delete(true).unPublish(true).canEdit(true).build()),
    adminUnpublished(SchoolFormButtonBuild.builder().publish(true).delete(true).canEdit(true).build()),
    adminDeleted(SchoolFormButtonBuild.builder().build()),

    // Button for owner
    ownerSaved(SchoolFormButtonBuild.builder().submit(true).delete(true).canEdit(true).build()),
    ownerSubmitted(SchoolFormButtonBuild.builder().delete(true).build()),
    ownerApproved(SchoolFormButtonBuild.builder().canEdit(true).delete(true).publish(true).build()),
    ownerRejected(SchoolFormButtonBuild.builder().delete(true).build()),
    ownerPublished(SchoolFormButtonBuild.builder().unPublish(true).delete(true).canEdit(true).build()),
    ownerUnpublished(SchoolFormButtonBuild.builder().publish(true).delete(true).canEdit(true).build()),
    ownerDeleted(SchoolFormButtonBuild.builder().build());

    private final SchoolFormButtonBuild schoolFormButtonBuild;
}