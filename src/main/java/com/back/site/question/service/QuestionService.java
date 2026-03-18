package com.back.site.question.service;

import com.back.DataNotFoundException;
import com.back.site.answer.entity.Answer;
import com.back.site.question.entitiy.Question;
import com.back.site.question.repository.QuestionRepository;
import com.back.site.user.entitiy.SiteUser;
import com.back.site.user.service.UserService;
import jakarta.persistence.criteria.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class QuestionService {
    private final QuestionRepository questionRepository;
    private final UserService userService;

    private Specification<Question> search(String kw) {
        return new Specification<>() {
            private static final long serialVersionUID = 1L;
            @Override
            public Predicate toPredicate(Root<Question> q, CriteriaQuery<?> query, CriteriaBuilder cb) {
                query.distinct(true);  // 중복을 제거
                Join<Question, SiteUser> u1 = q.join("author", JoinType.LEFT);
                Join<Question, Answer> a = q.join("answerList", JoinType.LEFT);
                Join<Answer, SiteUser> u2 = a.join("author", JoinType.LEFT);
                return cb.or(cb.like(q.get("subject"), "%" + kw + "%"), // 제목
                        cb.like(q.get("content"), "%" + kw + "%"),      // 내용
                        cb.like(u1.get("username"), "%" + kw + "%"),    // 질문 작성자
                        cb.like(a.get("content"), "%" + kw + "%"),      // 답변 내용
                        cb.like(u2.get("username"), "%" + kw + "%"));   // 답변 작성자
            }
        };
    }

    public List<Question> getList(){
        return questionRepository.findAll();
    }

    public Question getQuestion(Integer id){
        Optional<Question> question = questionRepository.findById(id);
        if(question.isPresent()) {
            return question.get();
        }else{
            throw new DataNotFoundException("question not found");
        }
    }

    public Page<Question> getList(int page, String kw){
        List<Sort.Order> sorts = new ArrayList<>();
        sorts.add(Sort.Order.desc(("createDate")));
        Pageable pageable = PageRequest.of(page,10, Sort.by(sorts));
        return questionRepository.findAllByKeyword(kw, pageable);
    }

    public void create(String subject, String content, String username ) {
        SiteUser author = userService.getUser(username);
        Question q = new Question(subject,content, author);
        questionRepository.save(q);
    }
    @Transactional
    public void modify(Question question, String subject, String content){
        question.update(subject, content);
    }
    @Transactional
    public void delete(Question question){
        questionRepository.delete(question);
    }

    @Transactional
    public void vote(Question question, SiteUser siteUser) {
        question.getVoter().add(siteUser);
        questionRepository.save(question);
    }

}
