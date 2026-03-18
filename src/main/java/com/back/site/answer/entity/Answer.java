package com.back.site.answer.entity;

import com.back.global.entity.BaseEntity;
import com.back.site.question.entitiy.Question;
import com.back.site.user.entitiy.SiteUser;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Set;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Answer extends BaseEntity {

    @Column(columnDefinition = "TEXT")
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    private Question question;

    @ManyToOne
    private SiteUser author;

    @ManyToMany
    Set<SiteUser> voter;

    public Answer(Question question, String content, SiteUser author){
        this.question = question;
        this.content = content;
        this.author = author;
    }

    public void update(String content) {
        this.content = content;
    }
}
