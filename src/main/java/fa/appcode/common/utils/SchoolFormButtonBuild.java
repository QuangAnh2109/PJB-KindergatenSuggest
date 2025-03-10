package fa.appcode.common.utils;

import lombok.Builder;
import lombok.Setter;
import org.springframework.ui.Model;

@Builder
public class SchoolFormButtonBuild {
    boolean saveDraft;
    boolean submit;
    boolean delete;
    boolean reject;
    boolean approve;
    boolean publish;
    boolean unPublish;
    boolean canEdit;

    public void setButton(Model model) {
        model.addAttribute("saveDraft", saveDraft);
        model.addAttribute("submit", submit);
        model.addAttribute("delete", delete);
        model.addAttribute("reject", reject);
        model.addAttribute("approve", approve);
        model.addAttribute("publish", publish);
        model.addAttribute("unpublish", unPublish);
        model.addAttribute("canEdit", canEdit);
        if(!canEdit) model.addAttribute("edit", false);
    }
}