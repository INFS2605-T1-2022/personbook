/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package au.edu.unsw.infs2605.personbook;

import java.time.LocalDateTime;

/**
 *
 * @author blair
 */
public class CaseNote {
    private LocalDateTime createTime;
    private String caseNoteText;

    
    @Override
    public String toString() {
        return "Case Note " + HelperForData.formatLocalDateTime(createTime);
    }

    public CaseNote() {
        this.createTime = LocalDateTime.now();
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }

    public String getCaseNoteText() {
        return caseNoteText;
    }

    public void setCaseNoteText(String caseNoteText) {
        this.caseNoteText = caseNoteText;
    }
}